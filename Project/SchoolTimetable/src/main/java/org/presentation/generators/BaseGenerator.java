package org.presentation.generators;
import org.domain.models.Timeslot;
import org.presentation.TemplateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseGenerator {
    protected final TemplateUtils utils;
    protected final String separator;
    protected final String generationDateString;
    protected final Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays;

    public BaseGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays) {
        this.utils = new TemplateUtils();
        this.separator = System.getProperty("file.separator");
        this.generationDateString = generationDateString;
        this.timetablesDays = timetablesDays;
    }

    protected void addToDaysMap(String tableName){
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
