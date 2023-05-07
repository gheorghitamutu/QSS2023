package org.application.presentation.generators;

import org.application.domain.models.StudentGroup;
import org.application.domain.models.Timeslot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsGenerator extends BaseGenerator{
    private final List<StudentGroup> groups;
    private final Map<String, String> timetablesNames;
    private final Map<String, String> listsData;

    public StudentsGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, List<StudentGroup> groups, Map<String, String> timetablesNames, Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.groups = groups;
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

    public void generate(){
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
}
