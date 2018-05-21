package com.me4502.supermart.store;
import java.util.HashMap;
import java.util.HashSet;
import java.util.OptionalInt;

import org.apache.commons.lang3.tuple.ImmutablePair;
import com.google.common.collect.ImmutableSet;

public class StockImpl implements Stock {
	ImmutableSet<ImmutablePair<Item, Integer>> pairSet;
	public StockImpl(ImmutableSet<ImmutablePair<Item, Integer>> pairSet) {
		this.pairSet = pairSet;
	}

	// Getting the total amount of items
	@Override
	public int getTotalAmount() {
		//mapToInt unbox the Integer (i->i)
		//stock.values().stream().mapToInt(i->i).sum();
		int totalAmount = 0;
		for (ImmutablePair<Item, Integer> pair : pairSet) {
    		totalAmount += pair.getRight();
    	}
    	return totalAmount;
	}
	
	// Gets a set of all stocked items
	@Override
    public ImmutableSet<Item> getStockedItems() {
    	HashSet<Item> tempSet = new HashSet<Item>();
    	for (ImmutablePair<Item, Integer> pair : pairSet) {
    		tempSet.add(pair.getLeft());
    	}
    	return ImmutableSet.copyOf(tempSet);
    }
    
	// Return the stock map
    @Override
	public ImmutableSet<ImmutablePair<Item, Integer>> getStockedItemQuantities() {
		return pairSet;
	}

	@Override
	public OptionalInt getItemQuantity(Item item) {
    	for (ImmutablePair<Item, Integer> pair : pairSet) {
    		if (item == pair.getLeft()) {
    			return OptionalInt.of(pair.getRight());
    		}
    	}
    	return OptionalInt.empty();
	}

	public static class StockBuilder implements Stock.Builder {
		private HashMap<Item, Integer> stock = new HashMap<Item, Integer>();
		public Builder addStockedItem(Item item, int quantity) {
			if (item == null || quantity  < 1) {
    			throw new IllegalArgumentException("Item cannot be null and must be positive");
    		}
			if (stock.containsKey(item)) {
				stock.put(item, new Integer(stock.get(item) + quantity));
			}
			else {
				stock.put(item, new Integer(quantity));
			}
			return this;
		}

		public Stock build() {
			// create the immutable set here
			// add the immutable set of immutable pairs as a parameter
			ImmutableSet<ImmutablePair<Item, Integer>> pairSet = stock.entrySet().stream()
					.map(e -> ImmutablePair.of(e.getKey(), e.getValue()))
					.collect(ImmutableSet.toImmutableSet()); 
			return new StockImpl(pairSet);
		}

		public Builder reset() {
			// clear the map
			stock.clear();
			return this;
		}
	}
}