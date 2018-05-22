package com.me4502.supermart.truck;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Pair<Item, Integer>> unplacedItems = cargo.getStockedItemQuantities()
                .stream()
                .map(pair -> new MutablePair<>(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toSet());
        List<Item> coldItems = new ArrayList<>();
        unplacedItems.stream()
                .filter(pair -> pair.getLeft().isTemperatureControlled())
                .sorted(Comparator.comparingDouble((Pair<Item, Integer> pair) -> pair.getLeft().getIdealTemperature().getAsDouble()).reversed())
                .forEach(pair -> {
                    for (int i = 0; i < pair.getRight(); i++) {
                        coldItems.add(pair.getLeft());
                    }
                });
        unplacedItems.removeIf(pair -> pair.getLeft().isTemperatureControlled());

        ImmutableSet.Builder<Truck> truckSetBuilder = new ImmutableSet.Builder<>();
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
        RefrigeratedTruck.RefrigeratedBuilder refrigeratedBuilder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();
        while(!coldItems.isEmpty()) {
            Stock stock = stockBuilder.build();
            try {
                Stock afterStock = stockBuilder.addStockedItem(coldItems.get(0), 1).build();
                refrigeratedBuilder.cargo(afterStock);
            } catch (IllegalArgumentException e) {
                // This means it's full
                stockBuilder.build();
            }
        }

        return SuperMartApplication.getInstance().getManifestBuilder().build();
    }
}
