package org.presentation.generators;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.domain.models.Timeslot;

import java.util.Map;


/**
 * The HomeGenerator class is responsible for generating the home page data of the timetable system.
 * It extends the BaseGenerator class and provides implementations for the necessary methods.
 * The home page data includes information such as the generation date and version number,
 * which are dynamically inserted into the main template.
 * The generated data is then stored in the appropriate section of the listsData map.
 */
public class HomeGenerator extends BaseGenerator{
    @Min(value = 1, message = "Version must be greater than 0")
    private final int version;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    /**
     * Constructs a HomeGenerator object with the specified generation date string, timetables days data, version number, and lists data map.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     * @param version              The version number of the timetable system. Must be greater than 0.
     * @param listsData            The map containing the generated data for various lists used on the page. Must not be null.
     */
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

    /**
     * Generates the home page data for the timetable system.
     * Overrides the abstract method 'generate' inherited from the BaseGenerator class.
     * The home page data includes the generation date and version information,
     * which are inserted into the main template to create the final content for the home page.
     * The generated data is then stored in the listsData map.
     */
    public void generate() {
        String mainData = utils.getBaseTemplateData("main");
        mainData = mainData.replace("$generation_date", this.generationDateString);
        mainData = mainData.replace("$version", String.valueOf(this.version));
        this.listsData.put("main", mainData);
    }
}
