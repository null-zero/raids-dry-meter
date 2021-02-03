package com.raidsdrymeter.ui;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmptyPanel extends JPanel {

    private static final GridBagLayout LAYOUT = new GridBagLayout();

    private static final Border PANEL_BORDER = BorderFactory.createMatteBorder(3, 0, 3, 0, ColorScheme.DARK_GRAY_COLOR);
    private static final Color PANEL_BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;

    private static final Border CONTAINER_BORDER = BorderFactory.createMatteBorder(0, 15, 0, 15, PANEL_BACKGROUND_COLOR);

    EmptyPanel(String text) {

        setLayout(LAYOUT);
        setBorder(PANEL_BORDER);
        setBackground(PANEL_BACKGROUND_COLOR);

        JLabel textJ = new JLabel(text, SwingConstants.LEFT);

        JPanel panel = createPanel();

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 20;

        this.add(panel, c);
    }

    private static JPanel createPanel()
    {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(CONTAINER_BORDER);
        panel.setBackground(PANEL_BACKGROUND_COLOR);

        return panel;
    }
}
