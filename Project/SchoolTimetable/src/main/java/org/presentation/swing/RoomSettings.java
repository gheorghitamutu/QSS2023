package org.presentation.swing;

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

public class RoomSettings implements BaseSettings {
    public RoomSettings(){

    }
    private JPanel createFieldPanel(JLabel label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#F6FFDE"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(component);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, component.getPreferredSize().height));
        return panel;
    }

    public JPanel deleteRoomForm(JPanel currentPanel){

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
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete room btn clicked");
                try {
                    GUI.app.roomsService.deleteRooms((String) model.getSelectedItem());
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

    public JPanel addRoomForm(JPanel currentPanel){
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
            public void actionPerformed(ActionEvent e) {
                System.out.println("Insert room btn clicked");
                try {
                    assert nameField.getText() != null;
                    GUI.app.roomsService.addRoom(nameField.getText(),
                                                Integer.parseInt((String) Objects.requireNonNull(capacityComboBox.getSelectedItem())),
                                                Integer.parseInt((String) Objects.requireNonNull(floorComboBox.getSelectedItem())),
                                                Room.Type.valueOf((String) typeComboBox.getSelectedItem()));
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
    @Override
    public JPanel createJPanel(JPanel main, String labelText) {
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

    @Override
    public JPanel createLeftJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));

        currentPanel = addRoomForm(currentPanel);
        currentPanel = deleteRoomForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }

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
