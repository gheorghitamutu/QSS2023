package org.application.presentation.generators;

import org.application.domain.models.StudentGroup;
import org.application.domain.models.Timeslot;
import org.application.presentation.GUI;

import java.util.*;

public class StudentsGenerator extends BaseGenerator{
    private final List<StudentGroup> groups;
    private final Map<String, String> timetablesNames;
    private final Map<String, String> listsData;

    public StudentsGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, Map<String, String> timetablesNames, Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.groups = GUI.app.studentGroupsService.getStudentGroups();
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

    public void generate(){
        String studentsData = utils.getBaseTemplateData("students");
        studentsData = studentsData.replace("$generation_date", this.generationDateString);
        Map<StudentGroup.Type, Map<Integer, List<String>>> typeGroupMap = new HashMap<>();
        Map<Integer, List<String>> masterYearsGroupsMap = new HashMap<>();
        Map<Integer, List<String>> bachelorYearsGroupsMap = new HashMap<>();

        typeGroupMap.put(StudentGroup.Type.BACHELOR, bachelorYearsGroupsMap);
        typeGroupMap.put(StudentGroup.Type.MASTER, masterYearsGroupsMap);

        for (StudentGroup studentGroup : this.groups){
            Integer year = studentGroup.getYear();
            String name = studentGroup.getName();
            StudentGroup.Type groupType = studentGroup.getType();
            Map<Integer, List<String>> currentMap = typeGroupMap.get(groupType);

            if (currentMap.containsKey(year)){
                currentMap.get(year).add(name);
            }
            else {
                List<String> groupsList = new ArrayList<>();
                groupsList.add(name);
                currentMap.put(year, groupsList);
            }
        }

        for (Map.Entry<StudentGroup.Type, Map<Integer, List<String>>> currentMapEntry : typeGroupMap.entrySet()) {
            String mapData = utils.getBaseTemplateData("atomics" + this.separator + "ulist_entry");
            StringBuilder elementsData = new StringBuilder();
            Map<Integer, List<String>> currentMap = currentMapEntry.getValue();
            StudentGroup.Type groupType = currentMapEntry.getKey();

            for (Map.Entry<Integer, List<String>> mapEntry : currentMap.entrySet()) {
                Integer year = mapEntry.getKey();
                String yearEntry = "<li>Year " + year;
                elementsData.append(yearEntry);

                List<String> groupNames = mapEntry.getValue();

                String yearData = utils.getBaseTemplateData("atomics" + this.separator + "ulist_entry");
                StringBuilder groupsElementsData = new StringBuilder();

                for (String groupName : groupNames) {
                    String groupEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

                    String timetableName = "timetable_g_" + year + groupName.toLowerCase().replace(" ", "_");
                    groupEntry = groupEntry.replace("$entry_name", groupName);
                    groupEntry = groupEntry.replace("$entry_ref", "./" + timetableName + ".html");

                    String groupTypeString = groupType.toString().toLowerCase();
                    groupTypeString = groupTypeString.substring(0, 1).toUpperCase() + groupTypeString.substring(1);

                    groupsElementsData.append(groupEntry);
                    this.addToDaysMap(timetableName);
                    this.timetablesNames.put(timetableName, "Timetable of Group " + groupName + ", Year " + year + ", " + groupTypeString);
                }

                yearData = yearData.replace("$elements", groupsElementsData.toString());
                elementsData.append(yearData);
                elementsData.append("</li>");
            }

            mapData = mapData.replace("$elements", elementsData.toString());
            studentsData = studentsData.replace("$" + groupType.toString().toLowerCase() + "_element_lists", mapData);
            this.listsData.put("students", studentsData);
        }
    }
}