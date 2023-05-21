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

public class TemplateUtils {
    public TemplateUtils() {
    }

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

    @NotBlank(message = "Template data string must not be blank")
    public String getBaseTemplateData(
            @NotBlank(message = "Template name must not be blank")
            String templateName) {
        String separator = "/";
        String currentPath = separator + "templates" + separator + templateName + ".html";

        return getDataFromResources(currentPath);
    }

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

    public void copyStyle(
            @NotBlank(message = "Save path must not be blank")
            String savePathBase) {
        String path = "/templates/style.css";
        String savePath = savePathBase + System.getProperty("file.separator") + "style.css";
        String data = getDataFromResources(path);
        saveTemplateDataToFile(data, savePath);
    }
}
