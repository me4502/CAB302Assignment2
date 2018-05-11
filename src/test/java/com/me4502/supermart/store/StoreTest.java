package com.me4502.supermart.store;

import com.me4502.supermart.SuperMartApplication;
import org.junit.Before;

public class StoreTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
        new StoreImpl();
    }


}
