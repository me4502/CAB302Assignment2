package com.me4502.supermart.truck;

import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

import java.util.OptionalDouble;

/**
 * Implementation for {@link RefrigeratedTruck}.
 *
 * @author Madeline Miller
 */
public class RefrigeratedTruckImpl extends AbstractTruck implements RefrigeratedTruck {

    private static final int CAPACITY = 800;
    private static final double MIN_TEMP = -20;
    private static final double MAX_TEMP = 10;

    private RefrigeratedTruckImpl(Stock cargo) {
        super(cargo);
    }

    @Override
    public String getType() {
        return "refrigerated";
    }

    @Override
    public double getStorageTemperature() {
        return getCargo().getStockedItems()
                .stream()
                .map(Item::getIdealTemperature)
                .filter(OptionalDouble::isPresent)
                .mapToDouble(OptionalDouble::getAsDouble)
                .filter(temp -> temp >= MIN_TEMP)
                .filter(temp -> temp <= MAX_TEMP)
                .min().orElse(MAX_TEMP);
    }

    @Override
    public double getCost() {
        return 900 + (200 * Math.pow(0.7, getStorageTemperature() / 5));
    }

    @Override
    public int getCargoCapacity() {
        return CAPACITY;
    }

    public static class RefrigeratedTruckBuilderImpl implements RefrigeratedTruck.RefrigeratedBuilder {
        private Stock cargo;

        @Override
        public RefrigeratedBuilder cargo(Stock cargo) {
            if (cargo.getTotalAmount() > CAPACITY) {
                throw new IllegalStateException("Cargo exceeds capacity");
            }
            if (!cargo.getStockedItems().stream().allMatch(Item::isTemperatureControlled)) {
                throw new IllegalStateException("Cargo can only contain temperature controlled items");
            }
            this.cargo = cargo;
            return this;
        }

        @Override
        public RefrigeratedTruck build() {
            if (cargo == null) {
                throw new IllegalStateException("RefrigeratedTruck requires cargo");
            }
            return new RefrigeratedTruckImpl(cargo);
        }

        @Override
        public RefrigeratedBuilder reset() {
            this.cargo = null;
            return this;
        }
    }
}
