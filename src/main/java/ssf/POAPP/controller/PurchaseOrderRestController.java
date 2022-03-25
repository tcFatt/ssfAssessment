package ssf.POAPP.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ssf.POAPP.model.Items;
import ssf.POAPP.service.QuotationService;


@RestController
public class PurchaseOrderRestController {

    @Autowired
    private QuotationService quotationSvc;

    @PostMapping(path = "/api/po", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPurchaseOrder(@RequestBody String payload) {

        Items orderItems = quotationSvc.createOrderItems(payload);


 

    return ResponseEntity.status(HttpStatus.OK).build();
        
    }

    
}
