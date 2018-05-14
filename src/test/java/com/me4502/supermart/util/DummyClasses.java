package com.me4502.supermart.util;

import java.util.OptionalDouble;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

/**
 * Class which can be used to represent stock and items for unit testing
 *
 * @author Liam Dale
 */
public class DummyClasses {
	// Item implementation for creating stock --> can be used in stock tests
	public class itemDummy implements Item {
		OptionalDouble idealTemperature;
		
		public itemDummy(OptionalDouble idealTemperature) {
			this.idealTemperature = idealTemperature;
		}
		public itemDummy() {
			this(null);
		}
		
		@Override
		public String getName() {
			return null;
		}
		@Override
		public double getManufacturingCost() {
			return 0;
		}
		@Override
		public double getSellPrice() {
			return 0;
		}
		@Override
		public int getReorderPoint() {
			return 0;
		}
		@Override
		public int getReorderAmount() {
			return 0;
		}
		@Override
		public OptionalDouble getIdealTemperature() {
			return idealTemperature;
		}
	}
	// Stock implementation for testing --> used in truck tests
	public class StockDummy implements Stock {
		private int totalAmount;
		
		// Need to be able to set amount so cargo capacity can be tested
		public StockDummy(int totalAmount) {
			this.totalAmount = totalAmount;
		}
		
		@Override
		public ImmutableSet<Item> getStockedItems() {
			return ImmutableSet.of(new itemDummy());
		}
		@Override
		public ImmutableSet<ImmutablePair<Item, Integer>> getStockedItemQuantities() {
			return ImmutableSet.of(ImmutablePair.of(new itemDummy(), totalAmount));
		}
	}
}
