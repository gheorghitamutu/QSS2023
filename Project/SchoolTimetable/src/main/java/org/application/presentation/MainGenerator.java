package org.application.presentation;

import org.application.domain.models.*;
import org.application.presentation.generators.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainGenerator {
    private final List<Discipline> disciplines;
    private final List<Room> rooms;
    private final List<StudentGroup> groups;
    private final List<Teacher> teachers;
    private final List<Timeslot> timeslots;
    private final int version;
    private final String generationDateString;

    private final TemplateUtils utils;
    private final String separator;
    private final Map<String, String> listsData;
    private final Map<String, String> timetablesData;
    private final Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays;
    private final Map<String, String> timetablesNames;

    private boolean listsGenerated;
    private boolean timetablesGenerated;

    public MainGenerator(List<Discipline> disciplines, List<Room> rooms, List<StudentGroup> groups, List<Teacher> teachers, List<Timeslot> timeslots, int version) {
        this.disciplines = disciplines;
        this.rooms = rooms;
        this.groups = groups;
        this.teachers = teachers;
        this.timeslots = timeslots;
        this.version = version;

        Date generationDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        this.generationDateString = formatter.format(generationDate);

        this.utils = new TemplateUtils();

        this.listsData = new HashMap<>();
        this.timetablesData = new HashMap<>();
        this.timetablesDays = new HashMap<>();
        this.timetablesNames = new HashMap<>();

        this.listsGenerated = false;
        this.timetablesGenerated = false;
        this.separator = System.getProperty("file.separator");
    }

    public void generateLists(){
        List<BaseGenerator> generators = new ArrayList<>();

        generators.add(new HomeGenerator(generationDateString, timetablesDays, version, listsData));
        generators.add(new DisciplinesGenerator(generationDateString, timetablesDays, disciplines, timetablesNames, listsData));
        generators.add(new RoomsGenerator(generationDateString, timetablesDays, rooms, timetablesNames, listsData));
        generators.add(new StudentsGenerator(generationDateString, timetablesDays, groups, timetablesNames, listsData));
        generators.add(new TeachersGenerator(generationDateString, timetablesDays, teachers, timetablesNames, listsData));

        for (BaseGenerator generator : generators){
            generator.generate();
        }

        this.listsGenerated = true;
    }

    public void generateTimetables(){
        BaseGenerator generator = new TimetablesGenerator(generationDateString, timetablesDays, timeslots, timetablesNames, timetablesData);
        generator.generate();

        this.timetablesGenerated = true;
    }

    private void saveMap(Map<String, String> dataMap, String savePath){
        for (Map.Entry<String, String> mapEntry : dataMap.entrySet()){
            String name = mapEntry.getKey();
            String data = mapEntry.getValue();
            String newSavePath = savePath + this.separator + name + ".html";

            this.utils.saveTemplateDataToFile(data, newSavePath);
        }
    }

    public void saveAllData(String savePath){
        if (this.listsGenerated){
            this.saveMap(listsData, savePath);
        }

        if (this.timetablesGenerated){
            this.saveMap(timetablesData, savePath);
        }

        this.utils.copyStyle(savePath);
    }
}