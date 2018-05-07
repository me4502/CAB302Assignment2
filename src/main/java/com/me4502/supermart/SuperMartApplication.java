package com.me4502.supermart;

import com.me4502.supermart.stock.Item;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Base class for the application state.
 *
 * @author Madeline Miller
 */
public class SuperMartApplication {

    private static SuperMartApplication instance;

    /**
     * Construct a new instance of the Supermart Application
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
     * Get the builder for the Item class.
     *
     * @return The item builder
     */
    public Item.Builder getItemBuilder() {
        throw new NotImplementedException();
    }
}
