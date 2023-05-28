package org.presentation.generators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.domain.models.StudentGroup;
import org.domain.models.Timeslot;
import org.presentation.GUI;

import java.util.*;


/**
 * The StudentsGenerator class is responsible for generating student-related data and
 * organizing it into individual timetables for each student group.
 * It extends the BaseGenerator class and inherits its core functionality for generating timetables based on the provided data.
 * The generated timetables are organized by group type (bachelor or master) and year.
 * Each group type contains a list of years, and each year contains a list of student groups.
 * It populates the 'students' section of the lists data map with the generated data.
 */
public class StudentsGenerator extends BaseGenerator{
    @NotNull(message = "Groups list must not be null")
    private final List<@Valid StudentGroup> groups;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    /**
     * Constructs a StudentsGenerator object with the specified generation date string, timetables days data, timetables names map, and lists data map.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     * @param timetablesNames      The map containing the timetable title organized by table names. Must not be null.
     * @param listsData            The map containing the generated data for various lists used on the page. Must not be null.
     */
    public StudentsGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @NotNull(message = "Timetables names map must not be null")
            Map<String, String> timetablesNames,
            @NotNull(message = "Lists data map must not be null")
            Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.groups = GUI.app.studentGroupsService.getStudentGroups();
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

    /**
     * Generates student-related information and timetables for student groups.
     * Overrides the abstract method 'generate' inherited from the BaseGenerator class.
     * The generation process involves organizing student groups by type and year, and generating individual timetables for each group.
     * The generated data includes information about the student groups, such as their names and respective timetables.
     * The generated timetables are organized by group type (bachelor or master) and year.
     * It populates the 'students' section of the lists data map with the generated data.
     */
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
            String name = Objects.requireNonNull(studentGroup.getName(), "Group name should not be null.");
            StudentGroup.Type groupType = Objects.requireNonNull(studentGroup.getType(), "Group type should not be null.");
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
