package com.me4502.supermart.truck;

/**
 * An interface for Ordinary Truck. This class is immutable.
 *
 * @author Liam Dale
 */
public interface OrdinaryTruck extends Truck {

	/**
    * Builder class to build {@link OrdinaryTruck} instances.
    *
    * @author Liam Dale
    */
	interface OrdinaryBuilder extends Truck.Builder<OrdinaryTruck, OrdinaryBuilder> {

    }
}
