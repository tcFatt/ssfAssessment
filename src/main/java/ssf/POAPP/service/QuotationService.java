package ssf.POAPP.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.POAPP.Quotation;
import ssf.POAPP.model.Items;

@Service
public class QuotationService {

    private static final Logger logger = LoggerFactory.getLogger(QuotationService.class);


    private static final String URL = "https://quotation.chuklee.com/quotation";

    public Items createOrderItems(String payLoad) {

        Items newOrder = new Items();
        
        try (InputStream is = new ByteArrayInputStream(payLoad.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();

            newOrder.setName(o.getString("name"));
            logger.info(">>> name : " + newOrder.getName());

            List<String> orderItems = new ArrayList<>();
            List<Integer> orderQuantity = new ArrayList<>();

            if (o.containsKey("lineItems")) {
                JsonArray orderArr = o.getJsonArray("lineItems");
                for(int i = 0; i < orderArr.size(); i++) {
                    JsonObject item = orderArr.getJsonObject(i);
                    orderItems.add(item.getString("item"));
                    orderQuantity.add(item.getInt("quantity"));
                }
                newOrder.setOrderItems(orderItems);
                newOrder.setOrderQuantity(orderQuantity);
                // Map not allow 2 same key, create 2 List instead.
                logger.info(">>> orderItems:" + orderItems);
                logger.info(">>> orderQuantity:" + orderQuantity);

            }
        }   catch (IOException ex) {
                ex.printStackTrace();
            }
         
        return newOrder;
    }

    public Optional<Quotation> getQuotations(List<String> items) {

        String quoteUrl = UriComponentsBuilder
            .fromUriString(URL)
            .toUriString();

        JsonArrayBuilder quoteItemsArr = Json.createArrayBuilder();
            for (String i : items) {
                quoteItemsArr.add(i);
            }
        
        RequestEntity<String> req = RequestEntity
            .post(quoteUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(quoteItemsArr.build().toString());

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);
        
        logger.info(">>> resp.body : " + resp.getBody());

        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            Quotation newQuote = new Quotation();

            newQuote.setQuoteId(o.getString("quoteId"));
            logger.info(">>> quoteId : " + newQuote.getQuoteId());

            if (o.containsKey("quotations")) {
                JsonArray quotationsArray = o.getJsonArray("quotations");
                for(int i = 0; i < quotationsArray.size(); i++) {
                    JsonObject item = quotationsArray.getJsonObject(i);
                    String itemName = item.getString("item");
                    Double unitPrice = item.getJsonNumber("unitPrice").doubleValue();

                    newQuote.addQuotation(itemName, unitPrice.floatValue());
                }
            logger.info(">>> Quotation : " + newQuote.getQuotations());

            }
            return Optional.of(newQuote);

        } catch (IOException ex) { 
            ex.printStackTrace();

            return Optional.empty();
        }
    }
}