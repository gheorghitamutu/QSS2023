package org.application.presentation.swing;

import javax.swing.*;

public interface BaseSettings {
    JPanel createJPanel(JPanel main, String labelText);

    JPanel createLeftJPanel();
    JPanel createRightJPanel();
}
