package org.presentation.generators;

import org.domain.models.*;
import org.presentation.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisciplinesGenerator extends BaseGenerator{
    private final List<Discipline> disciplines;
    private final Map<String, String> timetablesNames;
    private final Map<String, String> listsData;

    public DisciplinesGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, Map<String, String> timetablesNames, Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.disciplines = GUI.app.disciplinesService.getDisciplines();
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

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

            String name = discipline.getName();
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

            for (Teacher teacher : teachers) disciplineData.append(teacher.getName()).append(", ");
            for (StudentGroup group : studentGroups) disciplineData.append(group.getYear()).append(group.getName()).append(", ");
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
