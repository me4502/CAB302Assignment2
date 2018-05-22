package com.me4502.supermart.store;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.truck.Manifest;

import java.util.Optional;

/**
 * Represents a store. This class is a singleton.
 *
 * @author Madeline Miller
 */
public interface Store {

    /**
     * Gets the name of the store.
     *
     * @return The name of the store
     */
    String getName();

    /**
     * Gets the current capital of the store.
     *
     * @return The capital of the store
     */
    double getCapital();

    /**
     * Sets the capital of the store.
     *
     * @param capital The new capital
     */
    void setCapital(double capital);

    /**
     * Gets the capital formatted in dollars and cents.
     *
     * For example, 1054.34 will be formatted as $1,054.34
     *
     * @return The formatted capital
     */
    String getFormattedCapital();

    /**
     * Gets the current inventory of the store.
     *
     * @return The inventory of the store
     */
    Stock getInventory();

    /**
     * Sets the current inventory of the store.
     *
     * @param inventory The new inventory
     */
    void setInventory(Stock inventory);

    /**
     * Adds a stockable item to this Store.
     *
     * This refers to a possible stockable item,
     * not the actual stock of the store.
     *
     * @param item The item
     */
    void addItem(Item item);

    /**
     * Gets an item from the stockable items by name, if present.
     *
     * This refers to a possible stockable item,
     * not the actual stock of the store.
     *
     * @param name The item name
     * @return The item, if present
     */
    Optional<Item> getItem(String name);

    /**
     * Gets the items that this store can possibly stock.
     *
     * This refers to the possible stockable items, not
     * the actual stock of the store.
     *
     * @return An immutable set of items
     */
    ImmutableSet<Item> getItems();

    /**
     * Gets the manifest of this store.
     *
     * @return The manifest
     */
    Manifest getManifest();

    /**
     * Sets the manifest of this store.
     *
     * @param manifest The manifest
     */
    void setManifest(Manifest manifest);
}
