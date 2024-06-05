package shoppingapp;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderProcessing {
    private static OrderProcessing instance;
    private static final Logger logger = Logger.getLogger(OrderProcessing.class.getName());
    private PaymentProcessor paymentProcessor;

    public OrderProcessing() {
        try {
            DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping_system", "root", "Spring2023@depaul");
            paymentProcessor = new PaymentProcessor();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error: {0}", e.getMessage());
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public static OrderProcessing getInstance() {
        if (instance == null) {
            instance = new OrderProcessing();
        }
        return instance;
    }

    public void placeOrder(Order order) {
        try {
            if (paymentProcessor.processPayment(order.getTotalPrice())) {
                order.saveOrder();
                logger.log(Level.INFO, "Order placed successfully: {0}", order.getOrderId());
            } else {
                logger.log(Level.WARNING, "Payment failed. Order not placed.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error placing order: {0}", e.getMessage());
            throw new RuntimeException("Failed to place order", e);
        }
    }

    public void cancelOrder(int orderId) {
        try {
            Order order = Order.getOrderById(orderId, new ProductCatalog());
            if (order != null) {
                order.deleteOrder();
                logger.log(Level.INFO, "Order canceled successfully: {0}", orderId);
            } else {
                logger.log(Level.WARNING, "Order not found for cancellation: {0}", orderId);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error canceling order: {0}", e.getMessage());
            throw new RuntimeException("Failed to cancel order", e);
        }
    }
}
