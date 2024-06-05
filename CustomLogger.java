package shoppingapp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogger {
    private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void log(Level level, String message, Throwable thrown) {
        logger.log(level, message, thrown);
    }
}
