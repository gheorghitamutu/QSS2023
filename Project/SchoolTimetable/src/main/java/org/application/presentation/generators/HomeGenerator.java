package org.application.presentation.generators;

import org.application.domain.models.Timeslot;

import java.util.Map;

public class HomeGenerator extends BaseGenerator{
    private final int version;
    private final Map<String, String> listsData;

    public HomeGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, int version, Map<String, String> listsData) {
        super(generationDateString, timetablesDays);
        this.version = version;
        this.listsData = listsData;
    }

    public void generate() {
        String mainData = utils.getBaseTemplateData("main");
        mainData = mainData.replace("$generation_date", this.generationDateString);
        mainData = mainData.replace("$version", String.valueOf(this.version));
        this.listsData.put("main", mainData);
    }
}
