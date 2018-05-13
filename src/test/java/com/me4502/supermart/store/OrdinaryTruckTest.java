package com.me4502.supermart.store;

import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.DummyClasses.StockDummy;
import com.me4502.supermart.trucks.OrdinaryTruck;

public class OrdinaryTruckTest {
	
	DummyClasses dummyClass = new DummyClasses();
	DummyClasses.StockDummy stockDummyValid = dummyClass.new StockDummy(1); 
	DummyClasses.StockDummy stockDummyInvalid = dummyClass.new StockDummy(801);
	
	@Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    private OrdinaryTruck.OrdinaryBuilder truckBuilder() {
        return SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
                .cost(123.4)
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
    
    @Test(expected=IllegalStateException.class)
    public void testInvalidStockBuild() {
        OrdinaryTruck.OrdinaryBuilder builder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder()
        		.cost(123.4)
                .cargo(stockDummyInvalid);
        builder.build();
    }
    
    @Test
    public void testCorrectBuild() {
        OrdinaryTruck ordinaryTruck = buildTruck();
        // Hamcrest import?
        assertThat(123.4, is(equalTo(ordinaryTruck.getCost())));
        assertThat(stockDummyValid, is(equalTo(ordinaryTruck.getCargo()));
    }

    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        OrdinaryTruck.OrdinaryBuilder builder = truckBuilder().reset();
        builder.build();
    }
}
