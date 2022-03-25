package ssf.POAPP.model;

import java.util.Map;

public class Items {
    private String name;
    private String item;
    private String quantity;
    public Map<String, Integer> orderItems;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(Map<String, Integer> orderItems) {
        this.orderItems = orderItems;
    }
    



    
}
