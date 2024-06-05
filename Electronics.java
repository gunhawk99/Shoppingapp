package shoppingapp;

public class Electronics extends Product {
    private String brand;
    private String model;
    private int warrantyPeriod; // in months
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }
    
    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
    
    @Override
    public String toString() {
        return "Electronics [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description 
                + ", brand=" + brand + ", model=" + model + ", warrantyPeriod=" + warrantyPeriod + " months]";
    }
}
