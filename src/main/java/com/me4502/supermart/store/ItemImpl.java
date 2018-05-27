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

    /**
     * 
     * Creates an Item
     * 
     * @param name of item
     * @param manufacturingCost of item
     * @param sellPrice of item
     * @param reorderPoint of item
     * @param reorderAmount of item
     * @param idealTemperature of item
     */
    private ItemImpl(String name, Double manufacturingCost, Double sellPrice, Integer reorderPoint, Integer reorderAmount,
            OptionalDouble idealTemperature) {
        this.name = name;
        this.manufacturingCost = manufacturingCost;
        this.sellPrice = sellPrice;
        this.reorderPoint = reorderPoint;
        this.reorderAmount = reorderAmount;
        // idealTemperature is an optional, as it may not always have a value
        this.idealTemperature = idealTemperature;
    }

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
    // This is necessary to compare instances of Item
    public boolean equals(Object obj) {
    	// If the argument is an Item, and it has the same name property as this then they're equal
        if (obj instanceof Item) {
            return this.name.equals(((Item) obj).getName());
        }
        return false;
    }

    @Override
    // This is also necessary to compare instances of Item
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
        	// Item names can't be empty
            if (name.length() < 1) {
            	throw new IllegalArgumentException("Name must not be empty");
            }
            this.name = name;
            return this;
        }

        @Override
        public Builder manufacturingCost(double manufacturingCost) {
        	// Manufacturing cost must be positive
            if (manufacturingCost < 0) {
                throw new IllegalArgumentException("Manufacturing cost must be positive");
            }
            this.manufacturingCost = manufacturingCost;
            return this;
        }

        @Override
        public Builder sellPrice(double sellPrice) {
        	// Sell price must be positive
            if (sellPrice < 0) {
                throw new IllegalArgumentException("Sell price must positive");
            }
            this.sellPrice = sellPrice;
            return this;
        }

        @Override
        public Builder reorderPoint(int reorderPoint) {
        	// Can't have negative quantities
            if (reorderPoint < 0) {
                throw new IllegalArgumentException("Reorder point must positive");
            }
            this.reorderPoint = reorderPoint;
            return this;
        }

        @Override
        public Builder reorderAmount(int reorderAmount) {
        	// Can't have negative reorder amounts
            if (reorderAmount < 0) {
                throw new IllegalArgumentException("Reorder amount must positive");
            }
            this.reorderAmount = reorderAmount;
            return this;
        }

        @Override
        public Builder idealTemperature(double idealTemperature) {
        	// Items can't be stored below -20 degrees
            if (idealTemperature < -20) {
                throw new IllegalArgumentException("Items under 20 deg Celcius can't be stored in a truck");
            }
            // Items above 10 degrees are dry goods
            if (idealTemperature > 10) {
                throw new IllegalArgumentException("Items over 10 deg Celcius are considered dry goods");
            }
            this.idealTemperature = OptionalDouble.of(idealTemperature);
            return this;
        }

        @Override
        public Item build() {
        	// All parameters must be set
            if (this.name == null || this.manufacturingCost == null || this.sellPrice == null || this.reorderPoint == null
                    || this.reorderAmount == null) {
                throw new IllegalStateException("Need to set all parameters besides idealTemperature");
            }
            // Return a built item with the ItemImpl constructor
            return new ItemImpl(this.name, this.manufacturingCost, this.sellPrice, this.reorderPoint, this.reorderAmount, this.idealTemperature);
        }

        @Override
        public Builder reset() {
        	// Empty fields
            this.name = null;
            this.manufacturingCost = null;
            this.sellPrice = null;
            this.reorderPoint = null;
            this.reorderAmount = null;
            this.idealTemperature = OptionalDouble.empty();
            return this;
        }
    }
}
