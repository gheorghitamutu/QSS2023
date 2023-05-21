package org.presentation.swing;

import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.student.StudentNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class StudentSettings extends JFrame implements BaseSettings {
    public StudentSettings(){
    }
    public JPanel addStudentForm(JPanel currentPanel){
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("ADD STUDENT");
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

        // Add the registration ID field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel regLabel = new JLabel("Registration ID:");
        regLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        regLabel.setBackground(Color.decode("#617A55"));
        JTextField regField = new JTextField(20);
        JPanel regPanel = createFieldPanel(regLabel, regField);
        currentPanel.add(regPanel);

        // Add the year of study field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel yearLabel = new JLabel("Year of Study:");
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        yearLabel.setBackground(Color.decode("#617A55"));
        String[] yearValues = {"1", "2", "3"};
        JComboBox<String> yearComboBox = new JComboBox<>(yearValues);
        JPanel yearPanel = createFieldPanel(yearLabel, yearComboBox);
        currentPanel.add(yearPanel);

        // Add the group field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel groupLabel = new JLabel("Group:");
        groupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        groupLabel.setBackground(Color.decode("#617A55"));
        String[] groupValues = {"A1", "A2", "A3", "B1", "B2", "B3", "M1", "M2", "M3"};

        JComboBox<String> groupComboBox = new JComboBox<>(groupValues);
        JPanel groupPanel = createFieldPanel(groupLabel, groupComboBox);
        currentPanel.add(groupPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                System.out.println("Insert student button  clicked");
                String yearComboValue = (String) yearComboBox.getSelectedItem();
                String groupComboValue = (String) groupComboBox.getSelectedItem();
                try {
                    assert yearComboValue != null;
                     GUI.app.studentsService.addStudent(nameField.getText(), regField.getText(), Integer.parseInt(yearComboValue), groupComboValue);

                } catch (StudentAdditionException | ValidationException ex) {
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

    public JPanel deleteStudentForm(JPanel currentPanel){

        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel titleLabel = new JLabel("DELETE STUDENT");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        //titleLabel.setBounds(20,30,300,25);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPanel.add(titleLabel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel regLabel = new JLabel("Registration ID:");
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
                System.out.println("Delete btn clicked");
                try {
                    GUI.app.studentsService.deleteStudent(regField.getText());
                } catch (StudentNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                } catch (StudentDeletionFailed ex) {
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
    @Override
    public JPanel createLeftJPanel(){
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));

        currentPanel = addStudentForm(currentPanel);
        currentPanel = deleteStudentForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }

    @Override
    public JPanel createRightJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(null);
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(StudentSettings.class.getResource("/icons/student.png")));
        Image rescaledImage = image.getImage().getScaledInstance(350,350, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(30,20,350,350);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
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
}
