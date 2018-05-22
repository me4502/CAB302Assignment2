package com.me4502.supermart.store;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Optional;

public class StoreImpl implements Store {

    private String name;
    private double capital;
    private Stock inventory;
    private HashMap<String, Item> stockableItems = new HashMap<>();

    private static StoreImpl instance;

    public StoreImpl(String name) {
        if (instance != null) {
            throw new IllegalStateException("This object has already been instantiated");
        }
        this.name = name;
        this.capital = 100000;
        this.inventory = SuperMartApplication.getInstance().getStockBuilder().build();
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

    @Override
    public void addItem(Item item) {
        if (!stockableItems.containsKey(item.getName())) {
            stockableItems.put(item.getName(), item);
        }
    }

    @Override
    public Optional<Item> getItem(String name) {
        return Optional.ofNullable(stockableItems.get(name));
    }

    @Override
    public ImmutableSet<Item> getItems() {
        return ImmutableSet.copyOf(stockableItems.values());
    }
}
