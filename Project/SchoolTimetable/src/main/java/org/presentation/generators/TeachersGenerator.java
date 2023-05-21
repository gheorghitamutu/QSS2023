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

public class TeachersGenerator extends BaseGenerator{
    @NotNull(message = "Teachers list must not be null")
    private final List<@Valid Teacher> teachers;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

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
