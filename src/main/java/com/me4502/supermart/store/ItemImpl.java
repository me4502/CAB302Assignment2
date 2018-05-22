package com.me4502.supermart.store;

import java.util.OptionalDouble;

/**
 * Implementation for the {@link Item} interface
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
    private ItemImpl(String name, Double manufacturingCost, Double sellPrice, Integer reorderPoint, Integer reorderAmount,
            OptionalDouble idealTemperature) {
        this.name = name;
        this.manufacturingCost = manufacturingCost;
        this.sellPrice = sellPrice;
        this.reorderPoint = reorderPoint;
        this.reorderAmount = reorderAmount;
        this.idealTemperature = idealTemperature;
    }

    // Return properties
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getManufacturingCost() {
        return this.manufacturingCost; 
    }

    @Override
    public double getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public int getReorderPoint() {
        return this.reorderPoint;
    }

    @Override
    public int getReorderAmount() {
        return this.reorderAmount;
    }

    @Override
    public OptionalDouble getIdealTemperature() {
        return this.idealTemperature;
    }

    @Override
    public boolean isTemperatureControlled() {
        return this.idealTemperature.isPresent();
    }
    
    @Override 
    public boolean equals(Object obj) {
    	if (obj instanceof Item) {
    		return this.name.equals(((Item) obj).getName());
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return this.name.hashCode();
    }

    /**
     * {@inheritDoc}
     *
     * @author Liam Dale
     */
    public static class ItemBuilder implements Item.Builder {

        private String name;
        private Double manufacturingCost;
        private Double sellPrice;
        private Integer reorderPoint;
        private Integer reorderAmount;
        private OptionalDouble idealTemperature = OptionalDouble.empty();

        @Override
        public Builder name(String name) {
            //throw exception - based off what?
            this.name = name;
            return this;
        }

        @Override
        public Builder manufacturingCost(double manufacturingCost) {
            if (manufacturingCost < 0) {
                throw new IllegalArgumentException("Manufacturing cost must be positive");
            }
            this.manufacturingCost = manufacturingCost;
            return this;
        }

        @Override
        public Builder sellPrice(double sellPrice) {
            if (sellPrice < 0) {
                throw new IllegalArgumentException("Sell price must positive");
            }
            this.sellPrice = sellPrice;
            return this;
        }

        @Override
        public Builder reorderPoint(int reorderPoint) {
            if (reorderPoint < 0) {
                throw new IllegalArgumentException("Reorder point must positive");
            }
            this.reorderPoint = reorderPoint;
            return this;
        }

        @Override
        public Builder reorderAmount(int reorderAmount) {
            if (reorderAmount < 0) {
                throw new IllegalArgumentException("Reorder amount must positive");
            }
            this.reorderAmount = reorderAmount;
            return this;
        }

        @Override
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

        @Override
        public Item build() {
            if (name == null || manufacturingCost == null || sellPrice == null || reorderPoint == null || reorderAmount == null) {
                throw new IllegalStateException("Need to set all parameters besides idealTemperature");
            }
            return new ItemImpl(name, manufacturingCost, sellPrice, reorderPoint, reorderAmount, idealTemperature);
        }

        @Override
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
