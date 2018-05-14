package com.me4502.supermart.truck;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.OptionalDouble;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.truck.RefrigeratedTruck;

public class RefrigeratedTruckTest {
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
	double validStorageTemp = 1;
	double validCost = getCost(validStorageTemp);
	Stock validStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(validStorageTemp), 100)));
	
	// Return builder for valid item
	private RefrigeratedTruck.RefrigeratedBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
                .cost(validCost)
                .cargo(validStock)
                .storageTemperature(validStorageTemp);
    }
	
	// Build the valid item
    private RefrigeratedTruck buildTruck() {
        return truckBuilder().build();
    }
    
    // Return a built truck with parameters
    private RefrigeratedTruck buildUniqueTruck(double cost, Stock cargo, double storageTemperature) {
        return SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
                .cost(cost)
                .cargo(cargo)
                .storageTemperature(storageTemperature)
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
	    double invalidStorageTemp = -20;
		double invalidCost = getCost(validStorageTemp);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(validStorageTemp), 1)));
		// Attempt to build
    	buildUniqueTruck(invalidCost, invalidStock, invalidStorageTemp);
    }

    // Test values sitting below the lower boundaries
    @Test(expected=IllegalStateException.class)
    public void testBelowLowerThresholds() {
    	// Generate bad parameters
	    double invalidStorageTemp = -21;
		double invalidCost = getCost(validStorageTemp);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(validStorageTemp), 0)));
		// Attempt to build
    	buildUniqueTruck(invalidCost, invalidStock, invalidStorageTemp);
    }
    
    // Test values sitting on the upper boundaries
    @Test
    public void testOnUpperThresholds() {
    	// Generate bad parameters
	    double invalidStorageTemp = 10;
		double invalidCost = getCost(validStorageTemp);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(validStorageTemp), 800)));
		// Attempt to build
    	buildUniqueTruck(invalidCost, invalidStock, invalidStorageTemp);
    }
    
    // Test values sitting above the upper boundaries
    @Test(expected=IllegalStateException.class)
    public void testAboveUpperThresholds() {
    	// Generate bad parameters
	    double invalidStorageTemp = 11;
		double invalidCost = getCost(validStorageTemp);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(getItem(validStorageTemp), 801)));
		// Attempt to build
    	buildUniqueTruck(invalidCost, invalidStock, invalidStorageTemp);
    }
}
