package com.me4502.supermart.trucks;

/**
 * An interface for Ordinary Truck. This class is immutable.
 *
 * @author Liam Dale
 */
public interface OrdinaryTruck extends Truck {
	
	interface OrdinaryBuilder extends Truck.Builder {}
}
