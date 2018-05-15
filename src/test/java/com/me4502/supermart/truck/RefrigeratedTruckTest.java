package com.me4502.supermart.truck;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.OptionalDouble;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.truck.RefrigeratedTruck;

public class RefrigeratedTruckTest {
	private final int MIN_CAPACITY = 1;
	private final int MAX_CAPACITY = 800;
	private final int MIN_TEMPERATURE = -20;
	private final int MAX_TEMPERATURE = 10;
	
	// Get the cost of a truck for a certain temperature
	private double getCost(double storageTemperature) {
		return (900 + 20*Math.pow(Math.E, storageTemperature/5));
	}
	
	// Return an item -- only need to test for item temperature
	private Item getItem(double storageTemperature) {
		Item item = mock(Item.class);
		when(item.getIdealTemperature()).thenReturn(OptionalDouble.of(storageTemperature));
		return item;
	}
	
	// Return a stock item -- only need to test for lowest item temperature and total amount
	private Stock getStock(ImmutableSet<ImmutablePair<Item, Integer>> stockSetAmounts) {
		Stock stock = mock(Stock.class);
		when(stock.getStockedItemQuantities()).thenReturn(stockSetAmounts);
		return stock;
	}
	
	// Valid build parameters
	double validStorageTemp = 0;
	double validCost = getCost(validStorageTemp);
	Stock validStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(validStorageTemp), 100)));
	
	// Return builder for valid item
	private RefrigeratedTruck.RefrigeratedBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
                .cargo(validStock);
    }
	
	// Build the valid item
    private RefrigeratedTruck buildTruck() {
        return truckBuilder().build();
    }
    
    // Return a built truck with parameters
    private RefrigeratedTruck buildUniqueTruck(Stock cargo) {
        return SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
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
        RefrigeratedTruck RefrigeratedTruck = buildTruck();
        assertEquals("refrigerated", RefrigeratedTruck.getType());
        assertEquals(MAX_CAPACITY, RefrigeratedTruck.getCargoCapacity());
        assertEquals(validCost, RefrigeratedTruck.getCost());
        assertEquals(validStock, RefrigeratedTruck.getCargo());
        assertEquals(validStorageTemp, RefrigeratedTruck.getStorageTemperature());
    }

    // 
    @Test(expected=IllegalStateException.class)
    public void testInvalidEmptyBuild() {
        RefrigeratedTruck.RefrigeratedBuilder builder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();
        builder.build();
    }
    
    //
    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        RefrigeratedTruck.RefrigeratedBuilder builder = truckBuilder().reset();
        builder.build();
    }

	// Test values sitting on the lower boundaries
	@Test
    public void testOnLowerThresholds() {
    	// Generate bad parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(MIN_TEMPERATURE), MIN_CAPACITY)));
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }

    // Test values sitting below the lower boundaries
    @Test(expected=IllegalStateException.class)
    public void testBelowLowerThresholds() {
    	// Generate bad parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(MIN_TEMPERATURE - 1), MIN_CAPACITY - 1)));
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    // Test values sitting on the upper boundaries
    @Test
    public void testOnUpperThresholds() {
    	// Generate bad parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(MAX_TEMPERATURE), MAX_CAPACITY)));
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    // Test values sitting above the upper boundaries
    @Test(expected=IllegalStateException.class)
    public void testAboveUpperThresholds() {
    	// Generate bad parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(MAX_TEMPERATURE + 1), MAX_CAPACITY + 1)));
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testEmptyTruck() {
		// Attempt to build
    	buildUniqueTruck(null);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testEmptyStockTruck() {
    	// Generate bad parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(null, null)));
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }
}
