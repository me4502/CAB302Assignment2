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

/**
 * Test functionality of the ManifestImpl class
 *
 * @author Liam Dale
 */
public class ManifestTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }

    // Mock stock
    private Stock getStock() {
        Stock stock = mock(Stock.class);
        when(stock.getStockedItemQuantities()).thenReturn(ImmutableSet.of(ImmutablePair.of(mock(Item.class), 1)));
        when(stock.getStockedItems()).thenReturn(ImmutableSet.of(mock(Item.class)));
        when(stock.getTotalAmount()).thenReturn(1);
        return stock;
    }

    // Mock ordinaryTruck
    private OrdinaryTruck getOrdinaryTruck() {
        Stock stock = getStock();
        OrdinaryTruck ordinaryTruck = mock(OrdinaryTruck.class);
        when(ordinaryTruck.getCargo()).thenReturn(stock);
        return ordinaryTruck;
    }

    // Mock refrigeratedTruck
    private RefrigeratedTruck getRefrigeratedTruck() {
        Stock stock = getStock();
        RefrigeratedTruck RefrigeratedTruck = mock(RefrigeratedTruck.class);
        when(RefrigeratedTruck.getCargo()).thenReturn(stock);
        return RefrigeratedTruck;
    }

    // List of trucks to fill manifest
    private Truck ordinaryTruck = getOrdinaryTruck();
    private Truck refrigeratedTruck = getRefrigeratedTruck();

    private List<Truck> getTruckList() {
        return Lists.newArrayList(
                this.ordinaryTruck,
                this.refrigeratedTruck
        );
    }

    // Builder for valid manifest
    private Manifest.Builder validManifestBuilder() {
        Manifest.Builder builder = getInstance().getManifestBuilder();
        for (Truck truck : getTruckList()) {
            builder.addTruck(truck);
        }
        return builder;
    }

    // Return valid manifest
    private Manifest buildValidManifest() {
        return validManifestBuilder().build();
    }

    // Test build works
    @Test
    public void canBuildValid() {
        buildValidManifest();
    }

    // Can build an empty manifest
    @Test
    public void testEmptySucceeds() {
        getInstance().getManifestBuilder().build();
    }

    // Test that manifest has been emptied on rest
    @Test
    public void testResetWorks() {
        Manifest.Builder builder = validManifestBuilder().reset();
        assertTrue(builder.build().getTrucks().isEmpty());
    }

    // Can't have an empty truck in the manifest
    @Test
    public void noEmptyTrucks() {
        Manifest manifest = buildValidManifest();
        for (Truck truck : manifest.getTrucks()) {
            assertTrue(truck.getCargo().getTotalAmount() > 0);
        }
    }

    // Test that the manifest has been built with same objects
    @Test
    public void testSetsContainSameItems() {
        Manifest manifest = buildValidManifest();
        assertEquals(manifest.getTrucks().size(), getTruckList().size());
        for (Truck truck : manifest.getTrucks()) {
            assertTrue(getTruckList().contains(truck));
        }
    }
}
