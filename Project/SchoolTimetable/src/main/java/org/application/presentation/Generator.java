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

        this.listsGenerated = false;
        this.timetablesGenerated = false;
        this.separator = System.getProperty("file.separator");
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

            String timetableData = utils.getBaseTemplateData("timetable");
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
            this.timetablesData.put(timetableName, timetableData);
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
            String timetableData = utils.getBaseTemplateData("timetable");
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

            this.timetablesData.put(timetableName, timetableData);
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
                String timetableData = utils.getBaseTemplateData("timetable");
                String groupEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

                String timetableName = "timetable_g_" + year + groupName.toLowerCase().replace(" ", "_");
                groupEntry = groupEntry.replace("$entry_name", groupName);
                groupEntry = groupEntry.replace("$entry_ref", "./" + timetableName + ".html");

                groupsElementsData.append(groupEntry);
                this.timetablesData.put(timetableName, timetableData);
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
            String timetableData = utils.getBaseTemplateData("timetable");
            String teacherEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

            String name = teacher.getName();
            String timetableName = "timetable_t_" + name.toLowerCase().replace(" ", "_");

            if (teacher.getType() == Teacher.Type.TEACHER) name = name + ", Prof.";
            else if (teacher.getType() == Teacher.Type.COLLABORATOR) name = name + ", Collab.";

            teacherEntry = teacherEntry.replace("$entry_name", name);
            teacherEntry = teacherEntry.replace("$entry_ref", "./" + timetableName + ".html");

            elementsData.append(teacherEntry);
            this.timetablesData.put(timetableName, timetableData);
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

    public void generateTimetables(){
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