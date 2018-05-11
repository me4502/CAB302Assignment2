package com.me4502.supermart.store;

import java.util.OptionalDouble;

public class ItemImpl implements Item {
	private String name;
	private double manufacturingCost;
	private double sellPrice;
	private int reorderPoint;
	private int reorderAmount;
	private OptionalDouble idealTemperature;

	
	// Constructs an item, can overload with storage temperature
	public ItemImpl(String name, double manufacturingCost, double sellPrice, int reorderPoint, int reorderAmount, OptionalDouble idealTemperature) throws IllegalStateException {
		if (manufacturingCost < 0 || sellPrice < 0 || reorderPoint < 0 || reorderAmount < 0) {
			throw new IllegalStateException();
		}
		this.name = name;
		this.manufacturingCost = manufacturingCost;
		this.sellPrice = sellPrice;
		this.reorderPoint = reorderPoint;
		this.reorderAmount = reorderAmount;
		this.idealTemperature = idealTemperature;	
	}
	/*
	public Item(String name, double manufacturingCost, double sellPrice, int reorderPoint, int reorderAmount) {
		// refrigeration --> make null? integer type?
		// dry goods boolean --> could making this an additional property be useful?
		this(name, manufacturingCost, sellPrice, reorderPoint, reorderAmount, 100);
	}
	*/

	// Return properties
	public String getName() {
		return name;
	}
	public double getManufacturingCost() {
		return manufacturingCost;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public int getReorderPoint() {
		return reorderPoint;
	}
	public int getReorderAmount() {
		return reorderAmount;
	}
	public double getIdealTemperature() {
		return idealTemperature.getAsDouble();
	}
	public boolean isTemperatureControlled() {
		return idealTemperature.isPresent();
	}
	
	public static class ItemBuilder implements Item.Builder {
		private String name;
		private OptionalDouble manufacturingCost;
		private double sellPrice;
		private int reorderPoint;
		private int reorderAmount;
		private OptionalDouble idealTemperature;
		
		/**
         * Sets the name of the {@link Item}.
         *
         * @param name The name
         * @return The builder, for chaining
         */
        public Builder name(String name) {
        	//throw exception
        	this.name = name;
        }

        /**
         * Sets the manufacturing cost of the {@link Item}.
         *
         * @param manufacturingCost The manufacturing cost
         * @return The builder, for chaining
         */
        public Builder manufacturingCost(double manufacturingCost) {
        	
        }

        /**
         * Sets the sell price of the {@link Item}.
         * @param sellPrice The sell price
         * @return The builder, for chaining
         */
        public Builder sellPrice(double sellPrice) {
        	
        }

        /**
         * Sets the reorder point of the {@link Item}.
         * @param reorderPoint The reorder point
         * @return The builder, for chaining
         */
        public Builder reorderPoint(int reorderPoint) {
        	
        }

        /**
         * Sets the reorder amount of the {@link Item}.
         * @param reorderAmount The reorder amount
         * @return The builder, for chaining
         */
        public Builder reorderAmount(int reorderAmount) {
        	
        }

        /**
         * Sets the ideal temperature of the {@link Item}.
         * This is an optional field.
         * @param idealTemperature The ideal temperature
         * @return The builder, for chaining
         */
        public Builder idealTemperature(double idealTemperature) {
        }

        /**
         * Builds the {@link Item} using the given values.
         * If any of the required values have not been filled in, this
         * should throw an {@link IllegalStateException}.
         * @return The built Item
         * @throws IllegalStateException If a value has not been filled in
         */
        public Item build() {
        	//throw exception if not set --> opt
        	return new Item(name, ..);
        }

        /**
         * Resets the state of this builder.
         * @return The builder, for chaining
         */
        public Builder reset() {
        	//set all to nothing
        }
	}
}
