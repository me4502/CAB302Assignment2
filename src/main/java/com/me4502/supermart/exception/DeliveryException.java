package com.me4502.supermart.exception;

/**
 * Indicates that there is an issue with the delivery in some way.
 *
 * @author Madeline Miller
 */
public class DeliveryException extends Exception {

    /**
     * Creates a new DeliveryException with a custom message.
     *
     * @param message The message
     */
    public DeliveryException(String message) {
        super(message);
    }
}
