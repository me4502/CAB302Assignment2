package com.me4502.supermart.store;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        throw new NotImplementedException();
    }

    @Override
    public double getCapital() {
        throw new NotImplementedException();
    }

    @Override
    public Stock getInventory() {
        throw new NotImplementedException();
    }
}
