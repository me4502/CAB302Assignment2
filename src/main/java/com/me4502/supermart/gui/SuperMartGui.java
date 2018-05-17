package com.me4502.supermart.gui;

import com.me4502.supermart.SuperMartApplication;
import com.me4502.supermart.store.StoreImpl;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Point of entry for the GUI of the SuperMart application.
 *
 * @author Madeline Miller
 */
public class SuperMartGui {

    private SuperMartApplication application;

    // Form
    private JFrame frame;
    private JTabbedPane tabPane;

    // Info pane
    private JPanel infoPane;

    // Inventory pane
    private JPanel inventoryPane;
    private JTable inventoryTable;

    /**
     * Creates a new instance of the GUI.
     */
    private SuperMartGui() {
        // Create an application
        this.application = new SuperMartApplication();

        // Setup form
        this.frame = new JFrame();
        this.frame.setTitle("Store Inventory Management");

        // Setup info pane
        this.infoPane = new JPanel();
        JLabel infoPaneTitle = new JLabel("Store Inventory Management System");
        infoPaneTitle.setFont(new Font("Default", Font.PLAIN, 18));
        this.infoPane.add(infoPaneTitle);
        this.infoPane.add(new JLabel("Store: " + StoreImpl.getInstance().getName()));
        this.infoPane.add(new JLabel("Capital: " + StoreImpl.getInstance().getFormattedCapital()));

        // Setup inventory pane
        this.inventoryPane = new JPanel();
        this.inventoryPane.add(new JLabel("Stored Inventory"));
        this.inventoryTable = new JTable();
        fillInventoryTable();
        this.inventoryPane.add(this.inventoryTable);

        this.tabPane = new JTabbedPane();
        this.tabPane.addTab("Info", this.infoPane);
        this.tabPane.addTab("Inventory", this.inventoryPane);
        this.frame.add(this.tabPane);

        this.frame.setVisible(true);
    }

    private void fillInventoryTable() {
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
        Object[][] data = StoreImpl.getInstance().getInventory().getStockedItemQuantities().stream()
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
        this.inventoryTable.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return data.length;
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return data[rowIndex][columnIndex];
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                // This is a read-only table
                return false;
            }
        });
    }

    public static void main(String[] args) {
        new SuperMartGui();
    }
}
