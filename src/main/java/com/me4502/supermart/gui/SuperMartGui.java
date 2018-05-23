package com.me4502.supermart.gui;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.csv.CSV;
import com.me4502.supermart.exception.CSVFormatException;
import com.me4502.supermart.exception.DeliveryException;
import com.me4502.supermart.exception.StockException;
import com.me4502.supermart.store.Stock;
import com.me4502.supermart.store.Store;
import com.me4502.supermart.store.StoreImpl;
import com.me4502.supermart.truck.ManifestOptimiser;

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

    private Store store;

    // Form
    private JFrame frame;

    private JLabel capitalLabel;
    private JTable manifestTable;

    /**
     * Creates a new instance of the GUI.
     */
    private SuperMartGui() {
        // Create an application and store
        new SuperMartApplication();
        this.store = new StoreImpl("SuperMart");

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

    private void setCapitalLabel() {
        this.capitalLabel.setText("Capital: " + StoreImpl.getInstance().getFormattedCapital());
    }

    private JPanel createInventoryPane() {
        JPanel inventoryPane = new JPanel();
        inventoryPane.setLayout(new BoxLayout(inventoryPane, BoxLayout.Y_AXIS));
        JLabel inventoryPaneTitle = new JLabel("Stored Inventory");
        inventoryPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        inventoryPane.add(inventoryPaneTitle);
        JTable inventoryTable = new JTable();
        fillInventoryTable(inventoryTable);
        inventoryPane.add(new JScrollPane(inventoryTable));
        JButton loadInventoryButton = new JButton("Load Item Properties");
        loadInventoryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.loadItemProperties(file);
                    optimiseManifests();
                    setCapitalLabel();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                } catch (CSVFormatException | DeliveryException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage());
                }
                fillInventoryTable(inventoryTable);
            }
        });
        JButton loadSalesLogButton = new JButton("Load Sales Log");
        loadSalesLogButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.loadSalesLog(file);
                    optimiseManifests();
                    setCapitalLabel();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                } catch (CSVFormatException | StockException | DeliveryException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage());
                }
                fillInventoryTable(inventoryTable);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadInventoryButton);
        buttonPanel.add(loadSalesLogButton);
        inventoryPane.add(buttonPanel);

        return inventoryPane;
    }

    private void fillInventoryTable(JTable inventoryTable) {
        // Create column names
        String[] columns = new String[]{
                "Item",
                "Quantity",
                "Manufacturing cost ($)",
                "Sell price ($)",
                "Reorder point",
                "Reorder amount",
                "Temperature (°C)"
        };

        // Transform the Stock into a data frame for the table model
        Object[][] data = this.store.getItems().stream()
                .map(item -> new Object[]{
                        item.getName(),
                        this.store.getInventory().getItemQuantity(item).orElse(0),
                        item.getManufacturingCost(),
                        item.getSellPrice(),
                        item.getReorderPoint(),
                        item.getReorderAmount(),
                        item.getIdealTemperature().isPresent() ? item.getIdealTemperature().getAsDouble() : "Dry"
                })
                .toArray(Object[][]::new);

        // Set the model of the table
        inventoryTable.setModel(new ArrayTableModel(data, columns, false));
    }

    private JPanel createManifestPane() {
        JPanel manifestPane = new JPanel();
        manifestPane.setLayout(new BoxLayout(manifestPane, BoxLayout.Y_AXIS));
        JLabel manifestPaneTitle = new JLabel("Manifest");
        manifestPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        manifestPane.add(manifestPaneTitle);
        this.manifestTable = new JTable();
        fillManifestTable(this.manifestTable);
        manifestPane.add(new JScrollPane(this.manifestTable));
        JButton loadManifestButton = new JButton("Load Manifests");
        loadManifestButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.loadManifest(file);
                    setCapitalLabel();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                } catch (CSVFormatException | DeliveryException e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage());
                }
                fillManifestTable(this.manifestTable);
            }
        });
        JButton saveManifestButton = new JButton("Save Manifests");
        saveManifestButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showSaveDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    CSV.exportManifest(file, StoreImpl.getInstance().getManifest());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Failed to save the file: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadManifestButton);
        buttonPanel.add(saveManifestButton);
        manifestPane.add(buttonPanel);
        return manifestPane;
    }

    private void optimiseManifests() throws DeliveryException {
        Stock.Builder stockBuilder = SuperMartApplication.getInstance().getStockBuilder();
        this.store.getInventory().getStockedItemQuantities().stream()
                .filter(pair -> pair.getRight() <= pair.getLeft().getReorderPoint())
                .forEach(pair -> stockBuilder.addStockedItem(pair.getLeft(), pair.getLeft().getReorderAmount()));
        this.store.setManifest(new ManifestOptimiser(stockBuilder.build()).getManifest());
        fillManifestTable(this.manifestTable);
    }

    private void fillManifestTable(JTable manifestTable) {
        // Create column names
        String[] columns = new String[]{
                "Type",
                "Cargo Capacity",
                "Cost ($)",
                "Stored Cargo"
        };

        // Transform the Manifest into a data frame for the table model
        Object[][] data = this.store.getManifest().getTrucks().stream()
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

    public static void main(String[] args) {
        new SuperMartGui();
    }
}
