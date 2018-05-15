package com.me4502.supermart.store;

import com.google.common.collect.Lists;
import com.me4502.supermart.SuperMartApplication;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.OptionalDouble;

import static com.me4502.supermart.SuperMartApplication.getInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
    }

    private Item getItem1() {
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item 1");
        when(mockItem.getIdealTemperature()).thenReturn(OptionalDouble.of(1.0));
        when(mockItem.getSellPrice()).thenReturn(1.0);
        when(mockItem.getReorderAmount()).thenReturn(1);
        when(mockItem.getReorderPoint()).thenReturn(1);
        when(mockItem.getManufacturingCost()).thenReturn(1.0);
        return mockItem;
    }

    private Item getItem2() {
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item 2");
        when(mockItem.getIdealTemperature()).thenReturn(OptionalDouble.empty());
        when(mockItem.getSellPrice()).thenReturn(2.2);
        when(mockItem.getReorderAmount()).thenReturn(3);
        when(mockItem.getReorderPoint()).thenReturn(94);
        when(mockItem.getManufacturingCost()).thenReturn(32.5);
        return mockItem;
    }

    private List<Item> getItemList1() {
        return Lists.newArrayList(
                getItem1(),
                getItem2()
        );
    }

    private Stock.Builder testStockBuilder1() {
        Stock.Builder builder = getInstance().getStockBuilder();
        List<Item> list = getItemList1();
        for (int i = 0; i < list.size(); i++) {
            builder.addStockedItem(list.get(i), i + 1);
        }
        return builder;
    }

    @Test
    public void testBuildSucceeds() {
        testStockBuilder1().build();
    }

    @Test
    public void testEmptyBuildSucceeds() {
        getInstance().getStockBuilder().build();
    }

    @Test
    public void testBuildHasItems() {
        Stock stock = testStockBuilder1().build();
        for (ImmutablePair<Item, Integer> stockQuant : stock.getStockedItemQuantities()) {
            switch (stockQuant.getRight()) {
                case 1:
                    assertEquals(stockQuant.getLeft().getName(), getItem1().getName());
                    break;
                case 2:
                    assertEquals(stockQuant.getLeft().getName(), getItem2().getName());
                    break;
                default:
                    fail();
                    break;
            }
        }
    }

    @Test
    public void testEmptyBuildHasNoItems() {
        assertTrue(getInstance().getStockBuilder().build().getStockedItems().isEmpty());
    }

    @Test
    public void testSetsContainSameItems() {
        Stock stock = testStockBuilder1().build();
        assertEquals(stock.getStockedItems().size(), stock.getStockedItemQuantities().size());
        for (Item item : stock.getStockedItems()) {
            assertTrue(stock.getStockedItemQuantities().stream().map(Pair::getLeft).anyMatch(stockedItem -> stockedItem.equals(item)));
        }
    }

    @Test
    public void testGetAmount() {
        assertEquals(3, testStockBuilder1().build().getTotalAmount());
    }

    @Test(expected=IllegalStateException.class)
    public void testResetWorks() {
        Stock.Builder builder = testStockBuilder1().reset();
        assertTrue(builder.build().getStockedItems().isEmpty());
    }
}
