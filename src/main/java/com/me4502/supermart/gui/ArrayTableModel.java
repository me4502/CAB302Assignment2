package com.me4502.supermart.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * A {@link TableModel} that allows using arrays of data and columns.
 *
 * @author Madeline Miller
 */
public class ArrayTableModel extends AbstractTableModel {

    private Object[][] data;
    private String[] columns;
    private boolean editable;

    /**
     * Creates a ArrayTableModel, using a two-dimensional data array, a column array,
     * and whether the table is editable.
     *
     * @param data The data array, this must have the same width as the column argument
     * @param columns The columns
     * @param editable If it's editable
     */
    public ArrayTableModel(Object[][] data, String[] columns, boolean editable) {
        // Ensure that the data size matches the columns.
        if (data.length > 0 && data[0].length != columns.length) {
            throw new IllegalArgumentException("Data must have the same width as the columns");
        }
        this.data = data;
        this.columns = columns;
        this.editable = editable;
    }

    @Override
    public int getRowCount() {
        return this.data.length;
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return this.columns[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.editable;
    }
}
