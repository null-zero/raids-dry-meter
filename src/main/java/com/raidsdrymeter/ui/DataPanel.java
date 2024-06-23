package com.raidsdrymeter.ui;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DataPanel extends JPanel {

	private static final Border PANEL_BORDER = BorderFactory.createMatteBorder(3, 0, 3, 0, ColorScheme.DARK_GRAY_COLOR);
	private static final Color PANEL_BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;

	private static final Border CONTAINER_BORDER = BorderFactory.createMatteBorder(0, 15, 0, 15, PANEL_BACKGROUND_COLOR);

	JLabel total;
	JLabel total2;
	JLabel total3;
	JLabel total4;

	Border symmetricalDashedBorder = new SymmetricalDashedBorder(new float[]{10, 6}, 4, ColorScheme.DARK_GRAY_COLOR, 3);

	DataPanel(String text, int data, int data2, int data3)
	{
		setLayout(new GridBagLayout());
		setBorder(PANEL_BORDER);
		setBackground(PANEL_BACKGROUND_COLOR);

		JLabel textJ = new JLabel(text, SwingConstants.CENTER);
		total = new JLabel(QuantityFormatter.quantityToStackSize(data), SwingConstants.CENTER);
		total2 = new JLabel(QuantityFormatter.quantityToStackSize(data2), SwingConstants.CENTER);
		total3 = new JLabel(QuantityFormatter.quantityToStackSize(data3), SwingConstants.CENTER);

		JPanel panel = createPanel(2, 1);
		setBorder(new EmptyBorder(3, 0, 3, 0));

		panel.add(textJ);
		panel.add(total);

		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 8, 0, 8);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 5;
		c.weightx = 1;
		c.ipady = 0;
		this.add(panel, c);

		JPanel panel2 = createPanel(1, 1);
		panel2.add(total2);
		Border matteBorder = BorderFactory.createMatteBorder(4, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR);
		panel2.setBorder(new CompoundBorder(matteBorder,
			new SymmetricalDashedBorder(new float[]{10, 6}, 4, ColorScheme.DARK_GRAY_COLOR, 2)
		));
		c.insets = new Insets(6, 8, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 40;
		c.ipady = 15;
		c.gridx = 1;
		c.gridy = 1;
		this.add(panel2, c);

		JPanel panel3 = createPanel(1, 1);
		matteBorder = BorderFactory.createMatteBorder(4, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR);

		panel3.add(total3);
		panel3.setBorder(new CompoundBorder(matteBorder,
			new SymmetricalDashedBorder(new float[]{10, 6}, 4, PANEL_BACKGROUND_COLOR, 1)
		));
		c.insets = new Insets(6, 0, 0, 8);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 40;
		c.ipady = 15;
		c.gridx = 2;
		c.gridy = 1;
		this.add(panel3, c);
	}

	DataPanel(String text, int data, int data2, int data3, int data4)
	{
		setLayout(new GridBagLayout());
		setBorder(PANEL_BORDER);
		setBackground(PANEL_BACKGROUND_COLOR);

		JLabel textJ = new JLabel(text, SwingConstants.CENTER);
		total = new JLabel(QuantityFormatter.quantityToStackSize(data), SwingConstants.CENTER);

		total2 = new JLabel(QuantityFormatter.quantityToStackSize(data2), SwingConstants.CENTER);

		total3 = new JLabel(QuantityFormatter.quantityToStackSize(data3), SwingConstants.CENTER);

		total4 = new JLabel(QuantityFormatter.quantityToStackSize(data4), SwingConstants.CENTER);

		JPanel panel = createPanel(2, 1);
		setBorder(new EmptyBorder(3, 0, 3, 0));

		panel.add(textJ);
		panel.add(total);

		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 8, 0, 8);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 5;
		c.weightx = 1;
		c.ipady = 0;
		this.add(panel, c);

		JPanel panel2 = createPanel(1, 1);
		panel2.add(total2);
		panel2.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR));
		c.insets = new Insets(6, 8, 4, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 40;
		c.ipady = 15;
		c.gridx = 1;
		c.gridy = 1;
		this.add(panel2, c);

		JPanel panel3 = createPanel(1, 1);
		Border matteBorder = BorderFactory.createMatteBorder(4, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR); //left 2 right 2

		panel3.setBorder(new CompoundBorder(matteBorder, symmetricalDashedBorder));
		panel3.add(total3);

		c.insets = new Insets(6, 0, 4, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 40;
		c.ipady = 15;
		c.gridx = 2;
		c.gridy = 1;
		this.add(panel3, c);

		JPanel panel4 = createPanel(1, 1);
		panel4.add(total4);
		panel4.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR));
		c.insets = new Insets(6, 0, 4, 8);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.ipadx = 40;
		c.weightx = 0.3;
		c.ipady = 15;
		c.gridx = 3;
		c.gridy = 1;
		this.add(panel4, c);
	}

	DataPanel(String text1, String text2, String text3)
	{
		setLayout(new GridBagLayout());
		setBorder(PANEL_BORDER);
		setBackground(PANEL_BACKGROUND_COLOR);

		JLabel textJ1 = new JLabel(text1, SwingConstants.CENTER);
		JLabel textJ2 = new JLabel(text2, SwingConstants.CENTER);
		JLabel textJ3 = new JLabel(text3, SwingConstants.CENTER);


		JPanel panel1 = createPanel(1, 1);
		panel1.add(textJ1);
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(8, 0, 8, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 35;
		c.ipady = 15;
		this.add(panel1, c);

		JPanel panel2 = createPanel(1, 1);
		Border empty = BorderFactory.createEmptyBorder(-5, 1, -5, 1);
		panel2.setBorder(new CompoundBorder(empty, symmetricalDashedBorder));
		panel2.add(textJ2);
		c.insets = new Insets(8, 2, 4, 2);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 60;
		c.ipady = 15;
		this.add(panel2, c);

		JPanel panel3 = createPanel(1, 1);
		panel3.add(textJ3);
		c.insets = new Insets(8, 0, 4, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 35;
		c.ipady = 15;
		this.add(panel3, c);
	}

	DataPanel(String text, int data)
	{
		setLayout(new GridBagLayout());
		setBorder(PANEL_BORDER);
		setBackground(PANEL_BACKGROUND_COLOR);

		JLabel textJ = new JLabel(text, SwingConstants.CENTER);

		total = new JLabel(QuantityFormatter.quantityToStackSize(data), SwingConstants.CENTER);

		JPanel panel = createPanel(2, 1);

		panel.add(textJ);
		panel.add(total);

		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 0, 4, 8);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 40;
		c.ipady = 15;

		this.add(panel, c);

	}

	DataPanel(String text1, String text2, boolean bool)
	{
		setLayout(new GridBagLayout());
		setBorder(PANEL_BORDER);
		setBackground(PANEL_BACKGROUND_COLOR);

		JLabel textJ1 = new JLabel(text1, SwingConstants.CENTER);
		JLabel textJ2 = new JLabel(text2, SwingConstants.CENTER);



		JPanel panel1 = createPanel(1, 1);
		Border symmetricalDashedBorder = new SymmetricalDashedBorder(new float[]{10, 6}, 4, ColorScheme.DARK_GRAY_COLOR, 2);
		Border empty = BorderFactory.createEmptyBorder(-5, 0, -5, 0);
		panel1.setBorder(new CompoundBorder(empty, symmetricalDashedBorder));
		panel1.add(textJ1);
		final GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(8, 0, 8, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 35;
		c.ipady = 15;
		this.add(panel1, c);

		JPanel panel2 = createPanel(1, 1);
		panel2.add(textJ2);
		c.insets = new Insets(8, 0, 8, 0);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.ipadx = 35;
		c.ipady = 15;
		this.add(panel2, c);
	}
	DataPanel(String text, String formattedData)
	{
		setLayout(new GridBagLayout());
		setBorder(PANEL_BORDER);
		setBackground(PANEL_BACKGROUND_COLOR);

		JLabel textJ = new JLabel(text, SwingConstants.CENTER);

		total = new JLabel("" + formattedData, SwingConstants.CENTER);

		JPanel panel = createPanel(2, 1);

		panel.add(textJ);
		panel.add(total);

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(6, 0, 4, 8);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 40;
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

		JPanel panel = createPanel(2, 1);

		panel.add(textJ);

		this.add(panel, BorderLayout.CENTER);

	}

	private static JPanel createPanel(int rows, int cols)
	{
		final JPanel panel = new JPanel(new GridLayout(rows,cols,1,1));
		panel.setBorder(CONTAINER_BORDER);
		panel.setBackground(PANEL_BACKGROUND_COLOR);

		return panel;
	}

	void updateKC(int data)
	{
		total.setText(QuantityFormatter.quantityToStackSize(data));
	}

}
