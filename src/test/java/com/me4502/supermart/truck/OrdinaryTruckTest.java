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
import com.me4502.supermart.truck.OrdinaryTruck;

public class OrdinaryTruckTest {
	// Get the cost of a truck for a certain temperature
	private double getCost(int quantity) {
		return (750 + 0.25 * quantity);
	}
	
	// Return a stock item -- only need to test for lowest item temperature and total amount
	private Stock getStock(ImmutableSet<ImmutablePair<Item, Integer>> stockSetAmounts) {
		Stock stock = mock(Stock.class);
		when(stock.getStockedItemQuantities()).thenReturn(stockSetAmounts);
		return stock;
	}
	
	// Valid build parameters
	int validQuantity = 100;
	double validCost = getCost(validQuantity);
	Stock validStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), validQuantity)));
	
	// Return builder for valid item
	private OrdinaryTruck.OrdinaryBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cost(validCost)
                .cargo(validStock);
    }
	
	// Build the valid item
    private OrdinaryTruck buildTruck() {
        return truckBuilder().build();
    }
    
    // Return a built truck with parameters
    private OrdinaryTruck buildUniqueTruck(double cost, Stock cargo) {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cost(cost)
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
        assertEquals(validCost, OrdinaryTruck.getCost());
        assertEquals(validStock, OrdinaryTruck.getCargo());
    }

    // 
    @Test(expected=IllegalStateException.class)
    public void testInvalidEmptyBuild() {
        OrdinaryTruck.OrdinaryBuilder builder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();
        builder.build();
    }
    
    //
    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        OrdinaryTruck.OrdinaryBuilder builder = truckBuilder().reset();
        builder.build();
    }

	// Test values sitting on the lower boundaries
	@Test
    public void testOnLowerThresholds() {
    	// Generate parameters
		int borderQuantity = 1;
		double borderCost = getCost(borderQuantity);
		Stock borderStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), borderQuantity)));
		// Attempt to build
    	buildUniqueTruck(borderCost, borderStock);
    }

    // Test values sitting below the lower boundaries
    @Test(expected=IllegalStateException.class)
    public void testBelowLowerThresholds() {
    	// Generate parameters
		int invalidQuantity = 0;
		double invalidCost = getCost(invalidQuantity);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), invalidQuantity)));
		// Attempt to build
    	buildUniqueTruck(invalidCost, invalidStock);
    }
    
    // Test values sitting on the upper boundaries
    @Test
    public void testOnUpperThresholds() {
    	// Generate parameters
		int borderQuantity = 1000;
		double borderCost = getCost(borderQuantity);
		Stock borderStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), borderQuantity)));
		// Attempt to build
    	buildUniqueTruck(borderCost, borderStock);
    }
    
    // Test values sitting above the upper boundaries
    @Test(expected=IllegalStateException.class)
    public void testAboveUpperThresholds() {
    	// Generate parameters
		int invalidQuantity = 1001;
		double invalidCost = getCost(invalidQuantity);
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), invalidQuantity)));
		// Attempt to build
    	buildUniqueTruck(invalidCost, invalidStock);
    }
    
    // Test values sitting above the upper boundaries
    @Test(expected=IllegalStateException.class)
    public void testNoColdGoods() {
    	// Generate parameters
    	Item invalidItem = mock(Item.class);
    	when(invalidItem.getIdealTemperature()).thenReturn(OptionalDouble.of(10));
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(invalidItem, validQuantity)));
		// Attempt to build
    	buildUniqueTruck(validCost, invalidStock);
    }
}
