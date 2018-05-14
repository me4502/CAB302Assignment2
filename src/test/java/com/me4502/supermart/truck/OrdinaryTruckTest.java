package com.me4502.supermart.truck;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import org.junit.Before;
/*
import org.junit.Rule;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
*/

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.truck.OrdinaryTruck;
import com.me4502.supermart.util.DummyClasses;

public class OrdinaryTruckTest {	
	DummyClasses dummyClass = new DummyClasses();
	
	//
	private int stockValidAmount = 1;
	private double truckValidCost = 750 + 0.25 * stockValidAmount;
	DummyClasses.StockDummy stockDummyValid = new DummyClasses.StockDummy(stockValidAmount);
	
	
	private int stockInvalidAmount = 1001;
	private double truckInvalidCost = 750 + 0.25 * stockInvalidAmount;
	DummyClasses.StockDummy stockDummyInvalid = new DummyClasses.StockDummy(stockValidAmount);	
	
	@Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    private OrdinaryTruck.OrdinaryBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cost(truckValidCost)
                .cargo(stockDummyValid);
    }

    private OrdinaryTruck buildTruck() {
        return truckBuilder().build();
    }

    @Test
    public void testValidBuild() {
    	buildTruck();
    }

    @Test(expected=IllegalStateException.class)
    public void testInvalidBuild() {
        OrdinaryTruck.OrdinaryBuilder builder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();
        builder.build();
    }

    //@Test(expected=DeliveryException.class)
    @Test(expected=IllegalStateException.class)
    public void testInvalidStockBuild() {
        OrdinaryTruck.OrdinaryBuilder builder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
        		.cost(truckInvalidCost)
                .cargo(stockDummyInvalid);
        builder.build();
    }

/*  
//    @RunWith(Theories.class)
//    public static class PrimeTest {
//    	
//    @Before
//	    public void setupApplication() {
//	        new SuperMartApplication();
//	    }
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();    
//    	
//    	
//        @Theory
//        public void isPrime(int candidate) {
//        	DummyClasses.StockDummy stockTester = dummyClass.new StockDummy(candidate); 
//        	OrdinaryTruck.OrdinaryBuilder builder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
//        			.cost(750 + 0.25 * candidate)
//        			.cargo(stockTester);
//        	builder.build();
//        	thrown.expect(IllegalStateException.class);
//        }
//        public static @DataPoints int[] candidates = {1, 2, 3, 4, 5};
//    }
*/
    
    
    @Test
    public void testCorrectBuild() {
        OrdinaryTruck ordinaryTruck = buildTruck();
        assertEquals(truckValidCost, ordinaryTruck.getCost());
        assertEquals(stockDummyValid, ordinaryTruck.getCargo());
    }

    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        OrdinaryTruck.OrdinaryBuilder builder = truckBuilder().reset();
        builder.build();
    }
}
