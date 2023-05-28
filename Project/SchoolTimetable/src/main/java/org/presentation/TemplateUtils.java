package org.presentation;

import org.apache.commons.io.FileUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;


/**
 * Utility class for handling template-related operations.
 * This class provides various methods for retrieving, saving, and copying template data.
 */
public class TemplateUtils {

    /**
     * Constructs a new TemplateUtils object.
     */
    public TemplateUtils() {
    }

    /**
     * Retrieves data from a resource file.
     * This method reads the contents of a resource file located at the specified path and returns the data as a string.
     *
     * @param path  The path to the resource file. Must not be blank.
     * @return The data read from the resource file.
     * @throws RuntimeException if there is an error accessing or reading the resource file.
     */
    @NotBlank(message = "Resources data string must not be blank")
    private String getDataFromResources(
            @NotBlank(message = "Resources load path must not be blank")
            String path) {
        File dataFile;
        String dataString;

        try {
            dataFile = new File(Objects.requireNonNull(TemplateUtils.class.getResource(path)).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            dataString = FileUtils.readFileToString(dataFile, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dataString;
    }

    /**
     * Retrieves base template data.
     * This method retrieves the base template data by loading the contents of a template file with a specified name.
     *
     * @param templateName The name of the template. Must not be blank.
     * @return The base template data as a string.
     */
    @NotBlank(message = "Template data string must not be blank")
    public String getBaseTemplateData(
            @NotBlank(message = "Template name must not be blank")
            String templateName) {
        String separator = "/";
        String currentPath = separator + "templates" + separator + templateName + ".html";

        return getDataFromResources(currentPath);
    }

    /**
     * Saves template data to a file.
     * This method saves the provided template data to a file at the specified save path.
     *
     * @param templateData The template data string to be saved. Must not be empty.
     * @param savePath     The path where the template data will be saved. Must not be blank.
     * @throws RuntimeException if there is an error saving the template data to the file.
     */
    public void saveTemplateDataToFile(
            @NotEmpty(message = "Template data string must not be empty")
            String templateData,
            @NotBlank(message = "Save path must not be blank")
            String savePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(savePath));
            writer.write(templateData);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copies the style file to a specified save path.
     * This method retrieves the style file data and saves it to the provided save path.
     *
     * @param savePathBase The base path where the style file will be copied. Must not be blank.
     * @throws RuntimeException if there is an error copying the style file.
     */
    public void copyStyle(
            @NotBlank(message = "Save path must not be blank")
            String savePathBase) {
        String path = "/templates/style.css";
        String savePath = savePathBase + System.getProperty("file.separator") + "style.css";
        String data = getDataFromResources(path);
        saveTemplateDataToFile(data, savePath);
    }
}
