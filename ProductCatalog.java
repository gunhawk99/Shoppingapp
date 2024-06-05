package shoppingapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProductCatalog {
    private Connection connection;
    private static final Logger logger = Logger.getLogger(ProductCatalog.class.getName());

    public ProductCatalog() {
        try {
            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping_system", "root", "Spring2023@depaul");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error: {0}", e.getMessage());
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public void addProduct(Product product) {
        String query = "INSERT INTO products (type, name, price, description, brand, model, warranty_period, size, color, material) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product instanceof Electronics ? "electronics" : "clothing");
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getDescription());
            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                stmt.setString(5, electronics.getBrand());
                stmt.setString(6, electronics.getModel());
                stmt.setInt(7, electronics.getWarrantyPeriod());
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.INTEGER);
                stmt.setString(8, clothing.getSize());
                stmt.setString(9, clothing.getColor());
                stmt.setString(10, clothing.getMaterial());
            }
            stmt.executeUpdate();
            logger.log(Level.INFO, "Product added: {0}", product);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding product: {0}", e.getMessage());
            throw new RuntimeException("Failed to add product", e);
        }
    }

    public void removeProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Product removed with ID: {0}", productId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error removing product: {0}", e.getMessage());
            throw new RuntimeException("Failed to remove product", e);
        }
    }

    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                Product product = ProductFactory.createProduct(
                        type,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("warranty_period"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material")
                );
                logger.log(Level.INFO, "Product retrieved: {0}", product);
                return product;
            } else {
                logger.log(Level.WARNING, "Product not found with ID: {0}", productId);
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving product: {0}", e.getMessage());
            throw new RuntimeException("Failed to retrieve product", e);
        }
    }

    public List<Product> listProducts() {
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                Product product = ProductFactory.createProduct(
                        type,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("warranty_period"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material")
                );
                products.add(product);
            }
            logger.log(Level.INFO, "Listing all products");
            return products;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing products: {0}", e.getMessage());
            throw new RuntimeException("Failed to list products", e);
        }
    }

    public void addProductUsingFactory(String type, int id, String name, double price, String description, String... additionalAttributes) {
        try {
            Product product = ProductFactory.createProduct(type, id, name, price, description, additionalAttributes);
            addProduct(product);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding product using factory: {0}", e.getMessage());
            throw new RuntimeException("Failed to add product using factory", e);
        }
    }
}
