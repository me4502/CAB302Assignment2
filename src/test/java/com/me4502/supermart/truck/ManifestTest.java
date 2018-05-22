package com.me4502.supermart.truck;

import static com.me4502.supermart.SuperMartApplication.getInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ManifestTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }

    private Stock getStock() {
        Stock stock = mock(Stock.class);
        when(stock.getStockedItemQuantities()).thenReturn(ImmutableSet.of(ImmutablePair.of(mock(Item.class), 1)));
        when(stock.getStockedItems()).thenReturn(ImmutableSet.of(mock(Item.class)));
        when(stock.getTotalAmount()).thenReturn(1);
        return stock;
    }

    private OrdinaryTruck getOrdinaryTruck() {
        Stock stock = getStock();
        OrdinaryTruck ordinaryTruck = mock(OrdinaryTruck.class);
        when(ordinaryTruck.getCargo()).thenReturn(stock);
        return ordinaryTruck;
    }

    private RefrigeratedTruck getRefrigeratedTruck() {
        Stock stock = getStock();
        RefrigeratedTruck RefrigeratedTruck = mock(RefrigeratedTruck.class);
        when(RefrigeratedTruck.getCargo()).thenReturn(stock);
        return RefrigeratedTruck;
    }

    // Make a list of them
    private List<Truck> getTruckList() {
        return Lists.newArrayList(
                getOrdinaryTruck(),
                getRefrigeratedTruck()
        );
    }

    private Manifest.Builder validManifestBuilder() {
        Manifest.Builder builder = getInstance().getManifestBuilder();
        for (Truck truck : getTruckList()) {
            builder.addTruck(truck);
        }
        return builder;
    }

    private Manifest buildValidManifest() {
        return validManifestBuilder().build();
    }

    @Test
    public void canBuildValid() {
        buildValidManifest();
    }

    @Test
    public void testEmptySucceeds() {
        getInstance().getManifestBuilder().build();
    }

    //Allow an empty manifest
    @Test
    public void testResetWorks() {
        Manifest.Builder builder = validManifestBuilder().reset();
        assertTrue(builder.build().getTrucks().isEmpty());
    }

    @Test
    public void noEmptyTrucks() {
        Manifest manifest = buildValidManifest();
        for (Truck truck : manifest.getTrucks()) {
            assertTrue(truck.getCargo().getTotalAmount() > 0);
        }
    }

    @Test
    public void testSetsContainSameItems() {
        Manifest manifest = buildValidManifest();
        assertEquals(manifest.getTrucks().size(), getTruckList().size());
        for (Truck truck : manifest.getTrucks()) {
            assertTrue(getTruckList().contains(truck));
        }
    }
}
