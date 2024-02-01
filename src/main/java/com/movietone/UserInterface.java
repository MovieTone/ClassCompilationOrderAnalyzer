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
 * UserInterface class represents GUI for the program, the aim of which is to build a class dependency graph and sort it in topological order so that one can
 * see a recompilation order
 */
public class UserInterface extends JFrame implements ActionListener {

    private final Graph<String> graph;

    // labels
    private final JLabel inputLabel = new JLabel("Input file name:");
    private final JLabel classLabel = new JLabel("Class to recompile:");

    // text fields
    private final JTextField fileNameTextField = new JTextField(10);
    private final JTextField classNameTextField = new JTextField(10);

    // buttons
    private final JButton buildGraphButton = new JButton("Build Directed Graph");
    private final JButton topoButton = new JButton("Topological Order");

    // panels
    private final JPanel bottomPanel = new JPanel();
    private final JPanel topPanel = new JPanel();
    private final JPanel panel = new JPanel();

    // text area
    private final JTextArea orderTextArea = new JTextArea(10, 40);

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

        // sets a panel
        setPanel();

        // adds listeners to the buttons
        addListeners();

        pack();
    }

    /**
     * Starts the program and runs GUI
     *
     * @param args no console params
     */
    public static void main(String[] args) {
        UserInterface gui = new UserInterface();
        gui.setVisible(true);
    }

    /**
     * Handles buttons
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
     * Sets the main panel
     */
    private void setPanel() {
        // sets the bottom panel
        setBottomPanel();

        // sets the top panel
        setTopPanel();

        // sets main panel
        panel.add(topPanel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // adds panels to the frame
        add(panel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the bottom panel
     */
    private void setBottomPanel() {
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(orderTextArea);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(RECOMPILATION_ORDER));
    }

    /**
     * Sets the top panel
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
     * Adds listeners to the buttons
     */
    private void addListeners() {
        buildGraphButton.addActionListener(this);
        topoButton.addActionListener(this);
    }

    /**
     * Handles the Topological Order button
     *
     * @throws CycleDetectedException    if cycle is found
     * @throws InvalidClassNameException if the class does not match any class read from the file
     * @throws GraphNotBuiltException    if graph is not built yet
     */
    public void handleTopoButton() throws CycleDetectedException, InvalidClassNameException, GraphNotBuiltException {
        String className = classNameTextField.getText();
        if (className.isEmpty()) {
            JOptionPane.showMessageDialog(this, ENTER_CLASS_NAME);
            return;
        }
        // generates a string of classes in topological order and sets it to the textArea
        orderTextArea.setText(graph.generateTopoString(className));
    }

    /**
     * Handles button Build Graph
     *
     * @throws FileNotFoundException if no file found
     */
    public void handleBuildGraphButton() throws FileNotFoundException {
        String fileName = fileNameTextField.getText();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(this, ENTER_FILE_NAME);
            return;
        }
        // loads a graph from a file and shows a message
        String message = graph.loadGraph(fileName);
        JOptionPane.showMessageDialog(this, message);
    }

}