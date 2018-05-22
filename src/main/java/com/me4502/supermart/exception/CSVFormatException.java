package com.me4502.supermart.exception;

/**
 * Indicates that the CSV Format was invalid in some way.
 *
 * @author Madeline Miller
 */
public class CSVFormatException extends Exception {

    /**
     * Creates a CSVFormatException with a custom message.
     *
     * @param message The message
     */
    public CSVFormatException(String message) {
        super(message);
    }
}
