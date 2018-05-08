package com.me4502.supermart.store;

import java.util.OptionalDouble;

/**
 * Represents an item that can be stocked. This class is immutable.
 *
 * @author Madeline Miller
 */
public interface Item {

    /**
     * Gets the name of the item.
     *
     * @return The name of the item
     */
    String getName();

    /**
     * Gets the manufacturing cost of the item.
     *
     * @return The manufacturing cost
     */
    double getManufacturingCost();

    /**
     * Gets the sell price of the item.
     *
     * @return The sell price
     */
    double getSellPrice();

    /**
     * Gets the reorder point of the item. The item must be reordered
     * if the stock goes below this amount.
     *
     * @return The reorder point
     */
    int getReorderPoint();

    /**
     * Gets the reorder amount of this item. When a reorder occurs,
     * this many of the item will be ordered.
     *
     * @return The reorder amount
     */
    int getReorderAmount();

    /**
     * Gets the ideal temperature for this item to remain at, if applicable.
     *
     * @return The ideal temperature
     */
    OptionalDouble getIdealTemperature();

    /**
     * Builder class to build {@link Item} instances.
     *
     * @author Madeline Miller
     */
    interface Builder {

        /**
         * Sets the name of the {@link Item}.
         *
         * @param name The name
         * @return The builder, for chaining
         */
        Builder name(String name);

        /**
         * Sets the manufacturing cost of the {@link Item}.
         *
         * @param manufacturingCost The manufacturing cost
         * @return The builder, for chaining
         */
        Builder manufacturingCost(double manufacturingCost);

        /**
         * Sets the sell price of the {@link Item}.
         *
         * @param sellPrice The sell price
         * @return The builder, for chaining
         */
        Builder sellPrice(double sellPrice);

        /**
         * Sets the reorder point of the {@link Item}.
         *
         * @param reorderPoint The reorder point
         * @return The builder, for chaining
         */
        Builder reorderPoint(int reorderPoint);

        /**
         * Sets the reorder amount of the {@link Item}.
         *
         * @param reorderAmount The reorder amount
         * @return The builder, for chaining
         */
        Builder reorderAmount(int reorderAmount);

        /**
         * Sets the ideal temperature of the {@link Item}.
         *
         * This is an optional field.
         *
         * @param idealTemperature The ideal temperature
         * @return The builder, for chaining
         */
        Builder idealTemperature(double idealTemperature);

        /**
         * Builds the {@link Item} using the given values.
         *
         * If any of the required values have not been filled in, this
         * should throw an {@link IllegalStateException}.
         *
         * @return The built Item
         * @throws IllegalStateException If a value has not been filled in
         */
        Item build();

        /**
         * Resets the state of this builder.
         *
         * @return The builder, for chaining
         */
        Builder reset();
    }
}
