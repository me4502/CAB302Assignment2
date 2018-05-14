package com.me4502.supermart.store;

public class StoreImpl implements Store {

    private static StoreImpl instance;

    public StoreImpl() {
        if (instance != null) {
            throw new IllegalStateException("This object has already been instantiated");
        }
        instance = this;
    }

    public static Store getInstance() {
        return StoreImpl.instance;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getCapital() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCapital(double capital) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFormattedCapital() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stock getInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInventory(Stock inventory) {
        throw new UnsupportedOperationException();
    }
}
