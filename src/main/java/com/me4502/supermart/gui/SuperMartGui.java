package com.me4502.supermart.gui;

import com.me4502.supermart.SuperMartApplication;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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

    /**
     * Creates a new instance of the GUI.
     */
    private SuperMartGui() {
        // Create an application
        this.application = new SuperMartApplication();

        // Setup form
        this.frame = new JFrame();

        // Setup info pane
        this.infoPane = new JPanel();

        this.tabPane = new JTabbedPane();
        this.tabPane.addTab("Info", this.infoPane);
        this.frame.add(this.tabPane);

        this.frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SuperMartGui();
    }
}
