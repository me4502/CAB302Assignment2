package com.me4502.supermart.gui;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.Store;
import com.me4502.supermart.store.StoreImpl;

import java.awt.Font;
import java.io.File;

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

    private SuperMartApplication application;
    private Store store;

    // Form
    private JFrame frame;

    /**
     * Creates a new instance of the GUI.
     */
    private SuperMartGui() {
        // Create an application and store
        this.application = new SuperMartApplication();
        this.store = new StoreImpl("SuperMart");

        // Setup form
        this.frame = new JFrame();
        this.frame.setTitle("Store Inventory Management");

        // Add panes to tab menu
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Info", createInfoPane());
        tabPane.addTab("Inventory", createInventoryPane());
        tabPane.addTab("Sales Log", createSalesLogPane());
        tabPane.addTab("Manifest", createManifestPane());
        this.frame.add(tabPane);

        this.frame.setVisible(true);
        this.frame.setSize(750, 500);
    }

    private JPanel createInfoPane() {
        // Setup info pane
        JPanel infoPane = new JPanel();
        infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
        JLabel infoPaneTitle = new JLabel("Store Inventory Management System", SwingConstants.CENTER);
        infoPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        infoPane.add(infoPaneTitle);
        infoPane.add(new JLabel("Store: " + StoreImpl.getInstance().getName()));
        infoPane.add(new JLabel("Capital: " + StoreImpl.getInstance().getFormattedCapital()));

        return infoPane;
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
        JButton loadInventoryButton = new JButton("Load Inventory");
        loadInventoryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // TODO Once CSV is implemented read in the file
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a file to load inventory!");
            }
        });
        inventoryPane.add(loadInventoryButton);

        return inventoryPane;
    }

    private void fillInventoryTable(JTable inventoryTable) {
        // Create column names
        String[] columns = new String[] {
                "Item",
                "Quantity",
                "Manufacturing cost ($)",
                "Sell price ($)",
                "Reorder point",
                "Reorder amount",
                "Temperature (°C)"
        };

        // Transform the Stock into a data frame for the table model
        Object[][] data = this.store.getInventory().getStockedItemQuantities().stream()
                .map(pair -> new Object[]{
                        pair.getLeft().getName(),
                        pair.getRight(),
                        pair.getLeft().getManufacturingCost(),
                        pair.getLeft().getSellPrice(),
                        pair.getLeft().getReorderPoint(),
                        pair.getLeft().getReorderAmount(),
                        pair.getLeft().getIdealTemperature().orElse(0.0) // TODO Make this correct
                })
                .toArray(Object[][]::new);

        // Set the model of the table
        inventoryTable.setModel(new ArrayTableModel(data, columns, false));
    }

    private JPanel createSalesLogPane() {
        // Sales log pane
        JPanel salesLogPane = new JPanel();
        salesLogPane.setLayout(new BoxLayout(salesLogPane, BoxLayout.Y_AXIS));
        JLabel salesLogPaneTitle = new JLabel("Sales Log");
        salesLogPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        salesLogPane.add(salesLogPaneTitle);
        JTable salesLogTable = new JTable();
        fillSalesLogTable(salesLogTable);
        salesLogPane.add(new JScrollPane(salesLogTable));
        JButton loadSalesLogButton = new JButton("Load Sales Log");
        loadSalesLogButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // TODO Once CSV is implemented read in the file
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a file to load sales log!");
            }
        });
        salesLogPane.add(loadSalesLogButton);

        return salesLogPane;
    }

    private void fillSalesLogTable(JTable salesLogTable) {
        // TODO
    }

    private JPanel createManifestPane() {
        JPanel manifestPane = new JPanel();
        manifestPane.setLayout(new BoxLayout(manifestPane, BoxLayout.Y_AXIS));
        JLabel manifestPaneTitle = new JLabel("Manifest");
        manifestPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        manifestPane.add(manifestPaneTitle);
        JTable manifestTable = new JTable();
        fillManifestTable(manifestTable);
        manifestPane.add(new JScrollPane(manifestTable));
        JButton loadManifestButton = new JButton("Load Manifests");
        loadManifestButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // TODO Once CSV is implemented read in the file
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a file to load manifests!");
            }
        });
        JButton optimiseManifestButton = new JButton("Optimise Manifests");
        JButton saveManifestButton = new JButton("Save Manifests");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadManifestButton);
        buttonPanel.add(optimiseManifestButton);
        buttonPanel.add(saveManifestButton);
        manifestPane.add(buttonPanel);
        return manifestPane;
    }

    private void fillManifestTable(JTable manifestTable) {
        // TODO
    }

    public static void main(String[] args) {
        new SuperMartGui();
    }
}
