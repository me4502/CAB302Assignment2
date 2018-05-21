package com.me4502.supermart.truck;

import com.google.common.collect.ImmutableSet;
/**
 * An interface for manifests 
 *
 * @author Liam Dale
 */
public interface Manifest {

	/**
     * Gets the set of trucks in the manifest.
     *
     * @return The set of trucks
     */
    ImmutableSet<Truck> getTrucks();

    /**
     * Builder class to build {@link Manifest} instances.
     *
     * @author Liam Dale
     */
    interface Builder {

    	/**
         * Adds a truck {@link Manifest}.
         *
         * @param truck The truck
         * @return The builder, for chaining
         */
        Builder addTruck(Truck truck);

        /**
         * Builds the {@link Manifest}.
         *
         * @return The built Item
         */
        Manifest build();

        /**
         * Resets the state of this builder.
         *
         * @return The builder, for chaining
         */
        Builder reset();
    }
}
