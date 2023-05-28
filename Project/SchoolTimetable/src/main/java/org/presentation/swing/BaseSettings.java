package org.presentation.swing;

import javax.swing.*;
/**
 * The BaseSettings interface represents the base settings for a specific component in the Timetable Generator application.
 * It defines methods to create JPanels for the main panel and the left and right panels of the component.
 */
public interface BaseSettings {

    /**
     * Creates a JPanel for the main panel of the component.
     * @param main The main panel of the component.
     * @param labelText The label text of the component.
     * @return The JPanel for the main panel of the component.
     */
    JPanel createJPanel(JPanel main, String labelText);

    /**
     * Creates a JPanel for the left panel of the component.
     * @return The JPanel for the left panel of the component.
     */
    JPanel createLeftJPanel();

    /**
     * Creates a JPanel for the right panel of the component.
     * @return The JPanel for the right panel of the component.
     */
    JPanel createRightJPanel();
}
