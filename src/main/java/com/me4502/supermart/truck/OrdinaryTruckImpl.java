package com.me4502.supermart.truck;

import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

/**
 * Implementation for {@link OrdinaryTruck}.
 *
 * @author Madeline Miller
 */
public class OrdinaryTruckImpl extends AbstractTruck implements OrdinaryTruck {

    private static final int CAPACITY = 1000;

    private OrdinaryTruckImpl(Stock cargo) {
        super(cargo);
    }

    @Override
    public String getType() {
        return "ordinary";
    }

    @Override
    public double getCost() {
        // 750 + 0.25q where q is the total quantity of items in the cargo.
        return 750 + (0.25 * getCargo().getTotalAmount());
    }

    @Override
    public int getCargoCapacity() {
        return CAPACITY;
    }

    public static class OrdinaryTruckBuilderImpl implements OrdinaryTruck.OrdinaryBuilder {
        private Stock cargo;

        @Override
        public OrdinaryBuilder cargo(Stock cargo) {
            if (cargo.getTotalAmount() > CAPACITY) {
                throw new IllegalStateException("Cargo exceeds capacity");
            }
            if (cargo.getStockedItems().stream().anyMatch(Item::isTemperatureControlled)) {
                throw new IllegalStateException("Cargo must not contain temperature controlled items");
            }
            this.cargo = cargo;
            return this;
        }

        @Override
        public OrdinaryTruck build() {
            if (cargo == null) {
                throw new IllegalStateException("OrdinaryTruck requires cargo");
            }
            return new OrdinaryTruckImpl(cargo);
        }

        @Override
        public OrdinaryBuilder reset() {
            this.cargo = null;
            return this;
        }
    }
}
