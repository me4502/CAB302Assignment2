package com.me4502.supermart.truck;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * Test functionality of the OrdinaryTruckImpl class
 * 
 * @author Liam Dale
 *
 */
public class OrdinaryTruckTest {

    private final int MIN_CAPACITY = 1;
    private final int MAX_CAPACITY = 1000;

    private Item getItem(boolean isTemperatureControlled) {
        Item item = mock(Item.class);
        when(item.isTemperatureControlled()).thenReturn(isTemperatureControlled);
        return item;
    }

    // Return a stock item -- only need to test for lowest item temperature and total amount
    private Stock getStock(ImmutableSet<ImmutablePair<Item, Integer>> stockSetAmounts, ImmutableSet<Item> stockSet, int stockAmounts) {
        Stock stock = mock(Stock.class);
        when(stock.getStockedItemQuantities()).thenReturn(stockSetAmounts);
        when(stock.getStockedItems()).thenReturn(stockSet);
        when(stock.getTotalAmount()).thenReturn(stockAmounts);
        return stock;
    }

    // Valid build parameters
    private int validQuantity = 100;
    private double validCost = 775;
    private Stock validStock = getStock(
            ImmutableSet.of(ImmutablePair.of(getItem(false), this.validQuantity)),
            ImmutableSet.of(getItem(false)),
            this.validQuantity
    );

    // Return builder for valid item
    private OrdinaryTruck.OrdinaryBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cargo(this.validStock);
    }

    // Build the valid item
    private OrdinaryTruck buildTruck() {
        return truckBuilder().build();
    }

    // Return a built truck with parameters
    private OrdinaryTruck buildUniqueTruck(Stock cargo) {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cargo(cargo)
                .build();
    }

    // Get the supermart instance for testing
    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    // Check that the valid truck has build properly
    @Test
    public void testValidBuild() {
        OrdinaryTruck OrdinaryTruck = buildTruck();
        assertEquals("Ordinary", OrdinaryTruck.getType());
        assertEquals(this.MAX_CAPACITY, OrdinaryTruck.getCargoCapacity());
        assertEquals(this.validCost, OrdinaryTruck.getCost());
        assertEquals(this.validStock, OrdinaryTruck.getCargo());
    }

    // Test that can't build an empty truck
    @Test(expected = IllegalStateException.class)
    public void testInvalidEmptyBuild() {
        OrdinaryTruck.OrdinaryBuilder builder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();
        builder.build();
    }

    // Truck should be empty after reset,  test this fails
    @Test(expected = IllegalStateException.class)
    public void testResetWorks() {
        OrdinaryTruck.OrdinaryBuilder builder = truckBuilder().reset();
        builder.build();
    }

    // Test values sitting on the lower boundaries
    @Test
    public void testOnLowerThresholds() {
        // Generate parameters
        Stock boundaryStock = getStock(
                ImmutableSet.of(ImmutablePair.of(getItem(false), this.MIN_CAPACITY)),
                ImmutableSet.of(getItem(false)),
                this.MIN_CAPACITY);
        // Attempt to build
        buildUniqueTruck(boundaryStock);
    }

    // Test values sitting on the upper boundaries
    @Test
    public void testOnUpperThresholds() {
        // Generate parameters
        Stock boundaryStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(false), this.MAX_CAPACITY)),
                ImmutableSet.of(getItem(false)),
                this.MAX_CAPACITY);
        // Attempt to build
        buildUniqueTruck(boundaryStock);
    }

    // Test values sitting above the upper boundaries
    @Test(expected = IllegalStateException.class)
    public void testAboveUpperThresholds() {
        // Generate parameters
        Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(false), this.MAX_CAPACITY + 1)),
                ImmutableSet.of(getItem(false)),
                this.MAX_CAPACITY + 1);
        // Attempt to build
        buildUniqueTruck(invalidStock);
    }

    // Test hiring an ordinary truck with no dry goods
    @Test(expected = IllegalStateException.class)
    public void testNoDryGoods() {
        // Generate parameters
        Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(true), this.validQuantity)),
                ImmutableSet.of(getItem(true)),
                this.validQuantity);
        // Attempt to build
        buildUniqueTruck(invalidStock);
    }


    @Test
    public void testEmptyStockTruck() {
        // Generate bad parameters
        Stock invalidStock = getStock(ImmutableSet.of(),
                ImmutableSet.of(),
                0);
        // Attempt to build
        buildUniqueTruck(invalidStock);
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }
}
