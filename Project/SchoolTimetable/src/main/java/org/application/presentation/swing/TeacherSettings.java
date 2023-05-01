package org.application.presentation.swing;

import javax.swing.*;
import java.awt.*;

public class TeacherSettings implements BaseSettings {
    public TeacherSettings(){

    }
    @Override
    public JPanel createJPanel(JPanel main, String labelText) {
        JLabel resultLabel = new JLabel(labelText + " clicked", SwingConstants.CENTER);
        resultLabel.setForeground(Color.BLACK);

        // Remove previous components from right panel and add result label
        main.removeAll();
        main.add(resultLabel);

        return main;
    }

    @Override
    public JPanel createLeftJPanel() {
        return null;
    }

    @Override
    public JPanel createRightJPanel() {
        return null;
    }
}
