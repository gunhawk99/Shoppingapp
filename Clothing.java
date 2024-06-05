package shoppingapp;

public class Clothing extends Product {
    private String size;
    private String color;
    private String material;
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public void setMaterial(String material) {
        this.material = material;
    }
    
    @Override
    public String toString() {
        return "Clothing [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description 
                + ", size=" + size + ", color=" + color + ", material=" + material + "]";
    }
}
