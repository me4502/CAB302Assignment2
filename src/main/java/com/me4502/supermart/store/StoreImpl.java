package com.me4502.supermart.store;

public class StoreImpl implements Store {

    private static StoreImpl instance;

    public StoreImpl(String name) {
        if (instance != null) {
            throw new IllegalStateException("This object has already been instantiated");
        }
        instance = this;
    }

    /**
     * Gets the current instance of this Singleton class.
     *
     * @return The instance
     */
    public static Store getInstance() {
        return StoreImpl.instance;
    }

    /**
     * Removes the current opened instance of this class.
     */
    public void close() {
        StoreImpl.instance = null;
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
