package org.presentation.generators;

import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.domain.models.*;
import org.presentation.GUI;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Objects;

public class TimetablesGenerator extends BaseGenerator{
    @NotNull(message = "Timeslots list must not be null")
    private final List<@Valid Timeslot> timeslots;
    @NotNull(message = "Rooms list must not be null")
    private final List<@Valid Room> rooms;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Timetables data map must not be null")
    private final Map<String, String> timetablesData;
    @NotNull(message = "Free rooms map must not be null")
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;

    public TimetablesGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @NotNull(message = "Timetables names map must not be null")
            Map<String, String> timetablesNames,
            @NotNull(message = "Timetables data map must not be null")
            Map<String, String> timetablesData,
            @NotNull(message = "Free rooms map must not be null")
            Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms){
        super(generationDateString, timetablesDays);

        this.timeslots = GUI.app.timeslotsService.getSortedTimeslotsByStartTime();
        this.rooms = GUI.app.roomsService.getRooms();
        this.timetablesNames = timetablesNames;
        this.timetablesData = timetablesData;
        this.freeRooms = freeRooms;
    }

    private void initializeFreeRooms(){
        Timeslot.Day[] daysArray = Timeslot.Day.class.getEnumConstants();

        for (Room room : rooms){
            Map<Integer, Map<Timeslot.Day, Boolean>> hoursMap = new HashMap<>();
            for (int i = 8; i < 20; i++) {
                Map<Timeslot.Day, Boolean> dayMap = new HashMap<>();
                for (Timeslot.Day day : daysArray){
                    dayMap.put(day, true);
                }
                hoursMap.put(i, dayMap);
            }
            freeRooms.put(room, hoursMap);
        }
    }

    private void createTimetableDataFromDays(){
        for (Map.Entry<String, Map<Timeslot.Day, StringBuilder>> mapEntry : this.timetablesDays.entrySet()){
            String tableName = mapEntry.getKey();
            Map<Timeslot.Day, StringBuilder> daysData = mapEntry.getValue();

            String timetableEntry = utils.getBaseTemplateData("timetable");
            timetableEntry = timetableEntry.replace("$generation_date", this.generationDateString);
            timetableEntry = timetableEntry.replace("$title", this.timetablesNames.get(tableName));

            for (Map.Entry<Timeslot.Day, StringBuilder> dayEntry : daysData.entrySet()){
                String dayName = dayEntry.getKey().name().toLowerCase();
                String dayData = dayEntry.getValue().toString();

                timetableEntry = timetableEntry.replace("$" + dayName + "_elements", dayData);
            }

            this.timetablesData.put(tableName, timetableEntry);
        }
    }

    private void extractTeachersForTimetable(
            @NotNull(message = "Teachers set must not be null")
            Set<@Valid Teacher> teachers,
            @NotEmpty(message = "Table names list must not be empty")
            List<String> tableNames,
            @NotNull(message = "Teachers names map must not be null")
            Map<String, String> teacherNamesTables){
        for (Teacher teacher : teachers){
            String teacherName = Objects.requireNonNull(teacher.getName(), "Teacher name should not be null.");
            String timetableName = "timetable_t_" + teacherName.toLowerCase().replace(" ", "_");
            tableNames.add(timetableName);

            if (teacher.getType() == Teacher.Type.TEACHER) teacherName = "Prof. " + teacherName;
            else if (teacher.getType() == Teacher.Type.COLLABORATOR) teacherName = "Collab. " + teacherName;
            teacherNamesTables.put(teacherName, timetableName);
        }
    }

    private void extractGroupsForTimetable(
            @NotNull(message = "Groups set must not be null")
            Set<@Valid StudentGroup> studentGroups,
            @NotEmpty(message = "Table names list must not be empty")
            List<String> tableNames,
            @NotNull(message = "Groups names map must not be null")
            Map<String, String> groupsNamesTables){
        for (StudentGroup studentGroup : studentGroups){
            String groupName = Objects.requireNonNull(studentGroup.getName(), "Group name should not be null.");
            int year = studentGroup.getYear();
            String timetableName = "timetable_g_" + year + groupName.toLowerCase().replace(" ", "_");
            tableNames.add(timetableName);

            groupsNamesTables.put(year + groupName, timetableName);
        }
    }

    @NotBlank(message = "Timetable entry string must not be blank")
    private String addElementsToTimetableEntry(
            @NotBlank(message = "Entry name must not be blank")
            String name,
            @NotBlank(message = "Entry data must not be blank")
            String timetableEntry,
            @NotNull(message = "Elements names map must not be null")
            Map<String, String> elementsNamesTables,
            @NotBlank(message = "Endline must not be blank")
            String endLine){
        StringBuilder allElements = new StringBuilder();

        for (Map.Entry<String, String> mapEntry : elementsNamesTables.entrySet()){
            String elementEntry = utils.getBaseTemplateData("atomics" + this.separator + "a_entry");
            String elementName = mapEntry.getKey();
            String elementTable = mapEntry.getValue();

            elementEntry = elementEntry.replace("$entry_ref", "./" + elementTable + ".html");
            elementEntry = elementEntry.replace("$entry_name", elementName);

            allElements.append(elementEntry).append(endLine);
        }

        if (allElements.length() >= endLine.length()) allElements.setLength(allElements.length() - endLine.length());
        timetableEntry = timetableEntry.replace("$entry_" + name, allElements.toString());
        return timetableEntry;
    }

    private void addEntryToTables(
            @NotBlank(message = "Timetable entry data must not be blank")
            String timetableEntry,
            @NotEmpty(message = "Table names list must not be empty")
            List<String> tableNames,
            @NotNull(message = "Day must not be null")
            Timeslot.Day day){
        for (String tableName : tableNames){
            this.timetablesDays.get(tableName).get(day).append(timetableEntry);
        }
    }

    private void addToFreeRoomsTable(
            @NotNull(message = "Day must not be null")
            Timeslot.Day day,
            @NotNull(message = "Room must not be null")
            Room room,
            @Min(value = 0, message = "Start hour must be greater than -1")
            int startHour,
            @Min(value = 0, message = "End hour must be greater than -1")
            int endHour){
        for (int i = startHour; i < endHour; i++) {
            freeRooms.get(room).get(i).replace(day, false);
        }
    }

    private void generateTimetablesFromTimeslots(){
        for (Timeslot timeslot : this.timeslots) {
            Date startTime = new Date(timeslot.getTime().getTime());
            Date endTime = Date.from(startTime.toInstant().plus(timeslot.getTimespan()));
            Room room = timeslot.getRoom();
            Discipline discipline = timeslot.getSession().getDiscipline();
            Set<Teacher> teachers = timeslot.getSession().getTeachers();
            Set<StudentGroup> studentGroups = timeslot.getSession().getGroups();

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String startTimeString = formatter.format(startTime);
            String endTimeString = formatter.format(endTime);

            int startHour = 0;
            int endHour = 0;
            int endMinutes = 0;

            try{
                startHour = Integer.parseInt(startTimeString.substring(0, 2));
                endHour = Integer.parseInt(endTimeString.substring(0, 2));
                endMinutes = Integer.parseInt(endTimeString.substring(3, 5));
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }

            if (endMinutes > 0){
                endHour += 1;
            }

            this.addToFreeRoomsTable(timeslot.getWeekday(), room, startHour, endHour);

            SimpleDateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy");
            String startDateString = formatter2.format(timeslot.getStartDate());
            String endDateString = formatter2.format(timeslot.getEndDate());

            List<String> tableNames = new ArrayList<>();
            Map<String, String> groupsNamesTables = new HashMap<>();
            Map<String, String> teacherNamesTables = new HashMap<>();

            String disciplineTable = "timetable_d_" + discipline.getName().toLowerCase().replace(" ", "_");
            String roomTable = "timetable_r_" + room.getName().toLowerCase().replace(" ", "_");
            tableNames.add("timetable");
            tableNames.add(disciplineTable);
            tableNames.add(roomTable);

            extractTeachersForTimetable(teachers, tableNames, teacherNamesTables);
            extractGroupsForTimetable(studentGroups, tableNames, groupsNamesTables);

            String timetableEntry = utils.getBaseTemplateData("atomics" + this.separator + "timetable_entry");

            timetableEntry = timetableEntry.replace("$entry_start", startTimeString);
            timetableEntry = timetableEntry.replace("$entry_end", endTimeString);
            timetableEntry = timetableEntry.replace("$discipline_name", discipline.getName());
            timetableEntry = timetableEntry.replace("$discipline_ref", "./" + disciplineTable + ".html");
            timetableEntry = timetableEntry.replace("$entry_type", timeslot.getSession().getType().toString());
            timetableEntry = timetableEntry.replace("$room_name", room.getName());
            timetableEntry = timetableEntry.replace("$room_ref", "./" + roomTable + ".html");
            timetableEntry = timetableEntry.replace("$entry_frequency", timeslot.getPeriodicity().toString());
            timetableEntry = timetableEntry.replace("$entry_period", startDateString + " - " + endDateString);

            timetableEntry = addElementsToTimetableEntry("students", timetableEntry, groupsNamesTables, ", ");
            timetableEntry = addElementsToTimetableEntry("teachers", timetableEntry, teacherNamesTables, "<br>");

            addEntryToTables(timetableEntry, tableNames, timeslot.getWeekday());
        }
    }
    public void generate(){
        this.addToDaysMap("timetable");
        this.timetablesNames.put("timetable", "Complete Timetable");

        this.initializeFreeRooms();
        this.generateTimetablesFromTimeslots();
        this.createTimetableDataFromDays();
    }
}
