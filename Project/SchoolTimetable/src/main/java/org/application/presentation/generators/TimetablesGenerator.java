package org.application.presentation.generators;

import org.application.domain.models.*;
import org.application.presentation.GUI;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class TimetablesGenerator extends BaseGenerator{
    private final List<Timeslot> timeslots;
    private final List<Room> rooms;
    private final Map<String, String> timetablesNames;
    private final Map<String, String> timetablesData;
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;

    public TimetablesGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, Map<String, String> timetablesNames, Map<String, String> timetablesData, Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms){
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

    private void extractTeachersForTimetable(Set<Teacher> teachers, List<String> tableNames, Map<String, String> teacherNamesTables){
        for (Teacher teacher : teachers){
            String timetableName = "timetable_t_" + teacher.getName().toLowerCase().replace(" ", "_");
            String teacherName = teacher.getName();
            tableNames.add(timetableName);

            if (teacher.getType() == Teacher.Type.TEACHER) teacherName = "Prof. " + teacherName;
            else if (teacher.getType() == Teacher.Type.COLLABORATOR) teacherName = "Collab. " + teacherName;
            teacherNamesTables.put(teacherName, timetableName);
        }
    }

    private void extractGroupsForTimetable(Set<StudentGroup> studentGroups, List<String> tableNames, Map<String, String> groupsNamesTables){
        for (StudentGroup studentGroup : studentGroups){
            String groupName = studentGroup.getName();
            int year = studentGroup.getYear();
            String timetableName = "timetable_g_" + year + groupName.toLowerCase().replace(" ", "_");
            tableNames.add(timetableName);

            groupsNamesTables.put(year + groupName, timetableName);
        }
    }

    private String addElementsToTimetableEntry(String name, String timetableEntry, Map<String, String> elementsNamesTables, String endLine){
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

    private void addEntryToTables(String timetableEntry, List<String> tableNames, Timeslot.Day day){
        for (String tableName : tableNames){
            this.timetablesDays.get(tableName).get(day).append(timetableEntry);
        }
    }

    private void addToFreeRoomsTable(Timeslot.Day day, Room room, int startHour, int endHour){
        for (int i = startHour; i < endHour; i++) {
            freeRooms.get(room).get(startHour).replace(day, false);
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

            try{
                startHour = Integer.parseInt(startTimeString.substring(0, 2));
                endHour = Integer.parseInt(endTimeString.substring(0, 2));
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
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
