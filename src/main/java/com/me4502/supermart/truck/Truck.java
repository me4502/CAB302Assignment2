package com.me4502.supermart.truck;

import com.me4502.supermart.store.Stock;

/**
 * An interface for Truck that will be extended by Ordinary Truck and Refrigerated Truck. This class is immutable.
 *
 * @author Liam Dale
 */
public interface Truck {
	
	/**
     * Gets the name of the truck type.
     *
     * @return The truck type
     */
	String getType();
	
    /**
     * Gets the cost of a truck.
     *
     * @return The cost
     */
	double getCost();
	
    /**
     * Gets the cargo capacity of a truck.
     *
     * @return The cargo capacity
     */
	int getCargoCapacity();
	
    /**
     * Gets the cargo in a truck.
     *
     * @return The cargo
     */
	Stock getCargo();

    /**
     * The base builder for Truck classes.
     *
     * @param <T> The {@link Truck} type
     * @param <B> The {@link Builder} type
     */
	interface Builder<T extends Truck, B extends Builder> {
        /**
         * Sets the cost of the {@link Truck}.
         *
         * @param cost cost of hiring
         * @return The builder, for chaining
         */
        B cost(double cost);

        /**
         * Sets the cargo of the {@link Truck}.
         *
         * @param cargo cargo of the truck
         * @return The builder, for chaining
         */
        B cargo(Stock cargo);

        /**
         * Builds the {@link Truck} using the given values.
         *
         * If any of the required values have not been filled in, this
         * should throw an {@link IllegalStateException}.
         *
         * @return The built Truck
         * @throws IllegalStateException If a value has not been filled in
         */
        T build();
        
        /**
         * Resets the state of this builder.
         *
         * @return The builder, for chaining
         */
        B reset();
    }	
}
