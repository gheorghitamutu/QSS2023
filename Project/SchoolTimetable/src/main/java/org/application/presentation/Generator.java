package org.application.presentation;

import org.application.domain.models.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class Generator {
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

    public Generator(List<Discipline> disciplines, List<Room> rooms, List<StudentGroup> groups, List<Teacher> teachers, List<Timeslot> timeslots, int version) {
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

    private void addToDaysMap(String tableName){
        HashMap<Timeslot.Day, StringBuilder> daysData = new HashMap<>();
        List<Timeslot.Day> daysList = List.of(Timeslot.Day.MONDAY, Timeslot.Day.TUESDAY, Timeslot.Day.WEDNESDAY, Timeslot.Day.THURSDAY, Timeslot.Day.FRIDAY);

        for (Timeslot.Day day : daysList){
            StringBuilder dayData = new StringBuilder();
            daysData.put(day, dayData);
        }

        this.timetablesDays.put(tableName, daysData);
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

    private void generateMainList(){
        String mainData = utils.getBaseTemplateData("main");
        mainData = mainData.replace("$generation_date", this.generationDateString);
        mainData = mainData.replace("$version", String.valueOf(this.version));
        this.listsData.put("main", mainData);
    }

    private void generateDisciplinesList(){
        String disciplinesData = utils.getBaseTemplateData("disciplines");
        disciplinesData = disciplinesData.replace("$generation_date", this.generationDateString);
        StringBuilder elementsData = new StringBuilder();

        for (Discipline discipline : this.disciplines){
            StringBuilder disciplineData = new StringBuilder();
            List<Teacher> teachers = new ArrayList<>();
            List<StudentGroup> studentGroups = new ArrayList<>();

            String disciplineEntry = utils.getBaseTemplateData("atomics" + this.separator + "disciplines_entry");

            String name = discipline.getName();
            String timetableName = "timetable_d_" + name.toLowerCase().replace(" ", "_");

            disciplineEntry = disciplineEntry.replace("$discipline_name", name);
            disciplineEntry = disciplineEntry.replace("$discipline_ref", "./" + timetableName + ".html");

            for (Session session : discipline.getSessions()){
                for (Teacher teacher : session.getTeachers()){
                    if (teacher.getType() == Teacher.Type.TEACHER && !teachers.contains(teacher)) teachers.add(teacher);
                }
                for (StudentGroup group : session.getGroups()){
                    if (!studentGroups.contains(group)) studentGroups.add(group);
                }
            }

            for (Teacher teacher : teachers) disciplineData.append(teacher.getName()).append(", ");
            for (StudentGroup group : studentGroups) disciplineData.append(group.getYear()).append(group.getName()).append(", ");
            if (disciplineData.length() > 2) disciplineData.setLength(disciplineData.length() - 2);

            disciplineEntry = disciplineEntry.replace("$discipline_data", disciplineData.toString());
            elementsData.append(disciplineEntry);

            this.addToDaysMap(timetableName);
            this.timetablesNames.put(timetableName, "Timetable of " + name);
        }

        disciplinesData = disciplinesData.replace("$elements", elementsData.toString());
        this.listsData.put("disciplines", disciplinesData);
    }

    private void generateRoomsList(){
        String roomsData = utils.getBaseTemplateData("rooms");
        roomsData = roomsData.replace("$generation_date", this.generationDateString);
        StringBuilder labElementsData = new StringBuilder();
        StringBuilder courseElementsData = new StringBuilder();

        for (Room room : this.rooms){
            String roomEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

            String name = room.getName();
            String timetableName = "timetable_r_" + name.toLowerCase().replace(" ", "_");

            roomEntry = roomEntry.replace("$entry_name", name);
            roomEntry = roomEntry.replace("$entry_ref", "./" + timetableName + ".html");

            if (room.getType() == Room.Type.COURSE){
                courseElementsData.append(roomEntry);
            }
            else if (room.getType() == Room.Type.LABORATORY){
                labElementsData.append(roomEntry);
            }

            this.addToDaysMap(timetableName);
            this.timetablesNames.put(timetableName, "Timetable of Room " + name);
        }

        roomsData = roomsData.replace("$lab_elements", labElementsData.toString());
        roomsData = roomsData.replace("$course_elements", courseElementsData.toString());
        this.listsData.put("rooms", roomsData);
    }

    private void generateStudentsList(){
        String studentsData = utils.getBaseTemplateData("students");
        studentsData = studentsData.replace("$generation_date", this.generationDateString);
        Map<Integer, List<String>> yearsGroupsMap = new HashMap<>();

        for (StudentGroup studentGroup : this.groups){
            Integer year = studentGroup.getYear();
            String name = studentGroup.getName();

            if (yearsGroupsMap.containsKey(year)){
                yearsGroupsMap.get(year).add(name);
            }
            else {
                List<String> groupsList = new ArrayList<>();
                groupsList.add(name);
                yearsGroupsMap.put(year, groupsList);
            }
        }

        String mapData = utils.getBaseTemplateData("atomics" + this.separator + "ulist_entry");
        StringBuilder elementsData = new StringBuilder();

        for (Map.Entry<Integer, List<String>> mapEntry : yearsGroupsMap.entrySet()){
            Integer year = mapEntry.getKey();
            String yearEntry = "<li>Year " + year;
            elementsData.append(yearEntry);

            List<String> groupNames = mapEntry.getValue();

            String yearData = utils.getBaseTemplateData("atomics" + this.separator + "ulist_entry");
            StringBuilder groupsElementsData = new StringBuilder();

            for (String groupName : groupNames){
                String groupEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

                String timetableName = "timetable_g_" + year + groupName.toLowerCase().replace(" ", "_");
                groupEntry = groupEntry.replace("$entry_name", groupName);
                groupEntry = groupEntry.replace("$entry_ref", "./" + timetableName + ".html");

                groupsElementsData.append(groupEntry);
                this.addToDaysMap(timetableName);
                this.timetablesNames.put(timetableName, "Timetable of Group " + groupName + ", Year " + year);
            }

            yearData = yearData.replace("$elements", groupsElementsData.toString());
            elementsData.append(yearData);
        }

        mapData = mapData.replace("$elements", elementsData.toString());
        studentsData = studentsData.replace("$element_lists", mapData);
        this.listsData.put("students", studentsData);
    }

    private void generateTeachersList(){
        String teachersData = utils.getBaseTemplateData("teachers");
        teachersData = teachersData.replace("$generation_date", this.generationDateString);
        StringBuilder elementsData = new StringBuilder();

        for (Teacher teacher : this.teachers){
            String teacherEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

            String name = teacher.getName();
            String timetableName = "timetable_t_" + name.toLowerCase().replace(" ", "_");

            if (teacher.getType() == Teacher.Type.TEACHER) name = name + ", Prof.";
            else if (teacher.getType() == Teacher.Type.COLLABORATOR) name = name + ", Collab.";

            teacherEntry = teacherEntry.replace("$entry_name", name);
            teacherEntry = teacherEntry.replace("$entry_ref", "./" + timetableName + ".html");

            elementsData.append(teacherEntry);
            this.addToDaysMap(timetableName);
            this.timetablesNames.put(timetableName, "Timetable of " + name);
        }

        teachersData = teachersData.replace("$elements", elementsData.toString());
        this.listsData.put("teachers", teachersData);
    }

    public void generateLists(){
        this.generateMainList();
        this.generateDisciplinesList();
        this.generateRoomsList();
        this.generateStudentsList();
        this.generateTeachersList();

        this.listsGenerated = true;
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

    private void generateTimetablesFromTimeslots(){
        for (Timeslot timeslot : this.timeslots){
            Date startTime = timeslot.getTime();
            Date endTime = Date.from(startTime.toInstant().plus(timeslot.getTimespan()));
            Room room = timeslot.getRoom();
            Discipline discipline = timeslot.getSession().getDiscipline();
            Set<Teacher> teachers = timeslot.getSession().getTeachers();
            Set<StudentGroup> studentGroups = timeslot.getSession().getGroups();

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String startTimeString = formatter.format(startTime);
            String endTimeString = formatter.format(endTime);

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

            timetableEntry = addElementsToTimetableEntry("students", timetableEntry, groupsNamesTables, ", ");
            timetableEntry = addElementsToTimetableEntry("teachers", timetableEntry, teacherNamesTables, "<br>");

            addEntryToTables(timetableEntry, tableNames, timeslot.getWeekday());
        }
    }

    public void generateTimetables(){
        this.addToDaysMap("timetable");
        this.timetablesNames.put("timetable", "Complete Timetable");

        this.generateTimetablesFromTimeslots();
        this.createTimetableDataFromDays();

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
    }
}