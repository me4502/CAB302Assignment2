package util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.me4502.supermart.store.Stock;
import com.me4502.supermart.truck.RefrigeratedTruck;

public class mockClasses {
	public static RefrigeratedTruck getRefrigeratedTruck(double cost, Stock cargo, double storageTemperature) {
        RefrigeratedTruck mockTruck = mock(RefrigeratedTruck.class);
        when(mockTruck.getType()).thenReturn("refrigerated");
        when(mockTruck.getCargoCapacity()).thenReturn(800);
        when(mockTruck.getCost()).thenReturn(cost);
        when(mockTruck.getCargo()).thenReturn(cargo);
        when(mockTruck.getStorageTemperature()).thenReturn(storageTemperature);
        return mockTruck;
    }

}
