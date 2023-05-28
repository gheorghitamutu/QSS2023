package org.presentation;

import com.google.inject.ConfigurationException;
import org.GuiceInjectorSingleton;
import org.Application;
import org.Main;
import org.presentation.swing.*;
import org.presentation.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
/**
 * The GUI class represents the main graphical user interface for the Timetable Generator application.
 * It extends JFrame and implements the ActionListener interface to handle button click events.
 * The GUI displays various panels based on user interaction and provides navigation functionality.
 */
public class GUI extends JFrame implements ActionListener {

    /**
     * The Application instance used for accessing the application's services.
     */
    public static Application app;

    /**
     * The main panel of the GUI.
     */
    private JPanel mainPanel;

    /**
     * The left panel of the GUI.
     */
    private JPanel navPanel;

    /**
     * The right panel of the GUI.
     */
    private JButton exitButton;

    /**
     * The category buttons of the GUI.
     */
    private JButton[] categoryButtons;

    /**
     * The background color of the GUI (1).
     */
    private Color bgColor1 = Color.decode("#AAC8A7");

    /**
     * The background color of the GUI (2).
     */
    private Color bgColor2 = Color.decode("#F6FFDE");

    /**
     * The background color of the GUI (3).
     */
    private Color bgColor3 = Color.decode("#C9DBB2");

    /**
     * Sets up the application by initializing the dependency injection framework and creating an instance of the Application class.
     * This method should be called at the beginning of the application to configure the necessary dependencies and obtain the Application instance.
     *
     * @param isTest a boolean value indicating whether the application is running in a test environment
     *
     * @throws ConfigurationException if there is an error in the dependency configuration
     */
    public static void setUpAll(boolean isTest){
        var appInjector = Main.setupDependenciesInjector(isTest);
        app = appInjector.getInstance(Application.class);
        GuiceInjectorSingleton.INSTANCE.setInjector(appInjector);
    }

    /**
     * Creates a GUI for the Timetable Generator application.
     * Initializes the main frame, main panel, navigation panel, category buttons, and exit button.
     * Sets the layout, size, and appearance of the GUI components.
     * Registers event listeners and handles user interactions.
     */
    public GUI() {
        super("Timetable Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create main panel
        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel = createMain(mainPanel);
        add(mainPanel, BorderLayout.CENTER);


        // Create category buttons for the navigation
        String[] categories = {"Home","Students", "Teachers", "Disciplines", "Sessions", "Rooms", "Timeslots"};
        navPanel = new JPanel();
        // Create navigation panel
        navPanel.setLayout(new GridLayout(categories.length + 1, 1));
        navPanel.setBackground(bgColor1);
        add(navPanel, BorderLayout.WEST);

        categoryButtons = new JButton[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryButtons[i] = new JButton(categories[i]);
            categoryButtons[i].setBorderPainted(false);
            categoryButtons[i].setFocusPainted(false);
            categoryButtons[i].setBackground(bgColor1);
            categoryButtons[i].setForeground(Color.decode("#617A55"));
            categoryButtons[i].setFont(new Font("Arial", Font.BOLD, 19));
            categoryButtons[i].setPreferredSize(new Dimension(250, 50));
            categoryButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            categoryButtons[i].addActionListener(this);
            categoryButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    ((JButton)evt.getSource()).setBackground(bgColor2);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    ((JButton)evt.getSource()).setBackground(bgColor1);
                }
            });
            navPanel.add(categoryButtons[i]);
        }

        // Create exit button
        exitButton = new JButton("Exit");
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setBackground(bgColor1);
        exitButton.setForeground(bgColor3);
        exitButton.setFont(new Font("Arial", Font.BOLD, 23));
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        navPanel.add(exitButton);

        // Set main panel visible
        setResizable(false);
        setVisible(true);
    }

    /**
     * Creates the main panel of the GUI.
     * Removes previous components from the right panel and adds a result label.
     * Sets the size and background color of the main panel.
     * Adds an image to the panel using an ImageIcon.
     *
     * @param main the existing main panel to be modified
     * @return the modified main panel with the added image
     */
    public JPanel createMain(JPanel main){
        // Remove previous components from right panel and add result label
        main.removeAll();
        setSize(800, 600);
        main.setBackground(new Color(0x252F23));
        ImageIcon image = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/icons/logo.png")));
        Image rescaledImage = image.getImage().getScaledInstance(450,450, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(30,20,350,350);
        main.add(imageLabel, BorderLayout.CENTER);
        return main;
    }

    /**
     * Handles the actionPerformed event for buttons in the GUI.
     * Determines the source of the event and performs specific actions based on the button's text.
     * Creates and updates the main panel of the GUI based on the selected button.
     * Refreshes and repaints the main panel.
     *
     * @param e the ActionEvent representing the button click event
     */
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if(source.getText().equals("Students")){
            StudentSettings studentSettings = new StudentSettings();
            mainPanel = studentSettings.createJPanel(mainPanel, source.getText());

        } else  if (source.getText().equals("Home")){
            mainPanel = createMain(mainPanel);
        } else if (source.getText().equals("Teachers")){
            TeacherSettings teacherSettings = new TeacherSettings();
            mainPanel = teacherSettings.createJPanel(mainPanel, source.getText());
        } else if (source.getText().equals("Sessions")){
            SessionSettings sessionSettings = new SessionSettings();
            mainPanel = sessionSettings.createJPanel(mainPanel, source.getText());
        } else if(source.getText().equals("Disciplines")){
            DisciplineSettings disciplineSettings = new DisciplineSettings();
            mainPanel =disciplineSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Rooms")){
            RoomSettings roomSettings = new RoomSettings();
            mainPanel = roomSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Timeslots")) {
            TimeslotSettings timeslotSettings = new TimeslotSettings();
            mainPanel =timeslotSettings.createJPanel(mainPanel, source.getText());
        }

        pack();
        // Refresh panel
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    /**
     * Main method for the Timetable Generator application.
     * Creates a GUI for the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setUpAll(false);
        GUI gui = new GUI();
    }
}
