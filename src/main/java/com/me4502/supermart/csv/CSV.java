package com.me4502.supermart.csv;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.exception.CSVFormatException;
import com.me4502.supermart.exception.DeliveryException;
import com.me4502.supermart.exception.StockException;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.store.StoreImpl;
import com.me4502.supermart.truck.Manifest;
import com.me4502.supermart.truck.OrdinaryTruck;
import com.me4502.supermart.truck.RefrigeratedTruck;
import com.me4502.supermart.truck.Truck;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Used for parsing different csv file formats, reads and writes
 *
 * @author Liam Dale
 */
public class CSV {

    /**
     * Return an item builder base off a string array from csv
     *
     * @param line The CSV line to parse
     * @return The Item Builder
     */
    private static Item.Builder itemBuilder(String[] line) {
        Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder()
                .name(line[0])
                .manufacturingCost(Double.parseDouble(line[1]))
                .sellPrice(Double.parseDouble(line[2]))
                .reorderPoint(Integer.parseInt(line[3]))
                .reorderAmount(Integer.parseInt(line[4]));
        if (line.length == 6) {
            builder.idealTemperature(Double.parseDouble(line[5]));
        }
        return builder;
    }

    /**
     * Load item properties and set created items with quantity of zero in inventory
     *
     * @param file The file to load
     * @throws IOException if the file can't be loaded
     * @throws CSVFormatException if the format is invalid
     */
    public static void loadItemProperties(File file) throws IOException, CSVFormatException {
        Item tempItem;
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();

        // Create the new stock, based off the current inventory -- necessary to not reset if properties reloaded
        for (ImmutablePair<Item, Integer> itemPair : StoreImpl.getInstance().getInventory().getStockedItemQuantities()) {
            stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
        }

        // Add any new properties
        List<String[]> lines = readCSV(file);
        for (int i = 0; i < lines.size(); i++) {
            try {
                // Build the item
                tempItem = itemBuilder(lines.get(i)).build();
                // If the item is not in the list of stockables, add it to stockables and inventory with zero quantity
                if (!(StoreImpl.getInstance().getItem(tempItem.getName()).isPresent())) {
                    StoreImpl.getInstance().addItem(tempItem);
                    stockBuilder.addStockedItem(tempItem, 0);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Create a detailed message
                StringBuilder message = new StringBuilder("Invalid item formatting on line " + (i + 1) + ". \n\n"
                        + "Should be in the form:\n"
                        + "[item], [cost], [price], [reorder point], [reorder amount] OR\n"
                        + "[item], [cost], [price], [reorder point], [reorder amount], [temperature]\n\n"
                        + "But was presented as:\n");
                List<String> lineList = Arrays.asList(lines.get(i));
                for (String line : lineList) {
                    message.append((lineList.indexOf(line) != lineList.size() - 1) ? '[' + line + "], " : '[' + line + ']');
                }
                throw new CSVFormatException(message.toString());
            } catch (IllegalStateException | IllegalArgumentException e) {
                throw new CSVFormatException(e.getMessage());
            }
        }
        // Set items in inventory, with zero quantity
        StoreImpl.getInstance().setInventory(stockBuilder.build());
    }


    /**
     * Load a sales log, update the store capital and inventory appropriately
     *
     * @param file The file to load
     * @throws IOException if the file failed to be loaded
     * @throws StockException if the stock was invalid
     * @throws CSVFormatException if the format was invalid
     */
    public static void loadSalesLog(File file) throws IOException, StockException, CSVFormatException {
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();

        // Build the sold stock, and then reset the builder
        List<String[]> lines = readCSV(file);
        for (int i = 0; i < lines.size(); i++) {
            try {
                if (lines.get(i).length != 2) {
                    throw new CSVFormatException(null);
                }
                stockBuilder.addStockedItem(StoreImpl.getInstance().getItem(lines.get(i)[0]).get(), Integer.parseInt(lines.get(i)[1]));
            } catch (NumberFormatException | CSVFormatException e) {
                // Create a detailed message
                StringBuilder message = new StringBuilder("Invalid sales log formatting on line " + (i + 1) + ". \n\n"
                        + "Should be in the form:\n"
                        + "[item], [quantity]\n\n"
                        + "But was presented as:\n");
                List<String> lineList = Arrays.asList(lines.get(i));
                for (String line : lineList) {
                    message.append((lineList.indexOf(line) != lineList.size() - 1) ? '[' + line + "], " : '[' + line + ']');
                }
                throw new CSVFormatException(message.toString());
            } catch (NoSuchElementException e) {
                throw new StockException("Store doesn't stock " + lines.get(i)[0] + ", but sales log contains it.");
            } catch (IllegalStateException | IllegalArgumentException e) {
                throw new CSVFormatException(e.getMessage());
            }
        }
        Stock soldStock = stockBuilder.build();
        stockBuilder.reset();

        // Create the new stock, based off the current inventory
        for (ImmutablePair<Item, Integer> itemPair : StoreImpl.getInstance().getInventory().getStockedItemQuantities()) {
            stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
        }

        // Getting the total sell value of the stock while continuing to create the new stock
        double totalValue = 0;
        for (ImmutablePair<Item, Integer> itemPair : soldStock.getStockedItemQuantities()) {
            if (StoreImpl.getInstance().getItem(itemPair.getLeft().getName()).isPresent()) {
                totalValue += itemPair.getLeft().getSellPrice() * itemPair.getRight();
                try {
                    stockBuilder.addStockedItem(itemPair.getLeft(), -itemPair.getRight());
                } catch (IllegalArgumentException e) {
                    throw new StockException(e.getMessage());
                }
            } else {
                throw new StockException("Store doesn't stock " + itemPair.getLeft().getName() + ", but sales log contains it.");
            }
        }

        // Update the stock and the store capital
        StoreImpl.getInstance().setInventory(stockBuilder.build());
        StoreImpl.getInstance().setCapital(StoreImpl.getInstance().getCapital() + totalValue);
    }


    /**
     * Load a manifest, update the store manifest
     *
     * @param file The file to load
     * @throws IOException if the file failed to load
     * @throws CSVFormatException if the format is wrong
     * @throws DeliveryException if there is an issue generating a delivery
     */
    public static void loadManifest(File file) throws IOException, CSVFormatException, DeliveryException {
        // Create builders
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
        OrdinaryTruck.OrdinaryBuilder ordinaryBuilder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();
        RefrigeratedTruck.RefrigeratedBuilder refrigeratedBuilder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();

        // Create a list of trucks
        Manifest.Builder manifestBuilder = SuperMartApplication.getInstance().getManifestBuilder();

        // Iterate backwards over csv to fill the list of trucks
        List<String[]> lines = readCSV(file);
        for (int i = lines.size() - 1; i >= 0; i--) {
            String[] line = lines.get(i);
            if (line.length == 2) {
                try {
                    if (StoreImpl.getInstance().getItem(line[0]).isPresent()) {
                        stockBuilder.addStockedItem(StoreImpl.getInstance().getItem(line[0]).get(), Integer.parseInt(line[1]));
                    } else {
                        throw new DeliveryException("Store doesn't stock " + line[0] + ", but manifest contains it.");
                    }
                } catch (IllegalStateException | IllegalArgumentException e) {
                    throw new CSVFormatException(e.getMessage());
                }
            } else if (line.length == 1) {
                // Build the relevant truck and add it to the manifest, after reset builders for next stock and truck pair
                switch (line[0]) {
                    case ">Ordinary":
                        manifestBuilder.addTruck(ordinaryBuilder.cargo(stockBuilder.build()).build());
                        stockBuilder.reset();
                        ordinaryBuilder.reset();
                        break;
                    case ">Refrigerated":
                        manifestBuilder.addTruck(refrigeratedBuilder.cargo(stockBuilder.build()).build());
                        stockBuilder.reset();
                        refrigeratedBuilder.reset();
                        break;
                    default:
                        throw new CSVFormatException("Unknown truck type " + line[0]);
                }
            } else {
                // Create a detailed message
                StringBuilder message = new StringBuilder("Invalid manifest formatting on line " + (i + 1) + ". \n\n"
                        + "Should be in the form:\n"
                        + ">[truck type] OR\n"
                        + "[item], [quantity]\n\n"
                        + "But was presented as:\n");
                List<String> lineList = Arrays.asList(lines.get(i));
                for (String errorLine : lineList) {
                    message.append((lineList.indexOf(errorLine) != lineList.size() - 1) ? '[' + errorLine + "], " : '[' + errorLine + ']');
                }
                throw new CSVFormatException(message.toString());
            }
        }

        // Throw an exception if there are no trucks in the manifest -- empty manifest may be built
        Manifest manifest = manifestBuilder.build();
        if (manifest.getTrucks().isEmpty()) {
            throw new CSVFormatException("Cannot load a manifest without trucks.");
        }

        // Set the created manifest -- handle inventory and capital changes in storeImpl
        StoreImpl.getInstance().setManifest(manifest, true);
    }


    /**
     * Exports a manifest to the file location in the relevant format
     *
     * @param file to write
     * @param manifest to be written
     * @throws IOException if the file could not be written to
     */
    public static void exportManifest(File file, Manifest manifest) throws IOException {
        FileWriter writer = new FileWriter(file.getAbsolutePath());
        for (Truck truck : manifest.getTrucks()) {
            writer.write('>' + truck.getType() + '\n');
            for (ImmutablePair<Item, Integer> pair : truck.getCargo().getStockedItemQuantities()) {
                writer.write(pair.getLeft().getName() + ',' + pair.getRight() + '\n');
            }
        }
        writer.close();
    }


    /**
     * Reads a csv and returns its contents
     *
     * @param file The file to read
     * @return list of lines in a csv
     * @throws IOException if the file could not be read
     */
    private static ArrayList<String[]> readCSV(File file) throws IOException {
        ArrayList<String[]> linesList;
        FileReader fileReader = new FileReader(file.getAbsolutePath());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        linesList = bufferedReader.lines().map(line -> line.split(","))
                .collect(Collectors.toCollection(ArrayList::new));
        bufferedReader.close();
        return linesList;
    }
}