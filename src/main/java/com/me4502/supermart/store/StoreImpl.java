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
 *
 */
public class StoreImpl implements Store {

    private String name;
    private double capital;
    private Stock inventory;
    private HashMap<String, Item> stockableItems = new HashMap<>();
    private Manifest manifest;

    private static StoreImpl instance;

    public StoreImpl(String name) {
        if (instance != null) {
            throw new IllegalStateException("This object has already been instantiated");
        }
        this.name = name;
        this.capital = 100000;
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

    @Override
    public Manifest getManifest() {
        return manifest;
    }

    @Override
    public void setManifest(Manifest manifest, boolean update) throws DeliveryException {
    	if (update) {
			if (manifest == null) {
				throw new IllegalArgumentException("Manifest can't be null");
			}
		
			Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
		
		    // Create the new stock
		    for (ImmutablePair<Item, Integer> itemPair : getInventory().getStockedItemQuantities()) {
		        stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
		    }
		
		    // Find the value of the manifest while continuing to create the new stock
		    double totalValue = 0;
		    for (Truck truck : manifest.getTrucks()) {
		        totalValue += truck.getCost();
		        for (ImmutablePair<Item, Integer> itemPair : truck.getCargo().getStockedItemQuantities()) {
		            if (StoreImpl.getInstance().getItem(itemPair.getLeft().getName()).isPresent()) {
		                totalValue += itemPair.getLeft().getManufacturingCost() * itemPair.getRight();
		                stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
		            } else {
		                throw new DeliveryException("Store doesn't stock " + itemPair.getLeft().getName() + ", but manifest log contains it.");
		            }
		        }
		    }
		
		    // Update the stock and the store capital
		    setInventory(stockBuilder.build());
		    setCapital(StoreImpl.getInstance().getCapital() - totalValue);
    	}
    	
        this.manifest = manifest;
    }
}
