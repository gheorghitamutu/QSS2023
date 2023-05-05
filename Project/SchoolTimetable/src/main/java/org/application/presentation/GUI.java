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

    public JPanel createMain(JPanel main){
        // Remove previous components from right panel and add result label
        main.removeAll();
        setSize(800, 600);
        main.setBackground(new Color(0x252F23));
        ImageIcon image = new ImageIcon("D:\\Desktop\\MASTER\\Semestru2\\CSS\\QSS2023\\Project\\SchoolTimetable\\src\\main\\java\\org\\application\\presentation\\icons\\logo.png");
        Image rescaledImage = image.getImage().getScaledInstance(450,450, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(30,20,350,350);
        main.add(imageLabel, BorderLayout.CENTER);
        return main;
    }

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
        }else if(source.getText().equals("Disciplines")){
            DisciplineSettings disciplineSettings = new DisciplineSettings();
            mainPanel =disciplineSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Rooms")){
            RoomSettings roomSettings = new RoomSettings();
            mainPanel = roomSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Timeslots")) {
            TimeslotSettings timeslotSettings = new TimeslotSettings();
            mainPanel =timeslotSettings.createJPanel(mainPanel, source.getText());
        }else if(source.getText().equals("Generate Timetable")){
             //TODO TimeTable Generator Btn link here
        }

        pack();
        // Refresh right panel
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}
