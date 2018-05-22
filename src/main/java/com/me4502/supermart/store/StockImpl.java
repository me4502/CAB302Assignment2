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

    private StockImpl(ImmutableSet<ImmutablePair<Item, Integer>> pairSet) {
        this.pairSet = pairSet;
    }

    // Getting the total amount of items
    @Override
    public int getTotalAmount() {
        //mapToInt unbox the Integer (i->i)
        //stock.values().stream().mapToInt(i->i).sum();
        int totalAmount = 0;
        for (ImmutablePair<Item, Integer> pair : pairSet) {
            totalAmount += pair.getRight();
        }
        return totalAmount;
    }

    // Gets a set of all stocked items
    @Override
    public ImmutableSet<Item> getStockedItems() {
        HashSet<Item> tempSet = new HashSet<>();
        for (ImmutablePair<Item, Integer> pair : pairSet) {
            tempSet.add(pair.getLeft());
        }
        return ImmutableSet.copyOf(tempSet);
    }

    // Return the stock map
    @Override
    public ImmutableSet<ImmutablePair<Item, Integer>> getStockedItemQuantities() {
        return pairSet;
    }

    @Override
    public OptionalInt getItemQuantity(Item item) {
        for (ImmutablePair<Item, Integer> pair : pairSet) {
            if (item.getName().equals(pair.getLeft().getName())) {
                return OptionalInt.of(pair.getRight());
            }
        }
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
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            if (stock.containsKey(item)) {
                if (stock.get(item) < -quantity && quantity < 0) {
                    throw new IllegalArgumentException("Cannot remove this many items");
                }
                stock.put(item, stock.get(item) + quantity);
            } else {
                if (quantity < 0) {
                    throw new IllegalArgumentException("Cannot create an item with a negative quantity");
                }
                stock.put(item, quantity); 
            }
            return this;
        }

        @Override
        public Stock build() {
            // create the immutable set here
            // add the immutable set of immutable pairs as a parameter
            ImmutableSet<ImmutablePair<Item, Integer>> pairSet = stock.entrySet().stream()
                    .map(e -> ImmutablePair.of(e.getKey(), e.getValue()))
                    .collect(ImmutableSet.toImmutableSet());
            return new StockImpl(pairSet);
        }

        @Override
        public Builder reset() {
            // clear the map
            stock.clear();
            return this;
        }
    }
}