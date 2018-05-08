package com.me4502.supermart.store;

import com.google.common.collect.Lists;
import com.me4502.supermart.SuperMartApplication;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static com.me4502.supermart.SuperMartApplication.getInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StockTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
    }

    private Item getItem1() {
        return getInstance().getItemBuilder()
                .sellPrice(1.0)
                .reorderPoint(1)
                .reorderAmount(1)
                .manufacturingCost(1.0)
                .name("Test Item 1")
                .idealTemperature(1.0)
                .build();
    }

    private Item getItem2() {
        return getInstance().getItemBuilder()
                .sellPrice(2.2)
                .reorderPoint(94)
                .reorderAmount(3)
                .manufacturingCost(32.5)
                .name("Test Item 2")
                .build();
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
            builder.addStockedItem(list.get(i), i);
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
                    assertEquals(stockQuant.getLeft(), getItem1());
                    break;
                case 2:
                    assertEquals(stockQuant.getLeft(), getItem2());
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
}
