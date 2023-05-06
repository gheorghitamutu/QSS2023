package org.application.presentation.swing;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeslotSettings implements BaseSettings {
    public TimeslotSettings(){

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
        JTextField regField = new JTextField(20);
        JPanel regPanel = createFieldPanel(regLabel, regField);
        currentPanel.add(regPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonDelete = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                System.out.println("Delete room btn clicked");
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

        JLabel titleLabel = new JLabel("ADD TIMESLOT");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel);
        // Add the start date field
        JLabel startDateLabel = new JLabel("Start Date:");
        startDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        startDateLabel.setBackground(Color.decode("#617A55"));
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("##/##/####");
            formatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JFormattedTextField startDateField = new JFormattedTextField(formatter);
        startDateField.setColumns(10);

        JPanel namePanel = createFieldPanel(startDateLabel, startDateField);
        currentPanel.add(namePanel);
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add the end date field
        JLabel endDateLabel = new JLabel("End Date:");
        startDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        startDateLabel.setBackground(Color.decode("#617A55"));
        MaskFormatter endformatter = null;
        try {
            endformatter = new MaskFormatter("##/##/####");
            endformatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JFormattedTextField endDateField = new JFormattedTextField(endformatter);
        endDateField.setColumns(10);

        JPanel endDatePanel = createFieldPanel(endDateLabel, endDateField);
        currentPanel.add(endDatePanel);
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // add time
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setBackground(Color.decode("#617A55"));

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        JFormattedTextField  timeField = new JFormattedTextField(format);
        try {
            MaskFormatter mf = new MaskFormatter("##:##");
            mf.install(timeField);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JPanel timePanel = createFieldPanel(timeLabel, timeField);
        currentPanel.add(timePanel);
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        //Add timespan field
        JLabel timespanLabel = new JLabel("Timespan:");
        timespanLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timespanLabel.setBackground(Color.decode("#617A55"));
        JTextField timespanField = new JTextField(20);
        JPanel regPanel = createFieldPanel(timespanLabel, timespanField);
        currentPanel.add(regPanel);

        //Add day field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel dayLabel = new JLabel("Group:");
        dayLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dayLabel.setBackground(Color.decode("#617A55"));
        String[] dayValues = { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        JComboBox<String> dayComboBox = new JComboBox<>(dayValues);
        JPanel dayPanel = createFieldPanel(dayLabel, dayComboBox);
        currentPanel.add(dayPanel);


        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                System.out.println("Add room btn clicked");
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

        ImageIcon image = new ImageIcon("D:\\Desktop\\MASTER\\Semestru2\\CSS\\QSS2023\\Project\\SchoolTimetable\\src\\main\\java\\org\\application\\presentation\\icons\\room.png");
        Image rescaledImage = image.getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(0,30,300,300);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
