package com.me4502.supermart.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.exception.DeliveryException;
import com.me4502.supermart.truck.Manifest;
import com.me4502.supermart.truck.Truck;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests functionality of the Store class.
 *
 * @author Madeline Miller
 */
public class StoreTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
        new StoreImpl("Test Store");
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
        ((StoreImpl) StoreImpl.getInstance()).close();
    }

    @Test
    public void testGetStore() {
        Store instance = StoreImpl.getInstance();
        assertNotNull(instance);
    }

    @Test(expected = IllegalStateException.class)
    public void testRecreateStoreFails() {
        new StoreImpl("Test Store");
    }

    @Test
    public void testRecreateStoreWorksAfterClose() {
        ((StoreImpl) StoreImpl.getInstance()).close();
        new StoreImpl("Test Store");
    }

    @Test
    public void testGetName() {
        assertEquals("Test Store", StoreImpl.getInstance().getName());
    }

    @Test
    public void testCapital() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(1.0);
        // Check with a delta of 0.01 due to dollars and cents
        assertEquals(1.0, instance.getCapital(), 0.01);
    }

    @Test
    public void testDefaultCapital() {
        Store instance = StoreImpl.getInstance();
        // Check with a delta of 0.01 due to dollars and cents
        assertEquals(100000, instance.getCapital(), 0.01);
    }

    @Test
    public void testDefaultCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        assertEquals("$100,000.00", instance.getFormattedCapital());
    }

    @Test
    public void testSimpleCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(100);
        assertEquals("$100.00", instance.getFormattedCapital());
    }

    @Test
    public void testCentsCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(0.17);
        assertEquals("$0.17", instance.getFormattedCapital());
    }

    @Test
    public void testCentsOverflowCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(0.4502);
        assertEquals("$0.45", instance.getFormattedCapital());
    }

    @Test
    public void testCentsOverflowCapitalRoundFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(0.505);
        assertEquals("$0.51", instance.getFormattedCapital());
    }

    @Test
    public void testNegativeCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(-10);
        assertEquals("-$10.00", instance.getFormattedCapital());
    }

    @Test
    public void testDefaultEmptyStock() {
        Store instance = StoreImpl.getInstance();
        assertTrue(instance.getInventory().getStockedItems().isEmpty());
    }

    @Test
    public void testSettingStock() {
        Store instance = StoreImpl.getInstance();
        Stock mockStock = mock(Stock.class);
        instance.setInventory(mockStock);
        // Simple reference equality check - it shouldn't change the object
        assertEquals(mockStock, instance.getInventory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullStockFails() {
        Store instance = StoreImpl.getInstance();
        instance.setInventory(null);
    }

    @Test
    public void testSettingStockIsCorrect() {
        Store instance = StoreImpl.getInstance();
        Stock mockStock = mock(Stock.class);
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test");
        when(mockStock.getStockedItems()).thenReturn(ImmutableSet.of(mockItem));
        instance.setInventory(mockStock);
        assertTrue(instance.getInventory().getStockedItems().stream()
                .anyMatch(item -> item.getName().equals("Test")));
    }

    @Test
    public void testAddingItemsWork() {
        Store instance = StoreImpl.getInstance();
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item");
        instance.addItem(mockItem);
        assertEquals("Test Item", instance.getItem("Test Item").map(Item::getName).orElse("Empty"));
    }

    @Test
    public void testAddingDuplicateItemsDontAdd() {
        Store instance = StoreImpl.getInstance();
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item");
        instance.addItem(mockItem);
        instance.addItem(mockItem);
        assertEquals(1, instance.getItems().size());
    }

    @Test
    public void testGettingOtherItemIsEmpty() {
        Store instance = StoreImpl.getInstance();
        assertFalse(instance.getItem("Empty").isPresent());
    }

    @Test
    public void testGetItemsSet() {
        Store instance = StoreImpl.getInstance();
        assertEquals(0, instance.getItems().size());
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item");
        instance.addItem(mockItem);
        assertEquals(1, instance.getItems().size());
    }

    @Test
    public void testDefaultEmptyManifest() {
        Store instance = StoreImpl.getInstance();
        assertNotNull(instance.getManifest());
        assertTrue(instance.getManifest().getTrucks().isEmpty());
    }

    @Test
    public void testSettingManifest() {
        Store instance = StoreImpl.getInstance();

        Item mockitem = mock(Item.class);
        when(mockitem.getName()).thenReturn("Test");
        instance.addItem(mockitem);

        Stock mockStockTruck = mock(Stock.class);
        when(mockStockTruck.getStockedItemQuantities()).thenReturn(ImmutableSet.of(new ImmutablePair<>(mockitem, 1)));

        Stock mockStockStore = mock(Stock.class);
        when(mockStockStore.getStockedItemQuantities()).thenReturn(ImmutableSet.of(new ImmutablePair<>(mockitem, 0)));
        instance.setInventory(mockStockStore);

        Truck mockTruck = mock(Truck.class);
        when(mockTruck.getCost()).thenReturn(1000.0);
        when(mockTruck.getCargo()).thenReturn(mockStockTruck);

        Manifest mockManifest = mock(Manifest.class);
        when(mockManifest.getTrucks()).thenReturn(ImmutableSet.of(mockTruck));

        instance.setCapital(1000.0);
        try {
            instance.setManifest(mockManifest, true);
        } catch (DeliveryException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(mockManifest, instance.getManifest());
        assertEquals(0.0, instance.getCapital(), 0.001);
        assertEquals(1, instance.getInventory().getItemQuantity(mockitem).orElse(0));
    }

    @Test(expected = DeliveryException.class)
    public void testSettingManifestInvalidItem() throws DeliveryException {
        Store instance = StoreImpl.getInstance();

        Item mockitem = mock(Item.class);
        when(mockitem.getName()).thenReturn("Test");

        Stock mockStockTruck = mock(Stock.class);
        when(mockStockTruck.getStockedItemQuantities()).thenReturn(ImmutableSet.of(new ImmutablePair<>(mockitem, 1)));

        Stock mockStockStore = mock(Stock.class);
        when(mockStockStore.getStockedItemQuantities()).thenReturn(ImmutableSet.of(new ImmutablePair<>(mockitem, 0)));
        instance.setInventory(mockStockStore);

        Truck mockTruck = mock(Truck.class);
        when(mockTruck.getCost()).thenReturn(1000.0);
        when(mockTruck.getCargo()).thenReturn(mockStockTruck);

        Manifest mockManifest = mock(Manifest.class);
        when(mockManifest.getTrucks()).thenReturn(ImmutableSet.of(mockTruck));

        instance.setCapital(1000.0);
        instance.setManifest(mockManifest, true);
    }

    @Test
    public void testSettingManifestNoUpdate() {
        Store instance = StoreImpl.getInstance();

        Item mockitem = mock(Item.class);
        when(mockitem.getName()).thenReturn("Test");
        instance.addItem(mockitem);

        Stock mockStockTruck = mock(Stock.class);
        when(mockStockTruck.getStockedItemQuantities()).thenReturn(ImmutableSet.of(new ImmutablePair<>(mockitem, 1)));

        Stock mockStockStore = mock(Stock.class);
        when(mockStockStore.getStockedItemQuantities()).thenReturn(ImmutableSet.of(new ImmutablePair<>(mockitem, 0)));
        instance.setInventory(mockStockStore);

        Truck mockTruck = mock(Truck.class);
        when(mockTruck.getCost()).thenReturn(1000.0);
        when(mockTruck.getCargo()).thenReturn(mockStockTruck);

        Manifest mockManifest = mock(Manifest.class);
        when(mockManifest.getTrucks()).thenReturn(ImmutableSet.of(mockTruck));

        instance.setCapital(1000.0);
        try {
            instance.setManifest(mockManifest, false);
        } catch (DeliveryException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(mockManifest, instance.getManifest());
        assertEquals(1000.0, instance.getCapital(), 0.001);
        assertEquals(mockStockStore, instance.getInventory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullManifestFails() {
        Store instance = StoreImpl.getInstance();
        try {
            instance.setManifest(null, false);
        } catch (DeliveryException e) {
            e.printStackTrace();
            fail();
        }
    }
}
