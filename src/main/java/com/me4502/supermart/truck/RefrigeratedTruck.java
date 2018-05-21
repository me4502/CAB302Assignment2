package com.me4502.supermart.truck;

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

    /**
     * Builder class to {@link RefrigeratedTruck} instances.
     *
     * @author Liam Dale
     */
    interface RefrigeratedBuilder extends Truck.Builder<RefrigeratedTruck, RefrigeratedBuilder> {

    }
}
