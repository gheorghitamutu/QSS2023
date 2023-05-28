package org.presentation.generators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.domain.models.*;
import org.presentation.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * The DisciplinesGenerator class is responsible for generating discipline data for timetables.
 * It extends the BaseGenerator class and implements the necessary logic to generate discipline-related information.
 * This class generates discipline data including teacher information, associated student groups, and updates the disciplines data in the listsData map.
 */
public class DisciplinesGenerator extends BaseGenerator{
    @NotNull(message = "Disciplines list must not be null")
    private final List<@Valid Discipline> disciplines;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    /**
     * Constructs a DisciplinesGenerator object with the specified generation date string, timetables days data, timetables names map, and lists data map.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     * @param timetablesNames      The map containing the timetable title organized by table names. Must not be null.
     * @param listsData            The map containing the generated data for various lists used on the page. Must not be null.
     */
    public DisciplinesGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @NotNull(message = "Timetables names map must not be null")
            Map<String, String> timetablesNames,
            @NotNull(message = "Lists data map must not be null")
            Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.disciplines = GUI.app.disciplinesService.getDisciplines();
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

    /**
     * Generates data for disciplines in the timetables.
     * Overrides the abstract method 'generate' inherited from the BaseGenerator class.
     * This method generates discipline data, including teacher information and associated student groups,
     * and updates the disciplines data in the listsData map.
     */
    public void generate(){
        String disciplinesData = utils.getBaseTemplateData("disciplines");
        disciplinesData = disciplinesData.replace("$generation_date", this.generationDateString);
        StringBuilder elementsData = new StringBuilder();

        for (Discipline discipline : this.disciplines){
            StringBuilder disciplineData = new StringBuilder();
            List<Teacher> teachers = new ArrayList<>();
            List<StudentGroup> studentGroups = new ArrayList<>();
            int credits = discipline.getCredits();

            String disciplineEntry = utils.getBaseTemplateData("atomics" + this.separator + "disciplines_entry");

            String name = Objects.requireNonNull(discipline.getName(), "Discipline name should not be null.");
            String timetableName = "timetable_d_" + name.toLowerCase().replace(" ", "_");

            disciplineEntry = disciplineEntry.replace("$discipline_name", name);
            disciplineEntry = disciplineEntry.replace("$discipline_ref", "./" + timetableName + ".html");

            for (Teacher teacher : discipline.getTeachers()){
                if (teacher.getType() == Teacher.Type.TEACHER && !teachers.contains(teacher)) teachers.add(teacher);
            }

            for (Session session : discipline.getSessions()){
                for (StudentGroup group : session.getGroups()){
                    if (!studentGroups.contains(group)) studentGroups.add(group);
                }
            }

            for (Teacher teacher : teachers) disciplineData.append(Objects.requireNonNull(teacher.getName(), "Teachers name should not be null.")).append(", ");
            for (StudentGroup group : studentGroups) disciplineData.append(group.getYear()).append(Objects.requireNonNull(group.getName(), "Group name should not be null.")).append(", ");
            if (disciplineData.length() > 2) disciplineData.setLength(disciplineData.length() - 2);

            disciplineEntry = disciplineEntry.replace("$discipline_data", disciplineData.toString());
            elementsData.append(disciplineEntry);

            this.addToDaysMap(timetableName);
            this.timetablesNames.put(timetableName, "Timetable of " + name + ", " + credits + " credits discipline");
        }

        disciplinesData = disciplinesData.replace("$elements", elementsData.toString());
        this.listsData.put("disciplines", disciplinesData);
    }
}
