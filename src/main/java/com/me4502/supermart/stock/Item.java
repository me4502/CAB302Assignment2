package com.me4502.supermart.stock;

/**
 * Represents an item that can be stocked. This class is immutable.
 *
 * @author Madeline Miller
 */
public interface Item {

    /**
     * Get's the name of the item.
     *
     * @return The name of the item
     */
    String getName();

    /**
     * Get's the manufacturing cost of the item.
     *
     * @return The manufacturing cost
     */
    double getManufacturingCost();

    /**
     * Get's the sell price of the item.
     *
     * @return The sell price
     */
    double getSellPrice();

    /**
     * Get's the reorder point of the item. The item must be reordered
     * if the stock goes below this amount.
     *
     * @return The reorder point
     */
    int getReorderPoint();

    /**
     * Get's the reorder amount of this item. When a reorder occurs,
     * this many of the item will be ordered.
     *
     * @return The reorder amount
     */
    int getReorderAmount();

    /**
     * Get's the ideal temperature for this item to remain at.
     *
     * @return The ideal temperature
     */
    double getIdealTemperature();

    /**
     * Builder class to build Item instances.
     *
     * @author Madeline Miller
     */
    interface Builder {

        /**
         * Set's the name of the {@link Item}.
         *
         * @param name The name
         * @return The builder, for chaining
         */
        Builder name(String name);

        /**
         * Set's the manufacturing cost of the {@link Item}.
         *
         * @param manufacturingCost The manufacturing cost
         * @return The builder, for chaining
         */
        Builder manufacturingCost(double manufacturingCost);

        /**
         * Set's the sell price of the {@link Item}.
         *
         * @param sellPrice The sell price
         * @return The builder, for chaining
         */
        Builder sellPrice(double sellPrice);

        /**
         * Set's the reorder point of the {@link Item}.
         *
         * @param reorderPoint The reorder point
         * @return The builder, for chaining
         */
        Builder reorderPoint(int reorderPoint);

        /**
         * Set's the reorder amount of the {@link Item}.
         *
         * @param reorderAmount The reorder amount
         * @return The builder, for chaining
         */
        Builder reorderAmount(int reorderAmount);

        /**
         * Set's the ideal temperature of the {@link Item}.
         *
         * @param idealTemperature The ideal temperature
         * @return The builder, for chaining
         */
        Builder idealTemperature(double idealTemperature);

        /**
         * Builds the {@link Item} using the given values.
         *
         * If any of the values have not been filled in, this
         * should throw an {@link IllegalStateException}.
         *
         * @return The build Item
         * @throws IllegalStateException If a value has not been filled in
         */
        Item build();

        /**
         * Resets the state of this builder.
         */
        void reset();
    }
}
