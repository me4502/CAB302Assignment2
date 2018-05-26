package com.me4502.supermart.truck;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Optimises a manifest based on a requested cargo order.
 *
 * @author Madeline Miller
 */
public class ManifestOptimiser {

    private Stock cargo;

    /**
     * Creates a manifest optimiser from the given cargo.
     *
     * @param cargo The cargo
     */
    public ManifestOptimiser(Stock cargo) {
        this.cargo = cargo;
    }

    /**
     * Generates an immutable set of {@link Truck}s for the cargo.
     *
     * @return The truck set
     */
    public Manifest getManifest() {
        // Setup the builders
        Manifest.Builder manifestBuilder = SuperMartApplication.getInstance().getManifestBuilder();
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
        RefrigeratedTruck.RefrigeratedBuilder refrigeratedBuilder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();
        OrdinaryTruck.OrdinaryBuilder ordinaryBuilder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();

        List<Item> coldItems = new ArrayList<>();
        List<Item> warmItems = new ArrayList<>();

        this.cargo.getStockedItemQuantities().forEach(pair -> {
            for (int i = 0; i < pair.getRight(); i++) {
                if (pair.getLeft().isTemperatureControlled()) {
                    coldItems.add(pair.getLeft());
                } else {
                    warmItems.add(pair.getLeft());
                }
            }
        });

        coldItems.sort(Comparator.comparingDouble(item -> item.getIdealTemperature().getAsDouble()));

        while(!coldItems.isEmpty()) {
            stockBuilder.reset();
            refrigeratedBuilder.reset();
            int size = 0;
            while (size < RefrigeratedTruck.getCapacity() && !coldItems.isEmpty()) {
                size ++;
                stockBuilder.addStockedItem(coldItems.get(0), 1);
                coldItems.remove(0);
            }
            while (size < RefrigeratedTruck.getCapacity() && !warmItems.isEmpty()) {
                size ++;
                stockBuilder.addStockedItem(warmItems.get(0), 1);
                warmItems.remove(0);
            }
            manifestBuilder.addTruck(refrigeratedBuilder.cargo(stockBuilder.build()).build());
        }

        while (!warmItems.isEmpty()) {
            stockBuilder.reset();
            ordinaryBuilder.reset();
            int size = 0;
            while (size < OrdinaryTruck.getCapacity() && !warmItems.isEmpty()) {
                size ++;
                stockBuilder.addStockedItem(warmItems.get(0), 1);
                warmItems.remove(0);
            }
            manifestBuilder.addTruck(ordinaryBuilder.cargo(stockBuilder.build()).build());
        }

        return manifestBuilder.build();
    }
}
