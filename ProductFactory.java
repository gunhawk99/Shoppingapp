package shoppingapp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductFactory {
    private static final Logger logger = Logger.getLogger(ProductFactory.class.getName());

    public static Product createProduct(String type, int id, String name, double price, String description, String... additionalAttributes) {
        try {
            switch (type.toLowerCase()) {
                case "electronics":
                    Electronics electronics = new Electronics();
                    electronics.setId(id);
                    electronics.setName(name);
                    electronics.setPrice(price);
                    electronics.setDescription(description);
                    electronics.setBrand(additionalAttributes[0]);
                    electronics.setModel(additionalAttributes[1]);
                    electronics.setWarrantyPeriod(Integer.parseInt(additionalAttributes[2]));
                    return electronics;
                case "clothing":
                    Clothing clothing = new Clothing();
                    clothing.setId(id);
                    clothing.setName(name);
                    clothing.setPrice(price);
                    clothing.setDescription(description);
                    clothing.setSize(additionalAttributes[3]);
                    clothing.setColor(additionalAttributes[4]);
                    clothing.setMaterial(additionalAttributes[5]);
                    return clothing;
                default:
                    throw new IllegalArgumentException("Unknown product type: " + type);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating product: {0}", e.getMessage());
            throw new RuntimeException("Product creation failed", e);
        }
    }
}
