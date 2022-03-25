package ssf.POAPP.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import ssf.POAPP.Quotation;
import ssf.POAPP.model.Items;
import ssf.POAPP.service.QuotationService;


@RestController
public class PurchaseOrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderRestController.class);

    @Autowired
    private QuotationService quotationSvc;

    @PostMapping(path = "/api/po", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPurchaseOrder(@RequestBody String payload) {

        logger.info(">>> payload:" + payload);

        Items orderItems = quotationSvc.createOrderItems(payload);

        List<String> itemOnly = new ArrayList<String>(orderItems.getOrderItems().keySet());

        logger.info(">>> itemOnly:" + itemOnly);

        Optional<Quotation> orderQuotation = quotationSvc.getQuotations(itemOnly);

        if (orderQuotation.isPresent()) {
            Quotation outOfOQuotation = orderQuotation.get();
            
            float totalPrice = 0.0f;

            for (String i : orderItems.getOrderItems().keySet()) {
                Float unitPrice = outOfOQuotation.getQuotation(i);
                logger.info(">>> unitPrice:" + unitPrice);
                Integer quantity = orderItems.getOrderItems().get(i);
                logger.info(">>> quantity:" + quantity);
                totalPrice = totalPrice + (unitPrice * quantity);
                logger.info(">>>>>> totalPrice:" + totalPrice);
            }

            JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("invoiceId", outOfOQuotation.getQuoteId())
                .add("name", orderItems.getName())
                .add("total", totalPrice);
            
        return ResponseEntity.status(HttpStatus.CREATED).body(builder.build().toString());

        } else {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
       
    }

    
}
