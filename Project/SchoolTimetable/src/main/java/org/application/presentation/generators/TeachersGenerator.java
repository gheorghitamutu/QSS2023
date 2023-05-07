package org.application.presentation.generators;

import org.application.domain.models.Teacher;
import org.application.domain.models.Timeslot;
import org.application.presentation.GUI;

import java.util.List;
import java.util.Map;

public class TeachersGenerator extends BaseGenerator{
    private final List<Teacher> teachers;
    private final Map<String, String> timetablesNames;
    private final Map<String, String> listsData;

    public TeachersGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, Map<String, String> timetablesNames, Map<String, String> listsData){
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
}
