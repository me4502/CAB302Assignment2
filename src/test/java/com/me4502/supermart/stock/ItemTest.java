package com.me4502.supermart.stock;

import static junit.framework.TestCase.assertEquals;

import com.me4502.supermart.SuperMartApplication;
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

    private Item buildTestItem() {
        Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder();
        builder.name("Test Item");
        builder.idealTemperature(1.0);
        builder.manufacturingCost(1.0);
        builder.reorderAmount(1);
        builder.reorderPoint(1);
        builder.sellPrice(1.0);
        return builder.build();
    }

    private Item buildTestItem2() {
        Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder();
        builder.name("Test Item 2");
        builder.idealTemperature(1.7);
        builder.manufacturingCost(-9);
        builder.reorderAmount(-19);
        builder.reorderPoint(84);
        builder.sellPrice(0);
        return builder.build();
    }

    @Test
    public void testItemCanBeBuilt() {
        buildTestItem();
    }

    @Test
    public void testInvalidItemCantBeBuilt() {
        Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder();
        builder.build();
    }

    @Test
    public void testItemBuiltCorrectly() {
        Item item = buildTestItem();
        assertEquals("Test Item", item.getName());
        assertEquals(1.0, item.getIdealTemperature());
        assertEquals(1.0, item.getManufacturingCost());
        assertEquals(1, item.getReorderAmount());
        assertEquals(1, item.getReorderPoint());
        assertEquals(1.0, item.getSellPrice());
    }

    @Test
    public void testItem2BuiltCorrectly() {
        Item item = buildTestItem2();
        assertEquals("Test Item 2", item.getName());
        assertEquals(1.7, item.getIdealTemperature());
        assertEquals(-9, item.getManufacturingCost());
        assertEquals(-19, item.getReorderAmount());
        assertEquals(84, item.getReorderPoint());
        assertEquals(0, item.getSellPrice());
    }
}
