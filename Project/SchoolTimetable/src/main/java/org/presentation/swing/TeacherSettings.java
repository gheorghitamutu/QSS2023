package org.presentation.swing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;
import org.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
/**
 * Provides settings and functionality for managing teachers.
 * This class implements the {@link BaseSettings} interface.
 * It allows adding and deleting teachers through user interfaces.
 */
public class TeacherSettings implements BaseSettings {

    /**
     * Default constructor for the TeacherSettings class.
     */
    public TeacherSettings() {
    }

    /**
     * Creates a JPanel containing a form for adding a teacher.
     * @param label The label of the form. Must not be empty.
     * @param component The component to be added to the form. Must not be empty.
     * @return The JPanel containing the form.
     */
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

    /**
     * Displays a form for deleting a teacher.
     *
     * @param currentPanel The JPanel on which the form will be displayed. Must not be empty.
     * @return The modified currentPanel with the teacher deletion form added.
     * @throws RuntimeException if an exception occurs during the teacher deletion process.
     * Call the teachersService component in order to perform the operation on database side.
     */
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
                } catch (TeacherDeletionFailed | ValidationException ex) {
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
     * Displays a form for adding a new teacher.
     *
     * @param currentPanel The JPanel on which the form will be displayed. Must not be empty.
     * @return The modified currentPanel with the teacher form added.
     * @throws RuntimeException if an exception occurs during the teacher addition process.
     */
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
                } catch (TeacherAdditionException | ValidationException ex) {
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

    /**
     * Creates a left JPanel containing the addTeacherForm and deleteTeacherForm.
     *
     * @return The left JPanel containing the addTeacherForm and deleteTeacherForm.
     */
    @Override
    public JPanel createLeftJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));

        currentPanel = addTeacherForm(currentPanel);
        currentPanel = deleteTeacherForm(currentPanel);
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

        ImageIcon image = new ImageIcon(Objects.requireNonNull(TeacherSettings.class.getResource("/icons/teacher.png")));
        Image rescaledImage = image.getImage().getScaledInstance(350,350, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(30,20,350,350);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
