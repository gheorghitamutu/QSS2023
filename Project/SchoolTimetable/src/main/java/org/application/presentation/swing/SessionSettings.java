package org.application.presentation.swing;

import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Session;
import org.application.presentation.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SessionSettings implements BaseSettings{
    public SessionSettings(){

    }

    private void addGroupToSessionForm(JPanel currentPanel){

        JLabel titleLabel1 = new JLabel("ADD GROUP TO SESSION");
        titleLabel1.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel1.setForeground(Color.decode("#617A55"));
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel1);

        //add techer to session
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel sessionLabel = new JLabel("Discipline Session:");
        sessionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sessionLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> sessionSelector = new JComboBox<>();


        var allSession = GUI.app.sessionsService.getSessions();
        for (var s : allSession) {
            sessionSelector.addItem(s.getDiscipline().getName());
        }
        JPanel sessionPanel = createFieldPanel(sessionLabel, sessionSelector);
        currentPanel.add(sessionPanel);



        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel groupLabel = new JLabel("Group:");
        groupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        groupLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> groupSelector = new JComboBox<>();

        // get all the disciplines from db
        var allGroupsName = GUI.app.studentGroupsService.getStudentGroups();
        for (var group : allGroupsName) {
            groupSelector.addItem(group.getName());
        }
        JPanel teacherPanel = createFieldPanel(groupLabel, groupSelector);
        currentPanel.add(teacherPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton groupBtn = new JButton("Submit");
        JPanel groupButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        groupButtonPanel.setBackground(Color.decode("#F6FFDE"));
        groupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("ADD GROUP TO A SESSION CLICKED");
                try {
                    GUI.app.sessionsService.addGroupToSession((String) sessionSelector.getSelectedItem(), (String) groupSelector.getSelectedItem() );
                } catch (DisciplineNotFoundException | StudentGroupNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }

            }
        });
        groupButtonPanel.add(groupBtn);
        currentPanel.add(groupButtonPanel);

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

        JLabel titleLabel = new JLabel("DELETE SESSION");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        //titleLabel.setBounds(0,30,300,25);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentPanel.add(titleLabel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel regLabel = new JLabel("Data:");
        regLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        regLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> model = new JComboBox<>();

        // get all the disciplines from db
        var allSessions = GUI.app.sessionsService.getSessions();
        for (var session : allSessions) {
            model.addItem(session.getDiscipline().getName());
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
                System.out.println("Delete session btn clicked");
                try {
                    GUI.app.sessionsService.deleteSession((String) model.getSelectedItem());
                } catch (DisciplineNotFoundException | SessionDeletionFailed | SessionNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });


        buttonPanel.add(submitButtonDelete);
        currentPanel.add(buttonPanel);

        return currentPanel;
    }

    public JPanel addSessionForm(JPanel currentPanel){
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("CREATE SESSION");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel);

        //add discipline selector
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel disciplineLabel = new JLabel("Discipline:");
        disciplineLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        disciplineLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> model = new JComboBox<>();

        // get all the disciplines from db
        var allDisciplines = GUI.app.disciplinesService.getDisciplines();
        for (var discipline : allDisciplines) {
            model.addItem(discipline.getName());
        }
        JPanel dataPanel = createFieldPanel(disciplineLabel, model);
        currentPanel.add(dataPanel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setBackground(Color.decode("#617A55"));
        String[] yearValues = {"COURSE", "LABORATORY", "SEMINARY"};
        JComboBox<String> typeComboBox = new JComboBox<>(yearValues);
        JPanel typePanel = createFieldPanel(typeLabel, typeComboBox);
        currentPanel.add(typePanel);


        //Add half year values
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel halfyearLabel = new JLabel("Half Year:");
        halfyearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        halfyearLabel.setBackground(Color.decode("#617A55"));
        String[] halfyearValues = {"A", "B"};
        JComboBox<String> halfyearComboBox = new JComboBox<>(halfyearValues);
        JPanel halfyearPanel = createFieldPanel(halfyearLabel, halfyearComboBox);
        currentPanel.add(halfyearPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                System.out.println("Insert session btn clicked");
                try {
                    GUI.app.sessionsService.addSession(Session.Type.valueOf((String) typeComboBox.getSelectedItem()),
                            (String) halfyearComboBox.getSelectedItem(),
                            (String) model.getSelectedItem());
                } catch (SessionAdditionException | DisciplineNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "An exception occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(submitButtonAdd);
        currentPanel.add(buttonPanel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel titleLabel1 = new JLabel("ADD TEACHER TO SESSION");
        titleLabel1.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel1.setForeground(Color.decode("#617A55"));
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        // titleLabel.setBounds(50,30,300,25);
        currentPanel.add(titleLabel1);

        //add techer to session
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel sessionLabel = new JLabel("Discipline Session:");
        sessionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sessionLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> sessionSelector = new JComboBox<>();


        var allSession = GUI.app.sessionsService.getSessions();
        for (var s : allSession) {
            sessionSelector.addItem(s.getDiscipline().getName());
        }
        JPanel sessionPanel = createFieldPanel(sessionLabel, sessionSelector);
        currentPanel.add(sessionPanel);



        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel teacherLabel = new JLabel("Teacher:");
        teacherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        teacherLabel.setBackground(Color.decode("#617A55"));
        JComboBox<String> teacherSelector = new JComboBox<>();


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
                System.out.println("ADD TEACHER TO A SESSION CLICKED");
                try {
                    GUI.app.sessionsService.addTeacherToSession((String) sessionSelector.getSelectedItem(), (String) teacherSelector.getSelectedItem());
                } catch (DisciplineNotFoundException | SessionNotFoundException | TeacherNotFoundException ex) {
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

        // ADD GROUP TO SESSION
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addGroupToSessionForm(currentPanel);

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

        currentPanel = addSessionForm(currentPanel);
        currentPanel = deleteRoomForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }

    @Override
    public JPanel createRightJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(null);
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(SessionSettings.class.getResource("/icons/generator.png")));
        Image rescaledImage = image.getImage().getScaledInstance(250,250, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(20,200,300,300);
        currentPanel.add(imageLabel, BorderLayout.CENTER);
        return currentPanel;
    }
}
