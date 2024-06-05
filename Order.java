package  shoppingapp;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Order {
    private int orderId;
    private int userId;
    private List<Product> productList = new ArrayList<>();
    private double totalPrice;
    private Connection connection;
    private static final Logger logger = Logger.getLogger(Order.class.getName());
    private ProductCatalog productCatalog;

    public Order(int userId, ProductCatalog productCatalog) {
        this.userId = userId;
        this.productCatalog = productCatalog;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping_system", "root", "Spring2023@depaul");
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public void addProduct(Product product) {
        productList.add(product);
        calculateTotal();
        saveOrder();
    }
    
    public void removeProduct(int productId) {
        Product productToRemove = null;
        for (Product product : productList) {
            if (product.getId() == productId) {
                productToRemove = product;
                break;
            }
        }
        if (productToRemove != null) {
            productList.remove(productToRemove);
            calculateTotal();
            saveOrder();
        } else {
            CustomLogger.log(Level.WARNING, "Product with ID " + productId + " not found in cart.");
        }
    }

    private void calculateTotal() {
        totalPrice = 0;
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        calculateTotal();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void saveOrder() {
        if (orderId == 0) {
            String insertOrderQuery = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setDouble(2, totalPrice);
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
                
                saveOrderProducts();
                
                CustomLogger.log(Level.INFO, "Order saved with ID: " + orderId);
            } catch (SQLException e) {
                CustomLogger.log(Level.SEVERE, "Error saving order: " + e.getMessage(), e);
                throw new RuntimeException("Failed to save order", e);
            }
        } else {
            String updateOrderQuery = "UPDATE orders SET total_price = ? WHERE order_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateOrderQuery)) {
                stmt.setDouble(1, totalPrice);
                stmt.setInt(2, orderId);
                stmt.executeUpdate();
                
                saveOrderProducts();
                
                CustomLogger.log(Level.INFO, "Order updated with ID: " + orderId);
            } catch (SQLException e) {
                CustomLogger.log(Level.SEVERE, "Error updating order: " + e.getMessage(), e);
                throw new RuntimeException("Failed to update order", e);
            }
        }
    }

    private void saveOrderProducts() {
        String deleteOrderProductsQuery = "DELETE FROM order_products WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteOrderProductsQuery)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error deleting order products: " + e.getMessage(), e);
            throw new RuntimeException("Failed to delete order products", e);
        }

        String insertOrderProductsQuery = "INSERT INTO order_products (order_id, product_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertOrderProductsQuery)) {
            for (Product product : productList) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, product.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error saving order products: " + e.getMessage(), e);
            throw new RuntimeException("Failed to save order products", e);
        }
    }

    public static Order getOrderById(int orderId, ProductCatalog productCatalog) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping_system", "root", "Spring2023@depaul");
            String query = "SELECT * FROM orders WHERE order_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, orderId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    Order order = new Order(userId, productCatalog);
                    order.setOrderId(orderId);
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setProductList(getProductsByOrderId(orderId, connection, productCatalog));
                    return order;
                } else {
                    CustomLogger.log(Level.WARNING, "Order not found with ID: " + orderId);
                    return null;
                }
            }
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error retrieving order: " + e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve order", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                CustomLogger.log(Level.SEVERE, "Error closing connection: " + e.getMessage());
            }
        }
    }

    private static List<Product> getProductsByOrderId(int orderId, Connection connection, ProductCatalog productCatalog) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.* FROM products p JOIN order_products op ON p.id = op.product_id WHERE op.order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = productCatalog.getProductById(rs.getInt("id"));
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error retrieving products for order: " + e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve products for order", e);
        }
        return products;
    }

    public void clearCart() {
        productList.clear();
        totalPrice = 0;
        orderId = 0;
    }

    public void deleteOrder() {
        String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteOrderQuery)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            CustomLogger.log(Level.INFO, "Order deleted with ID: " + orderId);
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error deleting order: " + e.getMessage(), e);
            throw new RuntimeException("Failed to delete order", e);
        }
    }
}
