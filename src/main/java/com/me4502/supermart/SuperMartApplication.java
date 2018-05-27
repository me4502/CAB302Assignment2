package com.me4502.supermart;

import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.ItemImpl;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.store.StockImpl;
import com.me4502.supermart.truck.Manifest;
import com.me4502.supermart.truck.ManifestImpl;
import com.me4502.supermart.truck.OrdinaryTruck;
import com.me4502.supermart.truck.OrdinaryTruckImpl;
import com.me4502.supermart.truck.RefrigeratedTruck;
import com.me4502.supermart.truck.RefrigeratedTruckImpl;

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
        // Set the singleton instance, or fail if already set
        if (instance != null) {
            throw new IllegalStateException("Application already running!");
        }
        instance = this;
    }

    /**
     * Removes the current opened instance of this class.
     */
    public void close() {
        instance = null;
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
        return new ItemImpl.ItemBuilder();
    }

    /**
     * Get the builder for the {@link Stock} class.
     *
     * @return The stock builder
     */
    public Stock.Builder getStockBuilder() {
        return new StockImpl.StockBuilder();
    }

    /**
     * Get the builder for the {@link OrdinaryTruck} class.
     *
     * @return The ordinary truck builder
     */
    public OrdinaryTruck.OrdinaryBuilder getOrdinaryTruckBuilder() {
        return new OrdinaryTruckImpl.OrdinaryTruckBuilderImpl();
    }

    /**
     * Get the builder for the {@link RefrigeratedTruck} class.
     *
     * @return The refrigerated truck builder
     */
    public RefrigeratedTruck.RefrigeratedBuilder getRefrigeratedTruckBuilder() {
        return new RefrigeratedTruckImpl.RefrigeratedTruckBuilderImpl();
    }


    /**
     * Get the builder for the {@link Manifest} class.
     *
     * @return The manifest builder
     */
    public Manifest.Builder getManifestBuilder() {
        return new ManifestImpl.ManifestBuilderImpl();
    }
}
