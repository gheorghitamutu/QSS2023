package org.presentation.generators;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.domain.models.Timeslot;
import org.presentation.TemplateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The abstract BaseGenerator class provides a foundation for specific generators used to generate the HTML pages of the timetable.
 */
public abstract class BaseGenerator {
    @NotNull(message = "Template utils must not be null")
    protected final TemplateUtils utils;
    @NotEmpty(message = "Separator must not be empty.")
    protected final String separator;
    @NotBlank(message = "Generation string must not be blank")
    protected final String generationDateString;
    @NotNull(message = "Days data map must not be null")
    protected final Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays;

    /**
     * Constructs a BaseGenerator object with the specified generation date string and timetables days data.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     */
    public BaseGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day,StringBuilder>> timetablesDays) {
        this.utils = new TemplateUtils();
        this.separator = System.getProperty("file.separator");
        this.generationDateString = generationDateString;
        this.timetablesDays = timetablesDays;
    }

    /**
     * Adds a table name to the timetablesDays map, with empty data for all days of the week.
     * This method initializes a new HashMap with empty data for all days of the week, for the specified table name in the timetablesDays map.
     *
     * @param tableName The name of the table. Must not be blank.
     */
    protected void addToDaysMap(
            @NotBlank(message = "Table name must not be blank")
            String tableName){
        HashMap<Timeslot.Day, StringBuilder> daysData = new HashMap<>();
        List<Timeslot.Day> daysList = List.of(Timeslot.Day.MONDAY, Timeslot.Day.TUESDAY, Timeslot.Day.WEDNESDAY, Timeslot.Day.THURSDAY, Timeslot.Day.FRIDAY);

        for (Timeslot.Day day : daysList){
            StringBuilder dayData = new StringBuilder();
            daysData.put(day, dayData);
        }

        this.timetablesDays.put(tableName, daysData);
    }

    /**
     * Generates specific data or performs tasks for the generator.
     * This method is abstract and must be implemented by subclasses. It is responsible for generating specific data or performing tasks related to the generator's functionality.
     * Each subclass of BaseGenerator provides its own implementation of this method.
     */
    public abstract void generate();
}
