package com.movietone;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * P4GUI class represents GUI for the program, the aim of which is to build a class dependency graph and sort it in topological order so that one can
 * see a recompilation order
 */
public class UserInterface extends JFrame implements ActionListener {

    private Graph<String> graph;

    // labels
    private JLabel inputLabel = new JLabel("Input file name:");
    private JLabel classLabel = new JLabel("Class to recompile:");

    // text fields
    private JTextField fileNameTextField = new JTextField(10);
    private JTextField classNameTextField = new JTextField(10);

    // buttons
    private JButton buildGraphButton = new JButton("Build Directed Graph");
    private JButton topoButton = new JButton("Topological Order");

    // panels
    private JPanel bottomPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JPanel panel = new JPanel();

    // text area
    private JTextArea orderTextArea = new JTextArea(10, 40);

    // information and error messages
    private static final String WINDOW_NAME = "Class Dependency Graph";
    private static final String RECOMPILATION_ORDER = "Recompilation Order";
    private static final String ENTER_CLASS_NAME = "Enter The Class Name";
    private static final String ENTER_FILE_NAME = "Enter The File Name";

    public UserInterface() {
        super(WINDOW_NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        orderTextArea.setEditable(false);
        graph = new Graph<>();

        // set panel
        setPanel();

        // add listeners to the buttons
        addListeners();

        pack();
    }

    /**
     * starts the program and runs the GUI
     *
     * @param args console params
     */
    public static void main(String[] args) {
        UserInterface gui = new UserInterface();
        gui.setVisible(true);
    }

    /**
     * handles buttons clicking
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == buildGraphButton) {
                // build graph button clicked
                handleBuildGraphButton();
            } else if (e.getSource() == topoButton) {
                // topological order button clicked
                handleTopoButton();
            }
        } catch (FileNotFoundException | GraphNotBuiltException | InvalidClassNameException
                 | CycleDetectedException e1) {
            JOptionPane.showMessageDialog(this, e1.getMessage());
        }
    }

    /**
     * sets the main panel
     */
    private void setPanel() {
        // set the bottom panel
        setBottomPanel();

        // set the top panel
        setTopPanel();

        // set main panel
        panel.add(topPanel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // add panels to the frame
        add(panel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * sets the bottom panel
     */
    private void setBottomPanel() {
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(orderTextArea);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(RECOMPILATION_ORDER));
    }

    /**
     * sets the top panel
     */
    private void setTopPanel() {
        topPanel.setLayout(new GridLayout(2, 3, 10, 10));
        topPanel.add(inputLabel);
        topPanel.add(fileNameTextField);
        topPanel.add(buildGraphButton);
        topPanel.add(classLabel);
        topPanel.add(classNameTextField);
        topPanel.add(topoButton);
    }

    /**
     * adds listeners to the buttons
     */
    private void addListeners() {
        buildGraphButton.addActionListener(this);
        topoButton.addActionListener(this);
    }

    /**
     * handles button Topological Order
     *
     * @throws CycleDetectedException    if cycle is found
     * @throws InvalidClassNameException if the class does not match any class read from the file
     * @throws GraphNotBuiltException    if graph is not built yet
     */
    public void handleTopoButton() throws CycleDetectedException, InvalidClassNameException, GraphNotBuiltException {
        String className = classNameTextField.getText();
        // if no class name is specified
        if (className.isEmpty()) {
            JOptionPane.showMessageDialog(this, ENTER_CLASS_NAME);
            return;
        }
        // generate a string of classes in topological order and set it to the textArea
        orderTextArea.setText(graph.generateTopoString(className));
    }

    /**
     * handles button Build Graph
     *
     * @throws FileNotFoundException if no file found
     */
    public void handleBuildGraphButton() throws FileNotFoundException {
        String fileName = fileNameTextField.getText();
        // if no file name is specified
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(this, ENTER_FILE_NAME);
            return;
        }
        // load a graph from the file and show a message
        String message = graph.loadGraph(fileName);
        JOptionPane.showMessageDialog(this, message);
    }

}