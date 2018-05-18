package com.me4502.supermart.store;

import java.text.NumberFormat;

import com.google.common.collect.ImmutableSet;

public class StoreImpl implements Store {
	String name;
	double capital;
	Stock inventory;
	

    private static StoreImpl instance;

    public StoreImpl(String name) {
        if (instance != null) {
            throw new IllegalStateException("This object has already been instantiated");
        }
        this.name = name;
        this.capital = 100000;
        this.inventory = new StockImpl(ImmutableSet.of());
        instance = this;
    }

    /**
     * Gets the current instance of this Singleton class.
     *
     * @return The instance
     */
    public static Store getInstance() {
        return StoreImpl.instance;
    }

    /**
     * Removes the current opened instance of this class.
     */
    public void close() {
        StoreImpl.instance = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCapital() {
        return capital;
    }

    @Override
    public void setCapital(double capital) {
        this.capital = capital;
    }

    @Override
    public String getFormattedCapital() {
    	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        return (capital > 0) ? currencyFormatter.format(this.capital) : "-" + currencyFormatter.format(this.capital * -1);
    }

    @Override
    public Stock getInventory() {
        return this.inventory;
    }

    @Override
    public void setInventory(Stock inventory) {
    	if (inventory == null) {
    		throw new IllegalArgumentException("Inventory can't be null");
    	}
        this.inventory = inventory;
    }
}
