package com.me4502.supermart.truck;

import static junit.framework.TestCase.assertEquals;

import java.util.OptionalDouble;

import org.junit.Before;
import org.junit.Test;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.truck.RefrigeratedTruck;
import com.me4502.supermart.util.DummyClasses;

public class RefrigeratedTruckTest {
	// This can be used to represent classes without giving them a full implementation
	DummyClasses dummyClass = new DummyClasses();
	
	//Truck valid
	private int stockValidAmount = 100;
	private double stockValidTemp = 0;
	private double truckValidCost = 750 + Math.pow(Math.E, stockValidTemp/5);
	DummyClasses.StockDummy stockDummyValid = new DummyClasses.StockDummy(stockValidAmount, OptionalDouble.of(stockValidTemp));
	
	//Truck invalid upper bounds
	private int stockInvalidUpperAmount = 801;
	private double stockInvalidUpperTemp = 11;
	private double truckInvalidUpperCost = 750 + Math.pow(Math.E, stockInvalidUpperTemp/5);
	DummyClasses.StockDummy stockDummyUpperInvalid = new DummyClasses.StockDummy(stockInvalidUpperAmount, OptionalDouble.of(stockInvalidUpperTemp));	
	
	//Truck invalid lower bounds
	private int stockInvalidLowerAmount = 0;
	private double stockInvalidLowerTemp = -21;
	private double truckInvalidLowerCost = 750 + Math.pow(Math.E, stockInvalidLowerTemp/5);
	DummyClasses.StockDummy stockDummyLowerInvalid = new DummyClasses.StockDummy(stockInvalidLowerAmount, OptionalDouble.of(stockInvalidLowerTemp));	
		
	
	@Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    private RefrigeratedTruck.RefrigeratedBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
                .cost(truckValidCost)
                .cargo(stockDummyValid)
                .storageTemperature(stockValidTemp);
    }

    private RefrigeratedTruck buildTruck() {
        return truckBuilder().build();
    }

    @Test
    public void testValidBuild() {
    	buildTruck();
    }

    @Test(expected=IllegalStateException.class)
    public void testInvalidBuild() {
        RefrigeratedTruck.RefrigeratedBuilder builder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();
        builder.build();
    }

    //@Test(expected=DeliveryException.class)
    @Test(expected=IllegalStateException.class)
    public void testInvalidUpperStockBuild() {
        RefrigeratedTruck.RefrigeratedBuilder builder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
        		.cost(truckInvalidUpperCost)
                .cargo(stockDummyUpperInvalid)
                .storageTemperature(stockInvalidUpperTemp);
        builder.build();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testInvalidLowerStockBuild() {
        RefrigeratedTruck.RefrigeratedBuilder builder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder()
        		.cost(truckInvalidLowerCost)
                .cargo(stockDummyLowerInvalid)
                .storageTemperature(stockInvalidLowerTemp);
        builder.build();
    }
    
    
    @Test
    public void testCorrectBuild() {
        RefrigeratedTruck RefrigeratedTruck = buildTruck();
        assertEquals(truckValidCost, RefrigeratedTruck.getCost());
        assertEquals(stockDummyValid, RefrigeratedTruck.getCargo());
        assertEquals(stockValidTemp, RefrigeratedTruck.getStorageTemperature());
    }

    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        RefrigeratedTruck.RefrigeratedBuilder builder = truckBuilder().reset();
        builder.build();
    }
}
