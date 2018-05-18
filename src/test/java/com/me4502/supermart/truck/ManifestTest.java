package com.me4502.supermart.truck;

import static com.me4502.supermart.SuperMartApplication.getInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.me4502.supermart.SuperMartApplication;

public class ManifestTest {
	
    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }

    // Create some trucks
    OrdinaryTruck ordinaryTruck = mock(OrdinaryTruck.class);
    RefrigeratedTruck refrigeratedTruck = mock(RefrigeratedTruck.class);
    // Make a list of them
    private List<Truck> getTruckList() {
    	return Lists.newArrayList(
    		ordinaryTruck,
    		refrigeratedTruck
    	);
    }
    
    private Manifest.Builder validManifestBuilder() {
        Manifest.Builder builder = getInstance().getManifestBuilder();
        for(Truck truck : getTruckList()) {
            builder.addTruck(truck);
        }
        return builder;
    }

    public Manifest buildValidManifest() {
    	return validManifestBuilder().build(); 
    }
    
    @Test
    public void canBuildValid() {
    	buildValidManifest();
    }
    
    @Test
    public void testEmptySucceeds() {
        getInstance().getManifestBuilder().build();
    }
    
    //Allow an empty manifest
    @Test
    public void testResetWorks() {
        Manifest.Builder builder = validManifestBuilder().reset();
        assertTrue(builder.build().getTrucks().isEmpty());
    }
    
    @Test
    public void noEmptyTrucks() {
        Manifest manifest = buildValidManifest();
        for (Truck truck: manifest.getTrucks()) {
            assertTrue(truck.getCargo().getTotalAmount() > 0);
        }
    }
    
    @Test
    public void testSetsContainSameItems() {
        Manifest manifest = buildValidManifest();
        assertEquals(manifest.getTrucks().size(), getTruckList().size());
        for (Truck truck: manifest.getTrucks()) {
            assertTrue(getTruckList().contains(truck));
        }
    }	
}
