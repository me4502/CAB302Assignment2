package com.me4502.supermart.store;

import java.util.OptionalDouble;

/**
 * Implementation of the item interface
 *
 * @author Liam Dale
 */
public class ItemImpl implements Item {
	private String name;
	private Double manufacturingCost;
	private Double sellPrice;
	private Integer reorderPoint;
	private Integer reorderAmount;
	private OptionalDouble idealTemperature;
	
	// Constructs an item, can overload with storage temperature
	/*
	public ItemImpl(String name, Double manufacturingCost, Double sellPrice, Integer reorderPoint, Integer reorderAmount) {
		this(name, manufacturingCost, sellPrice, reorderPoint, reorderAmount, OptionalDouble.empty());	
	}
	*/
	public ItemImpl(String name, Double manufacturingCost, Double sellPrice, Integer reorderPoint, Integer reorderAmount, OptionalDouble idealTemperature) {
		this.name = name;
		this.manufacturingCost = manufacturingCost;
		this.sellPrice = sellPrice;
		this.reorderPoint = reorderPoint;
		this.reorderAmount = reorderAmount;
		this.idealTemperature = idealTemperature;	
	}

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
	public OptionalDouble getIdealTemperature() {
		return idealTemperature;
	}
	public boolean isTemperatureControlled() {
		return idealTemperature.isPresent();
	}
	
	public static class ItemBuilder implements Item.Builder {
		private String name;
		private Double manufacturingCost;
		private Double sellPrice;
		private Integer reorderPoint;
		private Integer reorderAmount;
		private OptionalDouble idealTemperature;
		
		/**
         * Sets the name of the {@link Item}.
         *
         * @param name The name
         * @return The builder, for chaining
         */
        public Builder name(String name) {
        	//throw exception - based off what?
        	this.name = name;
        	return this;
        }

        /**
         * Sets the manufacturing cost of the {@link Item}.
         *
         * @param manufacturingCost The manufacturing cost
         * @return The builder, for chaining
         */
        public Builder manufacturingCost(double manufacturingCost) {
        	if (manufacturingCost < 0) {
    			throw new IllegalArgumentException("Manufacturing cost must be positive");
    		}
        	this.manufacturingCost = manufacturingCost;
        	return this;
        }

        /**
         * Sets the sell price of the {@link Item}.
         * @param sellPrice The sell price
         * @return The builder, for chaining
         */
        public Builder sellPrice(double sellPrice) {
        	if (sellPrice < 0) {
    			throw new IllegalArgumentException("Sell price must positive");
    		}
        	this.sellPrice = sellPrice;
        	return this;
        }

        /**
         * Sets the reorder point of the {@link Item}.
         * @param reorderPoint The reorder point
         * @return The builder, for chaining
         */
        public Builder reorderPoint(int reorderPoint) {
        	if (reorderPoint < 0) {
    			throw new IllegalArgumentException("Reorder point must positive");
    		}
        	this.reorderPoint = reorderPoint;
        	return this;
        }

        /**
         * Sets the reorder amount of the {@link Item}.
         * @param reorderAmount The reorder amount
         * @return The builder, for chaining
         */
        public Builder reorderAmount(int reorderAmount) {
        	if (reorderAmount < 0) {
    			throw new IllegalArgumentException("Reorder amount must positive");
    		}
        	this.reorderAmount = reorderAmount;
        	return this;
        }

        /**
         * Sets the ideal temperature of the {@link Item}.
         * This is an optional field.
         * @param idealTemperature The ideal temperature
         * @return The builder, for chaining
         */
        public Builder idealTemperature(double idealTemperature) {
        	if (idealTemperature < -20) {
    			throw new IllegalArgumentException("Items under 20 deg Celcius can't be stored in a truck");
    		}
        	if (idealTemperature > 10) {
    			throw new IllegalArgumentException("Items over 10 deg Celcius are considered dry goods");
    		}
        	this.idealTemperature = OptionalDouble.of(idealTemperature);
        	return this;
        }

        /**
         * Builds the {@link Item} using the given values.
         * If any of the required values have not been filled in, this
         * should throw an {@link IllegalStateException}.
         * @return The built Item
         * @throws IllegalStateException If a value has not been filled in
         */
        public Item build() {
    		if (name == null || manufacturingCost == null|| sellPrice == null || reorderPoint == null || reorderAmount == null) {
    			throw new IllegalStateException("Need to set all parameters besides idealTemperature");
    		}
        	return new ItemImpl(name, manufacturingCost, sellPrice, reorderPoint, reorderAmount, idealTemperature);
        }

        /**
         * Resets the state of this builder.
         * @return The builder, for chaining
         */
        public Builder reset() {
        	name = null;
    		manufacturingCost = null;
    		sellPrice = null;
    		reorderPoint = null;
    		reorderAmount = null;
    		idealTemperature = OptionalDouble.empty();
    		return this;
        }
	}
}
