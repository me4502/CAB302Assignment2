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
	public static class ItemDummy implements Item {
		OptionalDouble idealTemperature;
		
		public ItemDummy(OptionalDouble idealTemperature) {
			this.idealTemperature = idealTemperature;
		}
		public ItemDummy() {
			this(OptionalDouble.empty());
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
	public static class StockDummy implements Stock {
		private int totalAmount;
		private OptionalDouble lowestTemperature;
		
		// Need to be able to set amount so cargo capacity can be tested
		public StockDummy(int totalAmount, OptionalDouble lowestTemperature) {
			this.totalAmount = totalAmount;
			this.lowestTemperature = lowestTemperature;
		}
		// Need to be able to set amount so cargo capacity can be tested
		public StockDummy(int totalAmount) {
			this(totalAmount, OptionalDouble.empty());
		}
		
		@Override
		public ImmutableSet<Item> getStockedItems() {
			return ImmutableSet.of(new ItemDummy(lowestTemperature));
		}
		@Override
		public ImmutableSet<ImmutablePair<Item, Integer>> getStockedItemQuantities() {
			return ImmutableSet.of(ImmutablePair.of(new ItemDummy(), totalAmount));
		}
	}
}
