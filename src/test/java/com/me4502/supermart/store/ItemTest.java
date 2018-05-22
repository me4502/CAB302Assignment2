package com.me4502.supermart.store;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import com.me4502.supermart.SuperMartApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests functionality of the Item class.
 *
 * @author Madeline Miller
 */
public class ItemTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }

    private Item.Builder testItemBuilder() {
        return SuperMartApplication.getInstance().getItemBuilder()
                .name("Test Item")
                .idealTemperature(1.0)
                .manufacturingCost(1.0)
                .reorderAmount(1)
                .reorderPoint(1)
                .sellPrice(1.0);
    }

    private Item buildTestItem() {
        return testItemBuilder().build();
    }

    private Item buildTestItem2() {
        return SuperMartApplication.getInstance().getItemBuilder()
                .name("Test Item 2")
                .manufacturingCost(9)
                .reorderAmount(19)
                .reorderPoint(84)
                .sellPrice(0)
                .build();
    }

    @Test
    public void testItemCanBeBuilt() {
        buildTestItem();
    }

    @Test
    public void testItem2CanBeBuilt() {
        buildTestItem2();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidItemCantBeBuilt() {
        Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder();
        builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidManufacturingCost() {
        // Can't have a negative manufacturing cost
        SuperMartApplication.getInstance().getItemBuilder()
                .manufacturingCost(-0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSellPrice() {
        // Can't have a negative sell price
        SuperMartApplication.getInstance().getItemBuilder()
                .sellPrice(-0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidReorderPoint() {
        // Can't have a negative reorder point
        SuperMartApplication.getInstance().getItemBuilder()
                .reorderPoint(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidReorderAmount() {
        // Can't have a negative reorder amount
        SuperMartApplication.getInstance().getItemBuilder()
                .reorderAmount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTemperature() {
        // Can't have a temperature below -20
        SuperMartApplication.getInstance().getItemBuilder()
                .idealTemperature(-21);
    }

    @Test
    public void testItemBuiltCorrectly() {
        Item item = buildTestItem();
        // Test if each of the attributes is correct
        assertEquals("Test Item", item.getName());
        assertTrue(item.getIdealTemperature().isPresent());
        assertEquals(1.0, item.getIdealTemperature().getAsDouble());
        assertEquals(1.0, item.getManufacturingCost());
        assertEquals(1, item.getReorderAmount());
        assertEquals(1, item.getReorderPoint());
        assertEquals(1.0, item.getSellPrice());
    }

    @Test
    public void testItem2BuiltCorrectly() {
        Item item = buildTestItem2();
        // Test if each of the attributes is correct
        assertEquals("Test Item 2", item.getName());
        assertFalse(item.getIdealTemperature().isPresent());
        assertEquals(9.0, item.getManufacturingCost());
        assertEquals(19, item.getReorderAmount());
        assertEquals(84, item.getReorderPoint());
        assertEquals(0.0, item.getSellPrice());
    }

    @Test
    public void testIsTemperatureControlled() {
        assertTrue(buildTestItem().isTemperatureControlled());
        assertFalse(buildTestItem2().isTemperatureControlled());
    }

    @Test(expected = IllegalStateException.class)
    public void testResetWorks() {
        Item.Builder builder = testItemBuilder().reset();
        builder.build();
    }
}
