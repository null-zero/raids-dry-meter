package com.raidsdrymeter.ui;

import lombok.Setter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.crypto.Data;
import java.awt.*;

public class DataPanel extends JPanel {

    private static final Border PANEL_BORDER = BorderFactory.createMatteBorder(3, 0, 3, 0, ColorScheme.DARK_GRAY_COLOR);
    private static final Color PANEL_BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;

    private static final Border CONTAINER_BORDER = BorderFactory.createMatteBorder(0, 15, 0, 15, PANEL_BACKGROUND_COLOR);

    JLabel total;

    DataPanel(String text, int data)
    {
        setLayout(new GridBagLayout());
        setBorder(PANEL_BORDER);
        setBackground(PANEL_BACKGROUND_COLOR);

        JLabel textJ = new JLabel(text, SwingConstants.CENTER);

        total = new JLabel(QuantityFormatter.quantityToStackSize(data), SwingConstants.CENTER);

        JPanel panel = createPanel();

        panel.add(textJ);
        panel.add(total);

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 15;

        this.add(panel, c);

    }

    DataPanel(String text, String formattedData)
    {
        setLayout(new GridBagLayout());
        setBorder(PANEL_BORDER);
        setBackground(PANEL_BACKGROUND_COLOR);

        JLabel textJ = new JLabel(text, SwingConstants.CENTER);

        total = new JLabel("" + formattedData, SwingConstants.CENTER);

        JPanel panel = createPanel();

        panel.add(textJ);
        panel.add(total);

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 15;

        this.add(panel, c);

    }

    DataPanel(String text)
    {
        setLayout(new BorderLayout());
        setBorder(PANEL_BORDER);
        setBackground(PANEL_BACKGROUND_COLOR);

        JLabel textJ = new JLabel(text, SwingConstants.CENTER);

        textJ.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = createPanel();

        panel.add(textJ);

        this.add(panel, BorderLayout.CENTER);

    }

    private static JPanel createPanel()
    {
        final JPanel panel = new JPanel(new GridLayout(2,1,1,1));
        panel.setBorder(CONTAINER_BORDER);
        panel.setBackground(PANEL_BACKGROUND_COLOR);

        return panel;
    }

    void updateKC(int data)
    {
        total.setText(QuantityFormatter.quantityToStackSize(data));
    }

}
