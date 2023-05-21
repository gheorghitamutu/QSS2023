package org.presentation.generators;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.domain.models.Timeslot;

import java.util.Map;

public class HomeGenerator extends BaseGenerator{
    @Min(value = 1, message = "Version must be greater than 0")
    private final int version;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    public HomeGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @Min(value = 1, message = "Version must be greater than 0")
            int version,
            @NotNull(message = "Lists data map must not be null")
            Map<String, String> listsData) {
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
