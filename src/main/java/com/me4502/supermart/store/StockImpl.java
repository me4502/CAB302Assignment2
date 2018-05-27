package com.me4502.supermart.store;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.OptionalInt;

/**
 *
 * Implementation for {@link Stock}.
 *
 * @author Liam Dale
 *
 */
public class StockImpl implements Stock {

    private ImmutableSet<ImmutablePair<Item, Integer>> pairSet;

    /**
     * 
     * Creates Stock
     * 
     * @param pairSet of Items and quantities
     */
    private StockImpl(ImmutableSet<ImmutablePair<Item, Integer>> pairSet) {
        this.pairSet = pairSet;
    }

    @Override
    public int getTotalAmount() {
        int totalAmount = 0;
        // Iterate over pairs in pairSet and sum quantities
        for (ImmutablePair<Item, Integer> pair : this.pairSet) {
            totalAmount += pair.getRight();
        }
        return totalAmount;
    }

    @Override
    public ImmutableSet<Item> getStockedItems() {
    	// Create a mutable set
        HashSet<Item> tempSet = new HashSet<>();
        // Iterate over pairs in pairSet and add to this set
        for (ImmutablePair<Item, Integer> pair : this.pairSet) {
            tempSet.add(pair.getLeft());
        }
        // Return items as an immutable set
        return ImmutableSet.copyOf(tempSet);
    }

    @Override
    public ImmutableSet<ImmutablePair<Item, Integer>> getStockedItemQuantities() {
        return this.pairSet;
    }

    @Override
    public OptionalInt getItemQuantity(Item item) {
    	// Iterate over pairSet and if a match is found return an optional of the quantity
        for (ImmutablePair<Item, Integer> pair : this.pairSet) {
            if (item.getName().equals(pair.getLeft().getName())) {
                return OptionalInt.of(pair.getRight());
            }
        }
        // If no match is found return an empty optional
        return OptionalInt.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @author Liam Dale
     */
    public static class StockBuilder implements Stock.Builder {

        private HashMap<Item, Integer> stock = new HashMap<>();

        @Override
        public Builder addStockedItem(Item item, int quantity) {
        	// Item added can't be null
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            // If this item is already in the hashmap, sum its quantities
            if (this.stock.containsKey(item)) {
            	// If removing and removing more than quantity stocked, throw an exception
                if (quantity < 0 && this.stock.get(item) < -quantity) {
                    throw new IllegalArgumentException("Cannot sell this many items");
                }
                this.stock.put(item, this.stock.get(item) + quantity);
            } else {
            	// Can't add items with a negative quantity, throw an exception if trying
                if (quantity < 0) {
                    throw new IllegalArgumentException("Cannot create an item with a negative quantity");
                }
                this.stock.put(item, quantity);
            }
            return this;
        }

        @Override
        public Stock build() {
        	// Convert to ImmutableSet<Item, Integer>>
            ImmutableSet<ImmutablePair<Item, Integer>> pairSet = this.stock.entrySet().stream()
                    .map(e -> ImmutablePair.of(e.getKey(), e.getValue()))
                    .collect(ImmutableSet.toImmutableSet());
            // Return a built item with the StockImpl constructor
            return new StockImpl(pairSet);
        }

        @Override
        public Builder reset() {
        	// Empty fields
            this.stock.clear();
            return this;
        }
    }
}