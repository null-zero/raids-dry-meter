package com.raidsdrymeter.ui;

import com.raidsdrymeter.data.RaidTab;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.AsyncBufferedImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collection;

public class SelectionPanel extends JPanel {

	private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
	private final static Color BUTTON_COLOR = ColorScheme.DARKER_GRAY_COLOR;
	private final static Color BUTTON_HOVER_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR;

	private final RaidsDryMeterPanel parent;
	private final ItemManager itemManager;

	SelectionPanel(
		final RaidsDryMeterPanel parent,
		final ItemManager itemManager
	)
	{
		this.parent = parent;
		this.itemManager = itemManager;

		this.setLayout(new GridBagLayout());
		this.setBackground(BACKGROUND_COLOR);

		createPanel();
	}

	private void createPanel()
	{
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 0, 0, 0);

		this.add(createRaidTabPanel(), c);
		c.gridy++;
	}

	private JPanel createRaidTabPanel()
	{
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 0, 0, 0);

		final JPanel container = new JPanel(new GridBagLayout());
		container.setBorder(new EmptyBorder(0, 0, 0, 0));

		container.add(createRaidTabs(), c);

		return container;
	}

	private JPanel createRaidTabs()
	{
		final JPanel container = new JPanel();

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;

		final MaterialTabGroup thisTabGroup = new MaterialTabGroup();
		thisTabGroup.setLayout(new GridLayout(0, 3, 7, 0));
		thisTabGroup.setBorder(new EmptyBorder(0, 0, 0, 0));

		final Collection<RaidTab> categoryTabs = RaidTab.getByCategoryName("Raids");
		for(final RaidTab tab : categoryTabs)
		{
			final MaterialTab materialTab = new MaterialTab("", thisTabGroup, null);
			materialTab.setName(tab.getName());
			materialTab.setToolTipText(tab.getName());
			materialTab.setOnSelectEvent(() ->
			{
				parent.setUniqueLogToNull();
				parent.requestUniqueLog(tab.getType(), tab.getName());
				return true;
			});


			final AsyncBufferedImage image = itemManager.getImage(tab.getItemID());
			final Runnable resize = () ->
			{
				materialTab.setIcon(new ImageIcon(image.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
				materialTab.setOpaque(true);
				materialTab.setBackground(Color.DARK_GRAY);
				materialTab.setHorizontalAlignment(SwingConstants.CENTER);
				materialTab.setVerticalAlignment(SwingConstants.CENTER);
				materialTab.setPreferredSize(new Dimension(55, 35));

			};

			image.onLoaded(resize);
			resize.run();

			thisTabGroup.addTab(materialTab);

		}
		container.add(thisTabGroup, c);

		return container;
	}


}
