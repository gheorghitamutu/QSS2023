package org.application.presentation;

import org.application.presentation.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener {
    // Define variables
    private JPanel mainPanel, navPanel;
    private JButton exitButton;
    private JButton[] categoryButtons;
    private Color bgColor1 = Color.decode("#AAC8A7");
    private Color bgColor2 = Color.decode("#F6FFDE");
    private Color bgColor3 = Color.decode("#C9DBB2");

    public GUI() {
        super("Timetable Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Create navigation panel
        navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.PAGE_AXIS));
        navPanel.setBackground(bgColor1);
        add(navPanel, BorderLayout.WEST);

        // Create category buttons for the navigation
        String[] categories = {"Students", "Teachers", "Disciplines", "Rooms", "Timeslots", "Generate TimeTable"};
        categoryButtons = new JButton[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryButtons[i] = new JButton(categories[i]);
            categoryButtons[i].setBorderPainted(false);
            categoryButtons[i].setFocusPainted(false);
            categoryButtons[i].setBackground(bgColor1);
            categoryButtons[i].setForeground(bgColor3);
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
            navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
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
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Open new frame
//        JFrame newFrame = new JFrame("New Frame");
//        newFrame.setSize(500, 400);
//        newFrame.setLocationRelativeTo(null);
//        newFrame.setVisible(true);
        // Get the source of the event

        JButton source = (JButton) e.getSource();
        JLabel resultLabel = new JLabel();
        if(source.getText().equals("Students")){
            StudentSettings studentSettings = new StudentSettings();
            studentSettings.createJPanel(mainPanel, source.getText());
        } else if (source.getText().equals("Teachers")){
            TeacherSettings teacherSettings = new TeacherSettings();
            teacherSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Disciplines")){
            DisciplineSettings disciplineSettings = new DisciplineSettings();
            disciplineSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Rooms")){
            RoomSettings roomSettings = new RoomSettings();
            roomSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Timeslots")) {
            TimeslotSettings timeslotSettings = new TimeslotSettings();
            timeslotSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Generate Timetable")){
             //TODO TimeTable Generator Btn link here
        }


        // Remove previous components from right panel and add result label
        //mainPanel.removeAll();
        //mainPanel.add(resultLabel);

        // Refresh right panel
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}
