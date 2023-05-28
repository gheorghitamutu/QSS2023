package org.presentation.swing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Timeslot;
import org.presentation.GUI;
import org.presentation.MainGenerator;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * The TimeslotSettings class represents the settings for managing timeslots in the application.
 * It provides methods for adding and deleting timeslots, as well as generating timetables.
 * The class extends the BaseSettings class and implements various functionality related to timeslot management.
 * Note: This class assumes that the necessary services and dependencies have been properly injected
 * or made accessible through the GUI.app instance.
 */
public class TimeslotSettings implements BaseSettings {

    /**
     * The timeslot version.
     */
    private int timeslotVersion = 0;

    /**
     * The date format for the timeslot.
     */
    @NotEmpty(message = "Date format must not be empty.")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * The time format for the timeslot.
     */
    @NotEmpty(message = "Time format must not be empty.")
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    /**
     * Default constructor for the TimeslotSettings class.
     */
    public TimeslotSettings(){
    }

    /**
     * Creates a JPanel that combines a label and a component for a timeslot field.
     *
     * @param label     The JLabel representing the timeslot label.
     *                  Cannot be null.
     * @param component The JComponent representing the timeslot component.
     *                  Must not be empty.
     * @return The JPanel containing the label and component.
     */
    private JPanel createFieldPanel(@NotEmpty(message = "Timeslot label should not be empty.")
                                    JLabel label,
                                    @NotEmpty (message = "Timeslot component should not be empty.")
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
     * Creates an option list for the delete operation, containing formatted strings representing timeslots.
     * Format the timeslot information into a string.
     * @return the list of options for the delete operation
     */
    private ArrayList<String> createOptionListForDeleteOperation(){
        ArrayList<String> optionList = new ArrayList<>();

        //take the timeslots
        var allTimeslot = GUI.app.timeslotsService.getTimeslots();

        for (var timeslot : allTimeslot){
            Date startDate = timeslot.getStartDate();
            long totalMinutes = timeslot.getTimespan().toMinutes();
            long hours = totalMinutes / 60;
            long min = totalMinutes % 60;

            Date timeDate = timeslot.getTime();

            optionList.add(timeslot.getSession().getDiscipline().getName()  + "," +
                           dateFormat.format(startDate)+ "," +
                           timeFormat.format(timeDate) + "," +
                           String.format("%02d:%02d", hours, min) + "," +
                           timeslot.getRoom().getName());
        }

        return optionList;
    }

    /**
     * Converts a duration string in the format "hh:mm" to a Duration object.
     *
     * @param s the duration string to be converted
     * @return the Duration object representing the duration
     *
     * @throws NumberFormatException if the duration string is not in the correct format
     * @throws IllegalArgumentException if the duration parameter is null or empty
     */
    private Duration durationConvertor(@NotBlank(message = "Duration parameter must not be null.")String s){

        String[] parts = s.split(":");
        long hours = Long.parseLong(parts[0].trim());
        long minutes = Long.parseLong(parts[1]);
        long totalMinutes = hours * 60 + minutes;

        return Duration.ofMinutes(totalMinutes);
    }

    /**
     * Adds a form for deleting a timeslot to the specified panel.
     *
     * @param currentPanel the panel to which the form will be added
     * @return the updated panel with the added timeslot deletion form
     * Call the TimeslotsService to delete the timeslot. The deletion operation is done using these parameters:
     * date, time and duration
     */
    public JPanel deleteRoomForm(@NotEmpty(message = "Current panel should not be empty") JPanel currentPanel){

        currentPanel.setBackground(Color.decode("#F6FFDE"));

        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel titleLabel = new JLabel("DELETE TIMESLOT");
        titleLabel.setFont(new Font("serif", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#617A55"));
        //titleLabel.setBounds(0,30,300,25);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentPanel.add(titleLabel);

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel dataLabel = new JLabel("Data:");
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dataLabel.setBackground(Color.decode("#617A55"));

        ArrayList<String>  optionsForDelete = createOptionListForDeleteOperation();
        JComboBox<String> model = new JComboBox<>();
        for (String opt : optionsForDelete){
            model.addItem(opt);
        }
        JPanel dataPanel = createFieldPanel(dataLabel, model);
        currentPanel.add(dataPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonDelete = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                System.out.println("Delete timeslot btn clicked");
                @NotNull(message = "List of options should not be null")
                String[] options =((String) model.getSelectedItem()).split(",");
                try {
                    GUI.app.timeslotsService.deleteTimeslot(
                            Objects.requireNonNull(dateFormat.parse(options[1]), "Date format value should not be null."),
                            Objects.requireNonNull(timeFormat.parse(options[2]),"Time format value should not be null."),
                            Objects.requireNonNull(durationConvertor(options[3]), "Duration convertor value should not be null."),
                            options[4]);
                } catch (ParseException | TimeslotDeletionFailed | RoomNotFoundException | TimeslotNotFoundException ex) {
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

        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton generateTimetableButton = new JButton("Generate Timetable");

        JPanel generatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        generatorPanel.setBackground(Color.decode("#F6FFDE"));

        generateTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = f.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    System.out.println(f.getSelectedFile().getAbsolutePath());
                    timeslotVersion = timeslotVersion + 1;
                    MainGenerator mainGenerator = new MainGenerator(timeslotVersion);
                    mainGenerator.generateLists();
                    mainGenerator.generateTimetables();
                    mainGenerator.saveAllData(f.getSelectedFile().getAbsolutePath());
                }
            }
        });
        generatorPanel.add(generateTimetableButton);
        currentPanel.add(generatorPanel);
        return currentPanel;
    }

    /**
     * Adds a form for creating a new timeslot to the specified panel.
     *
     * @param currentPanel the panel to which the form will be added
     * @return the updated panel with the added timeslot form
     * All the necessary configurations regarding timslot labels and fileds are done on this side of imeplementation.
     * Get all the rooms from the database and Call the TimeslotsService to add the timeslot.
     */
    public JPanel addTimeslotForm(@NotEmpty(message = "Current panel should not be empty") JPanel currentPanel){
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
        JLabel dayLabel = new JLabel("Day:");
        dayLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dayLabel.setBackground(Color.decode("#617A55"));
        String[] dayValues = { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        JComboBox<String> dayComboBox = new JComboBox<>(dayValues);
        JPanel dayPanel = createFieldPanel(dayLabel, dayComboBox);
        currentPanel.add(dayPanel);

        //Add periodicity field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel periodicityLabel = new JLabel("Periodicity:");
        periodicityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        periodicityLabel.setBackground(Color.decode("#617A55"));
        String[] periodicityValues = { "WEEKLY", "BIWEEKLY", "MONTHLY"};

        JComboBox<String> periodicityComboBox = new JComboBox<>(periodicityValues);
        JPanel periodicityPanel = createFieldPanel(periodicityLabel, periodicityComboBox);
        currentPanel.add(periodicityPanel);

         //Add room field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JComboBox<String> model = new JComboBox<>();
        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roomLabel.setBackground(Color.decode("#617A55"));
        // get all the disciplines from db
        var allRooms = GUI.app.roomsService.getRooms();
        for (var room : allRooms) {
            model.addItem(Objects.requireNonNull(room.getName(), "Room name should not be null."));
        }
        JPanel roomPanel = createFieldPanel(roomLabel, model);
        currentPanel.add(roomPanel);

        //Add session field
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JComboBox<String> sessionModel = new JComboBox<>();
        JLabel sessionLabel = new JLabel("Session:");
        sessionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sessionLabel.setBackground(Color.decode("#617A55"));
        // get all the disciplines from db
        var allSession = GUI.app.sessionsService.getSessions();
        for (var session : allSession) {
            sessionModel.addItem(Objects.requireNonNull(session.getDiscipline().getName(), "Session name should not be null."));
        }
        JPanel sessionPanel = createFieldPanel(sessionLabel, sessionModel);
        currentPanel.add(sessionPanel);

        // Add the submit button
        currentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton submitButtonAdd = new JButton("Submit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F6FFDE"));
        submitButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Insert timeslot btn clicked");
                try {
                    assert startDateField != null;
                    GUI.app.timeslotsService.addTimeslot(
                            Objects.requireNonNull(dateFormat.parse(startDateField.getText()),"Start date format should not be null. "),
                            Objects.requireNonNull(dateFormat.parse(endDateField.getText()), "End date format should not be null."),
                            Objects.requireNonNull(timeFormat.parse(timeField.getText()), "Time format should not be null. "),
                            Objects.requireNonNull(Duration.ofMinutes(Integer.parseInt(timespanField.getText())),"Timeslot format should not be null."),
                            Objects.requireNonNull(Timeslot.Day.valueOf((String) dayComboBox.getSelectedItem()),"Day format should not be null. "),
                            Objects.requireNonNull(Timeslot.Periodicity.valueOf((String) periodicityComboBox.getSelectedItem()),"Periodicity format should not be null."),
                            Objects.requireNonNull((String) model.getSelectedItem(),"Model format should not be null."),
                            Objects.requireNonNull((String) sessionModel.getSelectedItem(),"Session format should not be null."));
                } catch (ParseException | RoomNotFoundException | DisciplineNotFoundException |
                         TimeslotAdditionException | SessionNotFoundException | ValidationException ex) {
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
     * Creates a left JPanel containing the addTimeslotForm and deleteTimeslotForm.
     *
     * @return The left JPanel containing the addTimeslotForm and deleteTimeslotForm.
     */
    @Override
    public JPanel createLeftJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));

        currentPanel = addTimeslotForm(currentPanel);
        currentPanel = deleteRoomForm(currentPanel);
        // Set the frame size and center it on the screen
        return currentPanel;
    }

    /**
     * Creates a right JPanel containing the image.
     *
     * @return The right JPanel containing the image.
     */
    @Override
    public JPanel createRightJPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(null);
        currentPanel.setBackground(Color.decode("#F6FFDE"));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(TimeslotSettings.class.getResource("/icons/timeslot.png")));
        Image rescaledImage = image.getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT);
        ImageIcon finalImage = new ImageIcon(rescaledImage);
        JLabel imageLabel = new JLabel(finalImage);
        imageLabel.setBounds(30,130,300,300);
        currentPanel.add(imageLabel, BorderLayout.CENTER);

        return currentPanel;
    }
}
