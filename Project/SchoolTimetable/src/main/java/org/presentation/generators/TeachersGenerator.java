package org.presentation.generators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.domain.models.Teacher;
import org.domain.models.Timeslot;
import org.presentation.GUI;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * The TeachersGenerator class is responsible for generating teacher-related information and timetables.
 * It extends the BaseGenerator class and implements the necessary logic to generate teachers-related data and templates.
 * It generates teacher lists and their corresponding timetables based on the provided data,
 * and updates the teachers data in the listsData map.
 */
public class TeachersGenerator extends BaseGenerator{
    @NotNull(message = "Teachers list must not be null")
    private final List<@Valid Teacher> teachers;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    /**
     * Constructs a TeachersGenerator object with the specified generation date string, timetables days data, timetables names map, and lists data map.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     * @param timetablesNames      The map containing the timetable title organized by table names. Must not be null.
     * @param listsData            The map containing the generated data for various lists used on the page. Must not be null.
     */
    public TeachersGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @NotNull(message = "Timetables names map must not be null")
            Map<String, String> timetablesNames,
            @NotNull(message = "Lists data map must not be null")
            Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.teachers = GUI.app.teachersService.getTeachers();
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

    /**
     * Generates the teacher-related information and timetables.
     * Overrides the abstract method 'generate' inherited from the BaseGenerator class.
     * It populates the listsData map with the generated data.
     * The generated data includes teacher names and their corresponding timetables.
     */
    public void generate(){
        String teachersData = utils.getBaseTemplateData("teachers");
        teachersData = teachersData.replace("$generation_date", this.generationDateString);
        StringBuilder elementsData = new StringBuilder();

        for (Teacher teacher : this.teachers){
            String teacherEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

            String name = Objects.requireNonNull(teacher.getName(), "Teacher name should not be null.");
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
}
