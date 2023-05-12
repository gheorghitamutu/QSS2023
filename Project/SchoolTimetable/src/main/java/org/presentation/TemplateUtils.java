package org.presentation;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class TemplateUtils {
    public TemplateUtils() {
    }

    private String getDataFromResources(String path) {
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

    public String getBaseTemplateData(String templateName) {
        String separator = "/";
        String currentPath = separator + "templates" + separator + templateName + ".html";

        return getDataFromResources(currentPath);
    }

    public void saveTemplateDataToFile(String templateData, String savePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(savePath));
            writer.write(templateData);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyStyle(String savePathBase) {
        String path = "/templates/style.css";
        String savePath = savePathBase + System.getProperty("file.separator") + "style.css";
        String data = getDataFromResources(path);
        saveTemplateDataToFile(data, savePath);
    }
}
