package com.me4502.supermart.exception;

/**
 * Indicates that there is an issue with the stock in some way.
 *
 * @author Madeline Miller
 */
public class StockException extends Exception {

    /**
     * Creates a new StockException with a custom message.
     *
     * @param message The message
     */
    public StockException(String message) {
        super(message);
    }
}
