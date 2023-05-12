package org.presentation;

import org.presentation.generators.*;
import org.domain.models.Room;
import org.domain.models.Timeslot;
import org.presentation.generators.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainGenerator {
    private final int version;
    private final String generationDateString;

    private final TemplateUtils utils;
    private final String separator;
    private final Map<String, String> listsData;
    private final Map<String, String> timetablesData;
    private final Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays;
    private final Map<String, String> timetablesNames;
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;

    private boolean listsGenerated;
    private boolean timetablesGenerated;

    public MainGenerator(int version) {
        this.version = version;

        Date generationDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        this.generationDateString = formatter.format(generationDate);

        this.utils = new TemplateUtils();

        this.listsData = new HashMap<>();
        this.timetablesData = new HashMap<>();
        this.timetablesDays = new HashMap<>();
        this.timetablesNames = new HashMap<>();
        this.freeRooms = new HashMap<>();

        this.listsGenerated = false;
        this.timetablesGenerated = false;
        this.separator = System.getProperty("file.separator");
    }

    public void generateLists(){
        List<BaseGenerator> generators = new ArrayList<>();

        generators.add(new HomeGenerator(generationDateString, timetablesDays, version, listsData));
        generators.add(new DisciplinesGenerator(generationDateString, timetablesDays, timetablesNames, listsData));
        generators.add(new RoomsGenerator(generationDateString, timetablesDays, timetablesNames, listsData));
        generators.add(new StudentsGenerator(generationDateString, timetablesDays, timetablesNames, listsData));
        generators.add(new TeachersGenerator(generationDateString, timetablesDays, timetablesNames, listsData));

        for (BaseGenerator generator : generators){
            generator.generate();
        }

        this.listsGenerated = true;
    }

    public void generateTimetables(){
        BaseGenerator timetablesGenerator = new TimetablesGenerator(generationDateString, timetablesDays, timetablesNames, timetablesData, freeRooms);
        timetablesGenerator.generate();

        BaseGenerator freeRoomsGenerator = new FreeRoomsGenerator(generationDateString, timetablesDays, freeRooms, listsData);
        freeRoomsGenerator.generate();

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