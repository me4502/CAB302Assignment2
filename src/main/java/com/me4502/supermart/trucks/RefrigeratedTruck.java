package com.me4502.supermart.trucks;

/**
 * An interface for Refrigerated Truck. This class is immutable.
 *
 * @author Liam Dale
 */
public interface RefrigeratedTruck extends Truck {
    /**
     * Gets the temperature which the truck must store items.
     *
     * @return The storage temperature
     */
	double getStorageTemperature();
	
	interface RefrigeratedBuilder extends Truck.Builder {
        /**
         * Sets the name of the {@link RefrigeratedTruck}.
         *
         * @param storageTemperature, temperature which the truck must store items at
         * @return The builder, for chaining
         */
		RefrigeratedBuilder storageTemperature(double storageTemperature);
	}
}
