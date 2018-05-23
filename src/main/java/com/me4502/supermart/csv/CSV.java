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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Used for parsing different csv file formats, reads and writes
 *
 * @author Liam Dale
 */
public class CSV {

    private static Item.Builder itemBuilder(String[] line) {
        Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder()
                .name(line[0])
                .manufacturingCost(Double.parseDouble(line[1]))
                .reorderPoint(Integer.parseInt(line[2]))
                .reorderAmount(Integer.parseInt(line[3]))
                .sellPrice(Double.parseDouble(line[4]));
        if (line.length == 6) {
            builder.idealTemperature(Double.parseDouble(line[5]));
        }
        return builder;
    }

    public static void loadItemProperties(File file) throws IOException, CSVFormatException {
    	Item tempItem;
    	Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();  	
        List<String[]> lines = readCSV(file);
        for (int i = 0; i < lines.size(); i++) {
            try {
            	tempItem = itemBuilder(lines.get(i)).build();
            	stockBuilder.addStockedItem(tempItem, 0);
                StoreImpl.getInstance().addItem(tempItem);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new CSVFormatException("Invalid format on line " + (i + 1));
            }
        }
        StoreImpl.getInstance().setInventory(stockBuilder.build());
    }

    public static void loadSalesLog(File file) throws IOException, StockException, CSVFormatException {
        Stock currentStock = StoreImpl.getInstance().getInventory();
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();

        // Build the sold stock
        List<String[]> lines = readCSV(file);
        for (int i = 0; i < lines.size(); i++) {
            try {
                stockBuilder.addStockedItem(StoreImpl.getInstance().getItem(lines.get(i)[0]).get(), Integer.parseInt(lines.get(i)[1]));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new CSVFormatException("Invalid format on line " + (i + 1));
            } catch (NoSuchElementException e) {
                throw new StockException("Store doesn't stock " + lines.get(i)[0] + ", but sales log contains it.");
            }
        }

        Stock soldStock = stockBuilder.build();
        stockBuilder.reset();

        // Create the new stock
        for (ImmutablePair<Item, Integer> itemPair : currentStock.getStockedItemQuantities()) {
            stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
        }
        // Getting the total sell value of the stock while continuing to create the new stock
        double totalValue = 0; 
        for (ImmutablePair<Item, Integer> itemPair : soldStock.getStockedItemQuantities()) {
            if (StoreImpl.getInstance().getItem(itemPair.getLeft().getName()).isPresent()) {
                totalValue += itemPair.getLeft().getSellPrice() * itemPair.getRight();
                stockBuilder.addStockedItem(itemPair.getLeft(), -itemPair.getRight());
            } else {
                throw new StockException("Store doesn't stock " + itemPair.getLeft().getName() + ", but sales log contains it.");
            }
        }

        // Update the stock and the store capital
        StoreImpl.getInstance().setInventory(stockBuilder.build());
        StoreImpl.getInstance().setCapital(StoreImpl.getInstance().getCapital() + totalValue);
    }

    public static void loadManifest(File file) throws IOException, CSVFormatException, DeliveryException {
        Stock currentStock = StoreImpl.getInstance().getInventory();

        // Create builders
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
        OrdinaryTruck.OrdinaryBuilder ordinaryBuilder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();
        RefrigeratedTruck.RefrigeratedBuilder refrigeratedBuilder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();

        // Create a list of trucks
        ArrayList<Truck> truckList = new ArrayList<>();

        // Iterate backwards over csv to fill the list of trucks
        ArrayList<String[]> lines = readCSV(file);
        for (int counter = lines.size() - 1; counter >= 0; counter--) {
            String[] line = lines.get(counter);
            if (line.length == 2) {
                stockBuilder.addStockedItem(StoreImpl.getInstance().getItem(line[0]).get(), Integer.parseInt(line[1]));
            } else if (line.length == 1) {
                switch (line[0]) {
                    case ">Ordinary":
                        truckList.add(ordinaryBuilder.cargo(stockBuilder.build()).build());
                        stockBuilder.reset();
                        ordinaryBuilder.reset();
                        break;
                    case ">Refrigerated":
                        truckList.add(refrigeratedBuilder.cargo(stockBuilder.build()).build());
                        stockBuilder.reset();
                        refrigeratedBuilder.reset();
                        break;
                    default:
                        throw new CSVFormatException("Unknown truck type " + line[0]);
                }
            } else {
                throw new CSVFormatException("Unknown format on line " + (counter + 1));
            }
        }

        // Create the new stock
        for (ImmutablePair<Item, Integer> itemPair : currentStock.getStockedItemQuantities()) {
            stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
        }
        // Find the value of the manifest while continuing to create the new stock
        double totalValue = 0;
        for (Truck truck : truckList) {
            totalValue += truck.getCost();
            for (ImmutablePair<Item, Integer> itemPair : truck.getCargo().getStockedItemQuantities()) {
                if (StoreImpl.getInstance().getItem(itemPair.getLeft().getName()).isPresent()) {
                    totalValue += itemPair.getLeft().getManufacturingCost() * itemPair.getRight();
                    stockBuilder.addStockedItem(itemPair.getLeft(), itemPair.getRight());
                } else {
                    throw new DeliveryException("Store doesn't stock " + itemPair.getLeft().getName() + ", but manifest log contains it.");
                }
            }
        }

        // Update the stock and the store capital
        StoreImpl.getInstance().setInventory(stockBuilder.build());
        StoreImpl.getInstance().setCapital(StoreImpl.getInstance().getCapital() - totalValue);
    }

    // Initial tester
    public static void exportManifest(File file, Manifest manifest) throws IOException {
        FileWriter writer = new FileWriter(file.getAbsolutePath());
        for (Truck truck : manifest.getTrucks()) {
            writer.write(">" + truck.getType() + "\n");
            for (ImmutablePair<Item, Integer> pair : truck.getCargo().getStockedItemQuantities()) {
                writer.write(pair.getLeft().getName() + "," + pair.getRight() + "\n");
            }
        }
        writer.close();
    }

    private static ArrayList<String[]> readCSV(File file) throws IOException {
        ArrayList<String[]> linesList = new ArrayList<>();
        String line;
        FileReader fileReader = new FileReader(file.getAbsolutePath());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((line = bufferedReader.readLine()) != null) {
            linesList.add(line.split(","));
        }
        bufferedReader.close();
        return linesList;
    }
}