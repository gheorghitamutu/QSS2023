package org.application.presentation;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TemplateUtils {
    private String templatesPath;

    public TemplateUtils() {
        createTemplatesPath();
    }

    private void createTemplatesPath() {
        StringBuilder basePath = new StringBuilder(System.getProperty("user.dir"));
        String separator = System.getProperty("file.separator");

        List<String> packages = List.of("src", "main", "java", "org", "application", "presentation", "templates");

        for (String pack : packages){
            basePath.append(separator).append(pack);
        }

        this.templatesPath = basePath.toString();
    }

    public String getBaseTemplateData(String templateName) {
        String separator = System.getProperty("file.separator");
        String currentPath = this.templatesPath + separator + templateName + ".html";
        File htmlTemplateFile = new File(currentPath);
        String htmlString;

        try {
            htmlString = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return htmlString;
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
}
