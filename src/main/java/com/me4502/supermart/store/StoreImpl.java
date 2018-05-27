package com.me4502.supermart.store;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.exception.DeliveryException;
import com.me4502.supermart.truck.Manifest;
import com.me4502.supermart.truck.Truck;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Optional;

/**
 * Implementation for the {@link Store} interface.
 *
 * @author Liam Dale
 */
public class StoreImpl implements Store {

    private String name;
    private double capital;
    private Stock inventory;
    private HashMap<String, Item> stockableItems = new HashMap<>();
    private Manifest manifest;
    private static StoreImpl instance;

    /**
     * Create the singleton instance
     *
     * @param name The name of this store
     */
    public StoreImpl(String name) {
        if (instance != null) {
            throw new IllegalStateException("This object has already been instantiated");
        }
        // Construct with chosen name
        this.name = name;
        // Initial capital is $100,000
        this.capital = 100000;
        // Start with empty inventory and manifest
        this.inventory = SuperMartApplication.getInstance().getStockBuilder().build();
        this.manifest = SuperMartApplication.getInstance().getManifestBuilder().build();
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
        return this.name;
    }

    @Override
    public double getCapital() {
        return this.capital;
    }

    @Override
    public void setCapital(double capital) {
        this.capital = capital;
    }

    @Override
    public String getFormattedCapital() {
    	// Create a number formatter to apply to capital
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        // Ternary operator which formats differently for positive and negative values
        return (this.capital > 0) ? currencyFormatter.format(this.capital) : "-" + currencyFormatter.format(this.capital * -1);
    }

    @Override
    public Stock getInventory() {
        return this.inventory;
    }

    @Override
    public void setInventory(Stock inventory) {
    	// If inventory is null throw an exception
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory can't be null");
        }
        this.inventory = inventory;
    }

    @Override
    public void addItem(Item item) {
    	// Before adding the item check there are no duplicate names
        if (!this.stockableItems.containsKey(item.getName())) {
            this.stockableItems.put(item.getName(), item);
        }
    }

    @Override
    public Optional<Item> getItem(String name) {
        return Optional.ofNullable(this.stockableItems.get(name));
    }

    @Override
    public ImmutableSet<Item> getItems() {
        return ImmutableSet.copyOf(this.stockableItems.values());
    }

    @Override
    public Manifest getManifest() {
        return this.manifest;
    }

    @Override
    public void setManifest(Manifest manifest, boolean update) throws DeliveryException {
        // Manifest is not nullable
        if (manifest == null) {
            throw new IllegalArgumentException("Manifest can't be null");
        }

        // If the inventory and capital need to be updated
        if (update) {
            // Create a builder for the new inventory
            Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();

            // Create the new stock, based off what is currently there
            for (ImmutablePair<Item, Integer> itemPair : getInventory().getStockedItemQuantities()) {
                stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
            }

            // Find the value of the manifest while continuing to add to the new inventory
            double totalValue = 0;
            for (Truck truck : manifest.getTrucks()) {
                // Sum value of trucks
                totalValue += truck.getCost();
                // Sum value of item manufacturing costs and add items to builder (csv checks if stockable)
                for (ImmutablePair<Item, Integer> itemPair : truck.getCargo().getStockedItemQuantities()) {                    
                    totalValue += itemPair.getLeft().getManufacturingCost() * itemPair.getRight();
                    stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
                }
            }

            // Update the inventory and the store capital
            setInventory(stockBuilder.build());
            setCapital(StoreImpl.getInstance().getCapital() - totalValue);
        }

        this.manifest = manifest;
    }
}
