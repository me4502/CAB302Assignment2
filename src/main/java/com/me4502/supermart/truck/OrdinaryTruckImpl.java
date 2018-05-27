package com.me4502.supermart.truck;

import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

/**
 * Implementation for {@link OrdinaryTruck}.
 *
 * {@inheritDoc}
 *
 * @author Madeline Miller
 */
public class OrdinaryTruckImpl extends AbstractTruck implements OrdinaryTruck {

    private static final String TYPE = "Ordinary";

    /**
     * Creates an ordinary truck.
     *
     * @param cargo The cargo
     */
    private OrdinaryTruckImpl(Stock cargo) {
        super(cargo);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public double getCost() {
        // 750 + 0.25q where q is the total quantity of items in the cargo.
        return 750 + (0.25 * getCargo().getTotalAmount());
    }

    /**
     * {@inheritDoc}
     *
     * @author Madeline Miller
     */
    public static class OrdinaryTruckBuilderImpl implements OrdinaryTruck.OrdinaryBuilder {

        private Stock cargo;

        @Override
        public OrdinaryBuilder cargo(Stock cargo) {
            // Don't allow trucks that have too much cargo to be made
            if (cargo.getTotalAmount() > OrdinaryTruck.getCapacity()) {
                throw new IllegalStateException("Cargo exceeds capacity");
            }
            // Don't allow temperature-controlled items in a non-fridge truck
            if (cargo.getStockedItems().stream().anyMatch(Item::isTemperatureControlled)) {
                throw new IllegalStateException("Cargo must not contain temperature controlled items");
            }
            this.cargo = cargo;
            return this;
        }

        @Override
        public OrdinaryTruck build() {
            // Fail if the required properties haven't been set
            if (this.cargo == null) {
                throw new IllegalStateException("OrdinaryTruck requires cargo");
            }
            return new OrdinaryTruckImpl(this.cargo);
        }

        @Override
        public OrdinaryBuilder reset() {
            this.cargo = null;
            return this;
        }
    }
}
