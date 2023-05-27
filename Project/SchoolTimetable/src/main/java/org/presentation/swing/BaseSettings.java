package org.presentation.swing;

import javax.swing.*;
/**
 * The BaseSettings interface represents the base settings for a specific component in the Timetable Generator application.
 * It defines methods to create JPanels for the main panel and the left and right panels of the component.
 */
public interface BaseSettings {
    JPanel createJPanel(JPanel main, String labelText);

    JPanel createLeftJPanel();
    JPanel createRightJPanel();
}
