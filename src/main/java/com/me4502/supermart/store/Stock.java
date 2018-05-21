package com.me4502.supermart.store;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.OptionalInt;

/**
 * Represents a collection of {@link Item} instances with quantities. This class is immutable.
 *
 * @author Madeline Miller
 */
public interface Stock {

    /**
     * Gets a set of all stocked items.
     *
     * @return The stocked items
     */
    ImmutableSet<Item> getStockedItems();

    /**
     * Gets a set of stocked items and their quantities.
     *
     * @return The stocked items with quantities
     */
    ImmutableSet<ImmutablePair<Item, Integer>> getStockedItemQuantities();

    /**
     * Gets the quantity of the item, if present.
     *
     * If an item is not stocked, this is empty. If it's
     * stocked, but the stock is out, this returns 0.
     *
     * @param item The item to check the quantity of
     * @return The item quantity, if present
     */
    OptionalInt getItemQuantity(Item item);

    /**
     * Gets the total amount of the items in this Stock.
     *
     * @return The total amount of items
     */
    int getTotalAmount();

    /**
     * Builder class to build {@link Stock} instances.
     *
     * @author Madeline Miller
     */
    interface Builder {

        /**
         * Adds an item to the {@link Stock} with a quantity.
         *
         * @param item The item
         * @param quantity The quantity
         * @return The builder, for chaining
         */
        Builder addStockedItem(Item item, int quantity);

        /**
         * Builds the {@link Stock} using the given values.
         *
         * If no items have been added, this will create an empty Stock.
         *
         * @return The built Stock
         */
        Stock build();

        /**
         * Resets the state of this builder.
         *
         * @return The builder, for chaining
         */
        Builder reset();
    }
}
