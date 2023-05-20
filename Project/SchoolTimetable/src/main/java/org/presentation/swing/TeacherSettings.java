package org.presentation.swing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.Teacher;
import org.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class TeacherSettings implements BaseSettings {
    public TeacherSettings(){

    }
    private JPanel createFieldPanel( @NotEmpty(message = "Teacher label should not be empty.")
                                     JLabel label,
                                     @NotEmpty (message = "Teacher component should not be empty.")
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
    public JPanel deleteTeacherForm(@NotEmpty(message = "Current panel must not be empty.") JPanel currentPanel){

        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel titleLabel = new JLabel("DELETE TEACHER");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        //titleLabel.setBounds(20,30,300,25);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPanel.add(titleLabel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel regLabel = new JLabel("Data:");
        regLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        regLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> model = new JComboBox<>();

        // get all the teachers from db
        var allTeachers = GUI.app.teachersService.getTeachers();
        for (var teacher : allTeachers) {
           model.addItem(teacher.getName());
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
                System.out.println("Delete teacher btn clicked");
                try {
                    GUI.app.teachersService.deleteTeachers((String) model.getSelectedItem());
                } catch (TeacherDeletionFailed ex) {
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

    public JPanel addTeacherForm(@NotEmpty(message = "Current panel must not be empty.") JPanel currentPanel){
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("ADD TEACHER");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel);
        // Add the name field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBackground(Color.decode("#617A55"));
        JTextField nameField = new JTextField(20);
        JPanel namePanel = createFieldPanel(nameLabel, nameField);
        currentPanel.add(namePanel);

        // Add the type of teacher
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setBackground(Color.decode("#617A55"));
        String[] yearValues = {"TEACHER", "COLLABORATOR"};
        JComboBox<String> typeComboBox = new JComboBox<>(yearValues);
        JPanel typePanel = createFieldPanel(typeLabel, typeComboBox);
        currentPanel.add(typePanel);


        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Insert - teacher - btn - clicked");
                Teacher.@NotEmpty(message = "Teacher type must not be empty.") Type type = Teacher.Type.valueOf((String) typeComboBox.getSelectedItem());

                try {
                    GUI.app.teachersService.addTeacher(
                            Objects.requireNonNull(nameField.getText(), "Teacher name combo selector value must not be empty."),
                            type);
                } catch (TeacherAdditionException ex) {
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
    public JPanel createJPanel(@NotEmpty(message = "Main panel should not be empty.")
                                   JPanel main,
                               @NotBlank(message = "Label text should not be blank.")
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

    @Override
    public JPanel createLeftJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));

        currentPanel = addTeacherForm(currentPanel);
        currentPanel = deleteTeacherForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }

    @Override
    public JPanel createRightJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(null);
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(TeacherSettings.class.getResource("/icons/teacher.png")));
        Image rescaledImage = image.getImage().getScaledInstance(350,350, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(30,20,350,350);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
