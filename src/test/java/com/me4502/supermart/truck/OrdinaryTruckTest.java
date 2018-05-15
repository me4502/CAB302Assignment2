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
	private final int MIN_CAPACITY = 1;
	private final int MAX_CAPACITY = 1000;
	
	// Get the cost of a truck for a certain temperature
	private double getCost(int quantity) {
		return (750 + 0.25 * quantity);
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
	int validQuantity = 100;
	double validCost = getCost(validQuantity);
	Stock validStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), validQuantity)), 
			ImmutableSet.of(mock(Item.class)), 
			validQuantity);
	
	// Return builder for valid item
	private OrdinaryTruck.OrdinaryBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cargo(validStock);
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
        assertEquals("ordinary", OrdinaryTruck.getType());
        assertEquals(MAX_CAPACITY, OrdinaryTruck.getCargoCapacity());
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
		Stock boundaryStock = getStock(
				ImmutableSet.of(ImmutablePair.of(mock(Item.class), MIN_CAPACITY)),
				ImmutableSet.of(mock(Item.class)),
				MIN_CAPACITY);
		// Attempt to build
    	buildUniqueTruck(boundaryStock);
    }

    // Test values sitting below the lower boundaries
    @Test(expected=IllegalStateException.class)
    public void testBelowLowerThresholds() {
    	// Generate parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), MIN_CAPACITY - 1)),
				ImmutableSet.of(mock(Item.class)),
				MIN_CAPACITY - 1);
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    // Test values sitting on the upper boundaries
    @Test
    public void testOnUpperThresholds() {
    	// Generate parameters
		Stock boundaryStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), MAX_CAPACITY)),
				ImmutableSet.of(mock(Item.class)),
				MAX_CAPACITY);
		// Attempt to build
    	buildUniqueTruck(boundaryStock);
    }
    
    // Test values sitting above the upper boundaries
    @Test(expected=IllegalStateException.class)
    public void testAboveUpperThresholds() {
    	// Generate parameters
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(mock(Item.class), MAX_CAPACITY + 1)),
				ImmutableSet.of(mock(Item.class)),
				MAX_CAPACITY + 1);
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    // Test hiring a refrigerated truck with no cold goods
    @Test(expected=IllegalStateException.class)
    public void testNoColdGoods() {
    	// Generate parameters
    	Item invalidItem = mock(Item.class);
    	when(invalidItem.getIdealTemperature()).thenReturn(OptionalDouble.of(0));
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(invalidItem, validQuantity)),
				ImmutableSet.of(invalidItem),
				validQuantity);
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
		Stock invalidStock = getStock(ImmutableSet.of(ImmutablePair.of(null, null)),
				ImmutableSet.of(null),
				0);
		// Attempt to build
    	buildUniqueTruck(invalidStock);
    }
    
    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }
}
