package org.presentation.swing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Represents a class that contains the UI components and logic for the discipline settings.
 * This class extends the BaseSettings class and implements the BaseSettings interface.
 * This class is used to display the discipline settings form and handle the user input.
 */
public class DisciplineSettings implements BaseSettings {

    /**
     * The main panel of the discipline settings.
     */
    public DisciplineSettings(){

    }
    /**
     * Creates a JPanel that combines a label and a component for a discipline field.
     *
     * @param label     The JLabel representing the discipline label.
     *                  Cannot be null.
     * @param component The JComponent representing the discipline component.
     *                  Must not be empty.
     * @return The JPanel containing the label and component.
     */

    private JPanel createFieldPanel(
            @NotNull(message = "Discipline label should not be null")
            JLabel label,
            @NotEmpty(message = "Discipline component must not be empty.")
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
     * Adds a teacher to a discipline form.
     *
     * @param currentPanel The JPanel representing the current panel where the form will be added.
     *                     Must not be empty.
     * This method adds a teacher to a discipline form by creating and configuring the necessary UI components such as
     *                     labels, dropdown menus, and a submit button, and adds them to the provided JPanel.
     * Error handling is implemented to display an error message if any exceptions occur during the process.
     */
    private void addTeacherToDisciplineForm(
            @NotEmpty(message = "Teacher to discipline label form must not be empty")
            JPanel currentPanel){

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
                System.out.println("ADD TEACHER TO A DISICPLINE");
                try {
                    @NotBlank(message= "Teacher name should not be blank.")
                    String param1 = (String) teacherSelector.getSelectedItem();
                    @NotBlank(message = "Discipline name should not be blank.")
                    String param2 = (String) disciplineSelector.getSelectedItem();
                    GUI.app.disciplinesService.addTeacherToDiscipline(param1, param2);
                }catch (DisciplineNotFoundException | TeacherNotFoundException ex) {
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
    /**
     * Creates a form for deleting a discipline from the database on GUI side.
     *
     * @param currentPanel The JPanel representing the current panel where the form will be added.
     *                     Must not be empty.
     * @return The JPanel with the delete discipline form.
     * Create a dropdown menu (JComboBox) to select the discipline name
     * Create a field panel that combines the discipline name label and the dropdown menu.
     * Call the relevant service method to delete the discipline.
     */
    public JPanel deleteDisciplineForm(
            @NotEmpty(message = "Delete discipline panel should not be empty")
            JPanel currentPanel){

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
                    @NotBlank(message = "Current model should not be empty")
                    String param = (String) model.getSelectedItem();

                    GUI.app.disciplinesService.deleteDisciplines(param);
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
    /**
     * Creates a form for adding a discipline.
     *
     * @param currentPanel The JPanel representing the current panel where the form will be added.
     *                     Must not be empty.
     * @return The JPanel with the add discipline form.
     * Get the discipline name and credits value from a ComboBox Selector.
     * Call the relevant service method to add the discipline.
     * Display an error message if the addition fails
     */
    public JPanel addDisciplineForm(
            @NotEmpty(message = "Add Disicipline panel must not be empty.")
            JPanel currentPanel){
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
                @NotNull(message = "The combo value selected should not be null.")
                String creditsComboValue = (String) creditComboBox.getSelectedItem();
                try {
                    @NotBlank(message = "Disicpline name should not be blank.")
                    String param1 = nameField.getText();
                    @NotNull(message = "Credits value should not be null")
                    @Positive(message = "Credits value should be positive")
                    int param2 = Integer.parseInt(creditsComboValue);
                    GUI.app.disciplinesService.addDiscipline(param1, param2);
                } catch (DisciplineAdditionException | ValidationException ex) {
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
    /**
     * Creates a JPanel with left and right panels merged into the main panel.
     *
     * @param main      The JPanel representing the main panel. Must not be empty.
     * @param labelText The text for the label. Must not be blank.
     * @return The JPanel with the left and right panels merged into the main panel.
     */
    @Override
    public JPanel createJPanel(
            @NotEmpty(message = "Main panel should not be empty")
            JPanel main,
            @NotBlank(message = "The label text should not be blank")
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

        currentPanel = addDisciplineForm(currentPanel);
        currentPanel = deleteDisciplineForm(currentPanel);
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

        ImageIcon image = new ImageIcon(Objects.requireNonNull(DisciplineSettings.class.getResource("/icons/discipline.png")));
        Image rescaledImage = image.getImage().getScaledInstance(300,350, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(20,60,350,350);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
