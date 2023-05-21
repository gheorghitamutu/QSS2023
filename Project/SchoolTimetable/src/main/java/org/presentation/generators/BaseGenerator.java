package org.presentation.generators;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.domain.models.Timeslot;
import org.presentation.TemplateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseGenerator {
    @NotNull(message = "Template utils must not be null")
    protected final TemplateUtils utils;
    @NotEmpty(message = "Separator must not be empty.")
    protected final String separator;
    @NotBlank(message = "Generation string must not be blank")
    protected final String generationDateString;
    @NotNull(message = "Days data map must not be null")
    protected final Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays;

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

    public abstract void generate();
}
