package ssf.POAPP.model;

import java.util.List;

public class Items {
    private String name;
    private String item;
    private List<String> orderItems;
    private List<Integer> orderQuantity;
    
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

    public List<String> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(List<String> orderItems) {
        this.orderItems = orderItems;
    }
    public List<Integer> getOrderQuantity() {
        return orderQuantity;
    }
    public void setOrderQuantity(List<Integer> orderQuantity) {
        this.orderQuantity = orderQuantity;
    }




        
}
