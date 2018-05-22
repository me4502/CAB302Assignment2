package com.me4502.supermart.truck;

import com.google.common.collect.ImmutableSet;

/**
 * An interface for manifests 
 *
 * @author Liam Dale
 */
public interface Manifest {

    ImmutableSet<Truck> getTrucks();

    interface Builder {

        Builder addTruck(Truck truck);

        Manifest build();

        Builder reset();
    }
}
