package org.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.presentation.generators.*;
import org.domain.models.Room;
import org.domain.models.Timeslot;
import org.presentation.generators.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainGenerator {
    @Min(value = 1, message = "Version must be greater than 0")
    private final int version;
    @NotBlank(message = "Generation string must not be blank")
    private final String generationDateString;

    @NotNull(message = "Template utils must not be null")
    private final TemplateUtils utils;
    @NotEmpty(message = "Separator must not be empty.")
    private final String separator;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;
    @NotNull(message = "Timetables data map must not be null")
    private final Map<String, String> timetablesData;
    @NotNull(message = "Days data map must not be null")
    private final Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Free rooms map must not be null")
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;

    private boolean listsGenerated;
    private boolean timetablesGenerated;

    public MainGenerator(
            @Min(value = 1, message = "Version must be greater than 0")
            int version) {
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

    private void saveMap(
            @NotEmpty(message = "Data map must not be empty")
            Map<String, String> dataMap,
            @NotBlank(message = "Save path must not be blank")
            String savePath){
        for (Map.Entry<String, String> mapEntry : dataMap.entrySet()){
            String name = mapEntry.getKey();
            String data = mapEntry.getValue();
            String newSavePath = savePath + this.separator + name + ".html";

            this.utils.saveTemplateDataToFile(data, newSavePath);
        }
    }

    public void saveAllData(
            @NotBlank(message = "Save path must not be blank")
            String savePath){
        if (this.listsGenerated){
            this.saveMap(listsData, savePath);
        }

        if (this.timetablesGenerated){
            this.saveMap(timetablesData, savePath);
        }

        this.utils.copyStyle(savePath);
    }
}