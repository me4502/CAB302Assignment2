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

    private List<Truck> getTruckList() {
    	return Lists.newArrayList(
    		mock(OrdinaryTruck.class),
    		mock(RefrigeratedTruck.class)
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
    
    @Test(expected=IllegalStateException.class)
    public void testEmptyFails() {
        getInstance().getManifestBuilder().build();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        Manifest.Builder builder = validManifestBuilder().reset();
        assertTrue(builder.build().getTrucks().isEmpty());
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
