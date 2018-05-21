package com.me4502.csv;
// Reading
import java.io.FileReader;


import java.io.BufferedReader;
import java.io.File;
// Writing
import java.io.FileWriter;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.exception.CSVFormatException;
import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.store.StoreImpl;
import com.me4502.supermart.truck.Manifest;
import com.me4502.supermart.truck.OrdinaryTruck;
import com.me4502.supermart.truck.RefrigeratedTruck;
import com.me4502.supermart.truck.Truck;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
// Exceptions
import java.io.IOException;


/**
 * Used for parsing different csv file formats, reads and writes
 *
 * @author Liam Dale
 */
public class CSV {
	// item_properties
	// manifest
	// sales_log_0

	private static Item.Builder itemBuilder(String[] line) {
		Item.Builder builder = SuperMartApplication.getInstance().getItemBuilder()
				.name(line[0])
                .manufacturingCost(Double.parseDouble(line[1]))
                .reorderAmount(Integer.parseInt(line[2]))
                .reorderPoint(Integer.parseInt(line[3]))
                .sellPrice(Double.parseDouble(line[4]));
		if (line.length == 6) {
			builder.idealTemperature(Double.parseDouble(line[5]));
		}
		return builder;
	}
	
	public static void loadItemProperties(File file) throws IOException {
		for (String[] line : readCSV(file)) {
			StoreImpl.getInstance().addItem(itemBuilder(line).build());
		};
	}
	
	public static void loadSalesLog(File file) throws IOException{
		Stock currentStock = StoreImpl.getInstance().getInventory();
		Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
		
		// Build the sold stock
		for (String[] line : readCSV(file)) {
			stockBuilder.addStockedItem(StoreImpl.getInstance().getItem(line[0]).get(), Integer.parseInt(line[1]));
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
			}
			else {
				throw new CSVFormatException();
			}
    	}
		
		// Update the stock and the store capital
		StoreImpl.getInstance().setInventory(stockBuilder.build());
		StoreImpl.getInstance().setCapital(StoreImpl.getInstance().getCapital() + totalValue);
	}
	
	public static void loadManifest(File file) throws IOException {
		Stock currentStock = StoreImpl.getInstance().getInventory();
		
		// Create builders
		Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
		OrdinaryTruck.OrdinaryBuilder ordinaryBuilder = SuperMartApplication.getInstance().getOrdinaryTruckBuilder();
		RefrigeratedTruck.RefrigeratedBuilder refrigeratedBuilder = SuperMartApplication.getInstance().getRefrigeratedTruckBuilder();
		
		// Create a list of trucks 
		ArrayList<Truck> truckList = new ArrayList<Truck>();
		
		// Iterate backwards over csv to fill the list of trucks
		ArrayList<String[]> lines = readCSV(file);
		for(int counter= lines.size() - 1; counter >= 0; counter--) {
			String[] line = lines.get(counter);
			if (line.length == 2) {
				stockBuilder.addStockedItem(StoreImpl.getInstance().getItem(line[0]).get(), Integer.parseInt(line[1]));				
			}
			else if (line.length == 1) {
				if (line[0] == ">Ordinary") {
					truckList.add(ordinaryBuilder.cargo(stockBuilder.build()).build());
					stockBuilder.reset();
					ordinaryBuilder.reset();
				}
				else if (line[0] == ">Refrigerated") {
					truckList.add(refrigeratedBuilder.cargo(stockBuilder.build()).build());
					stockBuilder.reset();
					refrigeratedBuilder.reset();
				}
				else {
					throw new CSVFormatException();
				}	
			}
			else {
				throw new CSVFormatException();
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
				}
				else {
					throw new CSVFormatException();
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
		ArrayList<String[]> linesList = new ArrayList<String[]>();
        String line = null;
        FileReader fileReader = new FileReader(file.getAbsolutePath());  
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while((line = bufferedReader.readLine()) != null) {
            linesList.add(line.split(","));
        }
        bufferedReader.close();
        return linesList;
	}
}