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
	
	
	// Return an item -- only need to test for item temperature
	private Item getItem(double storageTemperature) {
		Item item = mock(Item.class);
		when(item.getIdealTemperature()).thenReturn(OptionalDouble.of(storageTemperature));
		when(item.isTemperatureControlled()).thenReturn((storageTemperature <= MAX_TEMPERATURE) ? true : false);
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
	int validAmount = 100;
	double validStorageTemp = 0;
	Item validItem = getItem(validStorageTemp);
	double validCost = 1100;
	Stock validStock = getStock(ImmutableSet.of(ImmutablePair.of(validItem, validAmount)),
			ImmutableSet.of(validItem),
			validAmount);
	
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
        assertEquals("Refrigerated", RefrigeratedTruck.getType());
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
    	// Generate boundary parameters
		Item boundaryItem = getItem(MIN_TEMPERATURE);
		Stock boundaryStock = getStock(ImmutableSet.of(ImmutablePair.of(boundaryItem, MIN_CAPACITY)),
				ImmutableSet.of(boundaryItem),
				MIN_CAPACITY);
		// Attempt to build
    	buildUniqueTruck(boundaryStock);
    }
    
    // Test values sitting on the upper boundaries
    @Test
    public void testOnUpperThresholds() {
    	// Generate boundary parameters
		Item boundaryItem = getItem(MAX_TEMPERATURE);
		Stock boundaryStock = getStock(ImmutableSet.of(ImmutablePair.of(boundaryItem, MAX_CAPACITY)),
				ImmutableSet.of(boundaryItem),
				MAX_CAPACITY);
		// Attempt to build
    	buildUniqueTruck(boundaryStock);
    }
    
    // Test values sitting above the upper boundaries
    @Test(expected=IllegalStateException.class)
    public void testAboveUpperThresholds() {
    	// Generate bad parameters
		Item invalidItem = getItem(MAX_TEMPERATURE + 1);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(invalidItem, MAX_CAPACITY + 1)),
				ImmutableSet.of(invalidItem),
				MAX_CAPACITY + 1);
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
