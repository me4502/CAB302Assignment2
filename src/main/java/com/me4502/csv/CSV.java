/*
 * ONLY PARTIAL 
 */



package com.me4502.csv;
// Reading
import java.io.FileReader;
import java.io.BufferedReader;
// Writing
import java.io.FileWriter;
import java.util.Map;

import com.me4502.supermart.store.Item;
import com.me4502.supermart.store.Stock;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
// Exceptions
import java.io.FileNotFoundException;
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
	public static ArrayList<Item> loadItemProperties(String fileName) {
		ArrayList<Item> items = new ArrayList<Item>();
		for (String[] line : readCSV(fileName)) {
			// TODO: add individual item to items
			for (String property : line) {
				System.out.println(property);
			}
		}
		return items;
	}
	
	public static ArrayList<String[]> readCSV(String fileName) {
		ArrayList<String[]> linesList = new ArrayList<String[]>();
		String fileDirectory = "/Users/Liam Dale/csv/" + fileName + ".csv";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileDirectory);  
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                linesList.add(line.split(","));
            }
            bufferedReader.close();         
        }
        // Catch the exception
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return linesList;
	}
	
	// Initial tester
	public static void exportManifest(Stock stock) {
		Date now = new Date();
		SimpleDateFormat formattedDate = new SimpleDateFormat("'-'dd.MM.yy'-'hh.mm.ssa");
		String fileName = "manifest" + formattedDate.format(now);
		String fileDirectory = "/Users/Liam Dale/csv/" + fileName + ".csv";
		try {
			FileWriter writer = new FileWriter(fileDirectory);
			// TODO:
			// Iterate through stock hashmap --> needs to be changed to ImmutableSet<ImmutablePair<Item, int>>
			for (Map.Entry<Item, Integer> entry : stock.getStockedItemQuantities().entrySet()) {
				writer.write(entry.getKey().getName() + "," + entry.getValue() + "\n");
			}
			writer.close();
		}
		// Catch the exception
		catch(IOException io) {
			System.out.println("Error writing file '" + fileName + "'");
		}
	}
}