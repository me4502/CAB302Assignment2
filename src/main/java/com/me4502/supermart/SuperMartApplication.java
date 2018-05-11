package com.me4502.supermart;

import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

/**
 * Base class for the application state.
 *
 * @author Madeline Miller
 */
public class SuperMartApplication {

    private static SuperMartApplication instance;

    /**
     * Construct a new instance of the SuperMart Application
     */
    public SuperMartApplication() {
        if (instance != null) {
            throw new IllegalStateException("Application already running!");
        }
        instance = this;
    }

    /**
     * Get the singleton instance of this class.
     *
     * @return The instance
     */
    public static SuperMartApplication getInstance() {
        return SuperMartApplication.instance;
    }

    /**
     * Get the builder for the {@link Item} class.
     *
     * @return The item builder
     */
    public Item.Builder getItemBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the builder for the {@link Stock} class.
     *
     * @return The stock builder
     */
    public Stock.Builder getStockBuilder() {
        throw new UnsupportedOperationException();
    }
}
