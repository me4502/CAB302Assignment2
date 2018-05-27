package com.me4502.supermart.gui;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.csv.CSV;
import com.me4502.supermart.exception.CSVFormatException;
import com.me4502.supermart.exception.DeliveryException;
import com.me4502.supermart.exception.StockException;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.store.StoreImpl;
import com.me4502.supermart.truck.ManifestOptimiser;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Point of entry for the GUI of the SuperMart application.
 *
 * @author Madeline Miller
 */
public class SuperMartGui {

    // Form
    private JFrame frame;

    private JLabel capitalLabel;
    private JLabel manifestPaneTitle;

    private JTable manifestTable;
    private JTable inventoryTable;

    private JButton loadManifestButton;
    private JButton saveManifestButton;

    /**
     * Creates a new instance of the GUI.
     */
    private SuperMartGui() {
        // Create an application and store
        new SuperMartApplication();
        new StoreImpl("SuperMart");

        // Setup form
        this.frame = new JFrame();
        this.frame.setTitle("Store Inventory Management");

        // Add panes to tab menu
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Info", createInfoPane());
        tabPane.addTab("Inventory", createInventoryPane());
        tabPane.addTab("Manifest", createManifestPane());
        this.frame.add(tabPane);

        this.frame.setVisible(true);
        this.frame.setSize(750, 500);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates the info pane of the GUI.
     *
     * @return The info panel
     */
    private JPanel createInfoPane() {
        // Setup info pane
        JPanel infoPane = new JPanel();
        infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
        JLabel infoPaneTitle = new JLabel("Store Inventory Management System", SwingConstants.CENTER);
        infoPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        infoPane.add(infoPaneTitle);

        this.capitalLabel = new JLabel();
        setCapitalLabel();
        infoPane.add(new JLabel("Store: " + StoreImpl.getInstance().getName()));
        infoPane.add(this.capitalLabel);

        return infoPane;
    }

    /**
     * Updates the capital label with the current capital of the store.
     */
    private void setCapitalLabel() {
        this.capitalLabel.setText("Capital: " + StoreImpl.getInstance().getFormattedCapital());
    }

    /**
     * Creates the inventory pane of the GUI.
     *
     * @return The inventory pane
     */
    private JPanel createInventoryPane() {
        // Setup inventory pane
        JPanel inventoryPane = new JPanel();
        inventoryPane.setLayout(new BoxLayout(inventoryPane, BoxLayout.Y_AXIS));
        JLabel inventoryPaneTitle = new JLabel("Stored Inventory");
        inventoryPaneTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventoryPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        inventoryPane.add(inventoryPaneTitle);

        // Create the table that stores the inventory
        this.inventoryTable = new JTable();
        fillInventoryTable(this.inventoryTable);
        inventoryPane.add(new JScrollPane(this.inventoryTable));

        // Setup the buttons on the screen
        JButton loadInventoryButton = new JButton("Load Item Properties");
        JButton loadSalesLogButton = new JButton("Load Sales Log");

        // When the load inventory button is pressed, open the file picker
        loadInventoryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(this.frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.loadItemProperties(file);

                    // Update properties now that it's loaded in
                    optimiseManifests();
                    setCapitalLabel();
                    loadSalesLogButton.setEnabled(true);
                    loadInventoryButton.setText("Re-load Item Properties");
                    this.saveManifestButton.setEnabled(true);
                    this.loadManifestButton.setEnabled(true);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this.frame, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                } catch (CSVFormatException | DeliveryException e1) {
                    JOptionPane.showMessageDialog(this.frame, e1.getMessage());
                }
                fillInventoryTable(this.inventoryTable);
            }
        });

        // When the load sales button is pressed, open the file picker
        loadSalesLogButton.setEnabled(false);
        loadSalesLogButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(this.frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.loadSalesLog(file);

                    // Update properties now that it's loaded in
                    optimiseManifests();
                    setCapitalLabel();
                    this.saveManifestButton.setEnabled(true);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this.frame, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                } catch (CSVFormatException | StockException | DeliveryException e1) {
                    JOptionPane.showMessageDialog(this.frame, e1.getMessage());
                }
                fillInventoryTable(this.inventoryTable);
            }
        });

        // Add the buttons in a panel so they're in the same line
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadInventoryButton);
        buttonPanel.add(loadSalesLogButton);
        inventoryPane.add(buttonPanel);

        return inventoryPane;
    }

    /**
     * Fills a table with inventory data.
     *
     * @param inventoryTable The table to fill
     */
    private void fillInventoryTable(JTable inventoryTable) {
        // Create column names
        String[] columns = {
                "Item",
                "Quantity",
                "Manufacturing cost ($)",
                "Sell price ($)",
                "Reorder point",
                "Reorder amount",
                "Temperature (\u00B0C)"
        };

        // Transform the Stock into a data frame for the table model
        Object[][] data = StoreImpl.getInstance().getItems().stream()
                .map(item -> new Object[]{
                        item.getName(),
                        StoreImpl.getInstance().getInventory().getItemQuantity(item).orElse(0),
                        item.getManufacturingCost(),
                        item.getSellPrice(),
                        item.getReorderPoint(),
                        item.getReorderAmount(),
                        item.getIdealTemperature().isPresent() ? item.getIdealTemperature().getAsDouble() : "N/A"
                })
                .toArray(Object[][]::new);

        // Set the model of the table
        inventoryTable.setModel(new ArrayTableModel(data, columns, false));
    }

    /**
     * Creates the manifest pane of the GUI.
     *
     * @return The manifest pane
     */
    private JPanel createManifestPane() {
        // Setup manifest pane
        JPanel manifestPane = new JPanel();
        manifestPane.setLayout(new BoxLayout(manifestPane, BoxLayout.Y_AXIS));
        this.manifestPaneTitle = new JLabel("Manifest");
        this.manifestPaneTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.manifestPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        manifestPane.add(this.manifestPaneTitle);

        // Create the table that stores the manifest
        this.manifestTable = new JTable();
        fillManifestTable(this.manifestTable);
        manifestPane.add(new JScrollPane(this.manifestTable));

        // Setup the buttons on the screen
        this.loadManifestButton = new JButton("Load Manifests");
        this.loadManifestButton.setEnabled(false);
        this.saveManifestButton = new JButton("Save Manifests");
        this.saveManifestButton.setEnabled(false);

        // When the load button is pressed, open a file picker
        this.loadManifestButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(this.frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.loadManifest(file);

                    // Update properties now that it's loaded in
                    setCapitalLabel();
                    this.saveManifestButton.setEnabled(false);
                    this.manifestPaneTitle.setText("Imported Manifest");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this.frame, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                } catch (CSVFormatException | DeliveryException e1) {
                    JOptionPane.showMessageDialog(this.frame, e1.getMessage());
                }
                fillManifestTable(this.manifestTable);
                fillInventoryTable(this.inventoryTable);
            }
        });

        // When the save button is pressed, open a file picker for saving
        this.saveManifestButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showSaveDialog(this.frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.exportManifest(file, StoreImpl.getInstance().getManifest());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this.frame, "Failed to save the file: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });

        // Add the buttons in a panel so that they're in line
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.loadManifestButton);
        buttonPanel.add(this.saveManifestButton);
        manifestPane.add(buttonPanel);

        return manifestPane;
    }

    /**
     * Generates a stock based on items needing re-order, and then creates a manifest.
     *
     * @throws DeliveryException If the manifest generation failed
     */
    private void optimiseManifests() throws DeliveryException {
        // Create a stock from the items that need restocking
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
        StoreImpl.getInstance().getInventory().getStockedItemQuantities().stream()
                .filter(pair -> pair.getRight() <= pair.getLeft().getReorderPoint())
                .forEach(pair -> stockBuilder.addStockedItem(pair.getLeft(), pair.getLeft().getReorderAmount()));

        // Set the manifest to the optimised manifest
        StoreImpl.getInstance().setManifest(new ManifestOptimiser(stockBuilder.build()).getManifest(), false);

        // Update the tables
        this.manifestPaneTitle.setText("Generated Manifest");
        fillManifestTable(this.manifestTable);
    }

    /**
     * Fills a table with manifest data.
     *
     * @param manifestTable The table to fill
     */
    private void fillManifestTable(JTable manifestTable) {
        // Create column names
        String[] columns = {
                "Type",
                "Cargo Capacity",
                "Cost ($)",
                "Stored Cargo"
        };

        // Transform the Manifest into a data frame for the table model
        Object[][] data = StoreImpl.getInstance().getManifest().getTrucks().stream()
                .map(truck -> new Object[]{
                        truck.getType(),
                        truck.getCargoCapacity(),
                        truck.getCost(),
                        truck.getCargo().getTotalAmount()
                })
                .toArray(Object[][]::new);

        // Set the model on the table
        manifestTable.setModel(new ArrayTableModel(data, columns, false));
    }

    /**
     * Entry point of the application
     *
     * @param args String args, ignored
     */
    public static void main(String[] args) {
        // Create an instance of the GUI
        new SuperMartGui();
    }
}
