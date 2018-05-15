package com.me4502.supermart.truck;

import com.me4502.supermart.store.Stock;

/**
 * Shared code for the implementations of {@link Truck}.
 *
 * @author Madeline Miller
 */
public abstract class AbstractTruck implements Truck {
    private Stock cargo;

    AbstractTruck(Stock cargo) {
        this.cargo = cargo;
    }

    @Override
    public Stock getCargo() {
        return this.cargo;
    }

}
