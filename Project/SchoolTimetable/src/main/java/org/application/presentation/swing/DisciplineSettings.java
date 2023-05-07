package org.application.presentation.swing;

import org.application.domain.exceptions.discipline.DisciplineAdditionException;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.application.domain.exceptions.teacher.TeacherAdditionException;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DisciplineSettings implements BaseSettings {
    public DisciplineSettings(){

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
    private void addTeacherToDisciplineForm(JPanel currentPanel){

        JLabel titleLabel1 = new JLabel("ADD TEACHER TO DISCIPLINE");
        titleLabel1.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel1.setForeground(Color.decode("#617A55"));
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel1);

        //add techer to session
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel disciplineLabel = new JLabel("Discipline:");
        disciplineLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        disciplineLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> disciplineSelector = new JComboBox<>();


        var allDisciplines = GUI.app.disciplinesService.getDisciplines();
        for (var d : allDisciplines) {
            disciplineSelector.addItem(d.getName());
        }
        JPanel disciplinePanel = createFieldPanel(disciplineLabel, disciplineSelector);
        currentPanel.add(disciplinePanel);



        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel teacherLabel = new JLabel("Teacher:");
        teacherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        teacherLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> teacherSelector = new JComboBox<>();

        // get all the disciplines from db
        var allTeacher = GUI.app.teachersService.getTeachers();
        for (var t : allTeacher) {
            teacherSelector.addItem(t.getName());
        }
        JPanel teacherPanel = createFieldPanel(teacherLabel, teacherSelector);
        currentPanel.add(teacherPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton teacherBtn = new JButton("Submit");
        JPanel teacherButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        teacherButtonPanel.setBackground(Color.decode("#F6FFDE"));
        teacherBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                System.out.println("ADD TEACHER TO A DISICPLINE");
                try {
                    GUI.app.disciplinesService.addTeacherToDiscipline((String) teacherSelector.getSelectedItem(), (String) disciplineSelector.getSelectedItem());
                } catch (DisciplineNotFoundException | TeacherNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }

            }

        });
        teacherButtonPanel.add(teacherBtn);
        currentPanel.add(teacherButtonPanel);
    }

    public JPanel deleteDisciplineForm(JPanel currentPanel){

        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel titleLabel = new JLabel("DELETE DISCIPLINE");
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
        var allDisciplines = GUI.app.disciplinesService.getDisciplines();
        for (var discipline : allDisciplines) {
            model.addItem(discipline.getName());
        }
        JPanel typePanel = createFieldPanel(regLabel, model);
        currentPanel.add(typePanel);
        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonDelete = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete room btn clicked");
                try {
                    GUI.app.disciplinesService.deleteDisciplines((String) model.getSelectedItem());
                } catch ( DisciplineDeletionFailed ex) {
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

    public JPanel addDisciplineForm(JPanel currentPanel){
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("ADD DISIPLINE");
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

        // Add the credits
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel creditsLabel = new JLabel("Credits:");
        creditsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        creditsLabel.setBackground(Color.decode("#617A55"));
        String[] creditsValues = {"3", "4", "5", "6"};
        JComboBox<String> creditComboBox = new JComboBox<>(creditsValues);
        JPanel creditsPanel = createFieldPanel(creditsLabel, creditComboBox);
        currentPanel.add(creditsPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Insert discipline btn clicked");
                String creditsComboValue = (String) creditComboBox.getSelectedItem();
                try {
                    assert creditsComboValue != null;
                    assert nameField.getText() != null;
                    GUI.app.disciplinesService.addDiscipline(nameField.getText(), Integer.parseInt(creditsComboValue));
                } catch (DisciplineAdditionException ex) {
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

        // ADD TEACHER TO A DISCIPLINE
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addTeacherToDisciplineForm(currentPanel);

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

        currentPanel = addDisciplineForm(currentPanel);
        currentPanel = deleteDisciplineForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }

    @Override
    public JPanel createRightJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(null);
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(DisciplineSettings.class.getResource("/icons/discipline.png")));
        Image rescaledImage = image.getImage().getScaledInstance(300,350, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(20,60,350,350);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
