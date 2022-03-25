package ssf.POAPP.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private static final String URL = "https://quotation.chuklee.com";

    public Items createOrderItems(String payLoad) {

        Items newOrder = new Items();
        
        Map<String, Integer> orderItems = new HashMap<>();

        try (InputStream is = new ByteArrayInputStream(payLoad.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();

            newOrder.setName(o.getString("name"));

            if (o.containsKey("lineItems")) {
                JsonArray orderArr = o.getJsonArray("lineItems");
                for(int i = 0; i < orderArr.size(); i++) {
                    JsonObject item = orderArr.getJsonObject(i);
                    orderItems.put(item.getString("item"), item.getInt("quantity"));
                }
            }
        }   catch (IOException ex) {
                ex.printStackTrace();
            }
         
        newOrder.setOrderItems(orderItems);

        return newOrder;
    }

    public Optional<Quotation> getQuotations(List<String> items) {

        String quoteUrl = UriComponentsBuilder
            .fromUriString(URL + "/quotation")
            .toUriString();

        JsonArrayBuilder quoteItemsArr = Json.createArrayBuilder();
            quoteItemsArr.addAll((JsonArrayBuilder) items);

        RequestEntity<String> req = RequestEntity
            .post(quoteUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(quoteItemsArr.build().toString());

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);

        Quotation newQuote = new Quotation();

        InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
        JsonReader r = Json.createReader(is);
        JsonObject o = r.readObject();

        newQuote.setQuoteId(o.getString("quoteId"));

        if (o.containsKey("quotations")) {
            JsonArray quoteArr = o.getJsonArray("quotations");
            for (int i = 0; i < quoteArr.size(); i++) {
                JsonObject item = quoteArr.getJsonObject(i);
                String itemName = item.getString("item");
                Double unitPrice = Double.parseDouble(item.getString("unitPrice"));
                newQuote.addQuotation(itemName, unitPrice.floatValue());
        }
    }
    return Optional.empty();

    }
}