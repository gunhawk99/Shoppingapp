package shoppingapp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentProcessor {
    private static final Logger logger = Logger.getLogger(PaymentProcessor.class.getName());

    public boolean processPayment(double amount) {
        if (amount > 0) {
            // Simulate successful payment processing
            logger.log(Level.INFO, "Payment of ${0} processed successfully.", amount);
            return true;
        } else {
            // Simulate payment failure
            logger.log(Level.WARNING, "Payment of ${0} failed. Amount must be greater than zero.", amount);
            return false;
        }
    }
}
