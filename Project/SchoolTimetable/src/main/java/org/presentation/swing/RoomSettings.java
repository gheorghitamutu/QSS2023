package org.presentation.swing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;
import org.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Represents the settings for managing rooms in the GUI.
 */
public class RoomSettings implements BaseSettings {

    /**
     * Default constructor.
     */
    public RoomSettings() {
    }

    /**
     * Creates a panel with a label and a component.
     *
     * @param label     the label for the field
     * @param component the component to be added
     * @return the panel containing the label and the component
     * It uses a BoxLayout with a horizontal axis to arrange the label and the component in a row.
     * The panel's maximum size is set to allow it to expand horizontally within its container
     * while keeping its preferred height based on the component's preferred size.
     */
    private JPanel createFieldPanel(
            @NotEmpty(message = "Room label should not be empty.")
            JLabel label,
            @NotEmpty (message = "Room component should not be empty.")
            JComponent component) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#F6FFDE"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(component);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, component.getPreferredSize().height));
        return panel;
    }

    /**
     * Creates a panel for deleting a room.
     *
     * @param currentPanel the current panel to which the delete room form is added
     * @return the panel containing the delete room form
     * Get all the rooms from the database and add their names to the combo box.
     * Add vertical spacing below the data panel.
     * Display an error message if room deletion fails.
     */
    public JPanel deleteRoomForm(
            @NotEmpty(message = "Current panel should not be empty.")
            JPanel currentPanel){

        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel titleLabel = new JLabel("DELETE ROOM");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        //titleLabel.setBounds(0,30,300,25);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentPanel.add(titleLabel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel regLabel = new JLabel("Name:");
        regLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        regLabel.setBackground(Color.decode("#617A55"));

        JComboBox<String> model = new JComboBox<>();
        // get all the disciplines from db
        var allRooms = GUI.app.roomsService.getRooms();
        for (var room : allRooms) {
            model.addItem(room.getName());
        }
        JPanel dataPanel = createFieldPanel(regLabel, model);
        currentPanel.add(dataPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonDelete = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(@NotEmpty(message = "Current event should not be empty")
                                        ActionEvent e) {
                System.out.println("Delete room btn clicked");
                try {
                    @NotBlank(message = "Current model should not be blank")
                    String param = (String) model.getSelectedItem();
                    GUI.app.roomsService.deleteRooms(param);
                } catch (RoomDeletionFailed | ValidationException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });


        buttonPanel.add(submitButtonDelete);
        currentPanel.add(buttonPanel);

        return currentPanel;
    }

    /**
     * Creates a panel for adding a new room.
     *
     * @param currentPanel the current panel to which the add room form is added
     * @return the panel containing the add room form
     */
    public JPanel addRoomForm(
            @NotEmpty(message = "Current panel should not be empty.")
            JPanel currentPanel){
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("ADD ROOM");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel);
        // Add the name field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBackground(Color.decode("#617A55"));
        JTextField nameField = new JTextField(20);
        JPanel namePanel = createFieldPanel(nameLabel, nameField);
        currentPanel.add(namePanel);

        // Add the type
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setBackground(Color.decode("#617A55"));
        String[] yearValues = {"COURSE", "LABORATORY"};
        JComboBox<String> typeComboBox = new JComboBox<>(yearValues);
        JPanel typePanel = createFieldPanel(typeLabel, typeComboBox);
        currentPanel.add(typePanel);


        //Add floor number
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel floorLabel = new JLabel("Floor no:");
        floorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        floorLabel.setBackground(Color.decode("#617A55"));
        String[] floorValues = {"1", "2", "3", "4"};
        JComboBox<String> floorComboBox = new JComboBox<>(floorValues);
        JPanel floorPanel = createFieldPanel(floorLabel, floorComboBox);
        currentPanel.add(floorPanel);

        // Add the type
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel capacityLabel = new JLabel("Capacity:");
        capacityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        capacityLabel.setBackground(Color.decode("#617A55"));
        String[] capacityValues = {"30", "50", "100", "200"};
        JComboBox<String> capacityComboBox = new JComboBox<>(capacityValues);
        JPanel capacityPanel = createFieldPanel(capacityLabel, capacityComboBox);
        currentPanel.add(capacityPanel);


        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(@NotEmpty(message = "Current event should not be empty.")
                                        ActionEvent e) {
                System.out.println("Insert room btn clicked");
                try {
                    @NotBlank(message = "Room name should not be blank.")
                    String param1 = nameField.getText();
                    GUI.app.roomsService.addRoom(param1,
                                                Integer.parseInt((String) Objects.requireNonNull(capacityComboBox.getSelectedItem(), "Capacity combo selector value should not be blank")),
                                                Integer.parseInt((String) Objects.requireNonNull(floorComboBox.getSelectedItem(), "Floor combo selector value should not be blank")),
                                                Room.Type.valueOf((String) Objects.requireNonNull(typeComboBox.getSelectedItem(), "Type combo selector value should not be blank")));
                } catch (RoomAdditionException | ValidationException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(submitButtonAdd);
        currentPanel.add(buttonPanel);

        return currentPanel;
    }

    /**
     * Creates a JPanel with left and right panels merged into the main panel.
     *
     * @param main      The JPanel representing the main panel. Must not be empty.
     * @param labelText The text for the label. Must not be blank.
     * @return The JPanel with the left and right panels merged into the main panel.
     */
    @Override
    public JPanel createJPanel(
            @NotEmpty(message = "Main panel should not be empty.")
            JPanel main,
            @NotBlank(message = "Current text label should not be empty.")
            String labelText) {
        // Remove previous components from right panel and add result label
        main.removeAll();
        // create insertPanel
        JPanel leftPanel = createLeftJPanel();
        // create deletePanel
        JPanel rightPanel = createRightJPanel();

        //merge the 2 panelS into mainPanel
        main.add(leftPanel, BorderLayout.WEST);
        main.add(rightPanel, BorderLayout.EAST);

        return main;
    }

    /**
     * Creates a left JPanel containing the addDisciplineForm and deleteDisciplineForm.
     *
     * @return The left JPanel containing the addDisciplineForm and deleteDisciplineForm.
     */
    @Override
    public JPanel createLeftJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));

        currentPanel = addRoomForm(currentPanel);
        currentPanel = deleteRoomForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }
    
    /**
     * Creates a right JPanel with an image displayed at the center.
     *
     * @return The right JPanel with the image displayed at the center.
     */
    @Override
    public JPanel createRightJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(null);
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(RoomSettings.class.getResource("/icons/room.png")));
        Image rescaledImage = image.getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(0,30,300,300);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
