package com.raidsdrymeter.ui;

import com.raidsdrymeter.RaidsDryMeterPlugin;
import com.raidsdrymeter.data.UniqueLog;
import com.raidsdrymeter.storage.RaidRecord;
import lombok.Getter;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.loottracker.LootRecordType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RaidsDryMeterPanel extends PluginPanel {

    private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
    private final static Color BUTTON_HOVER_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR;

    private final RaidsDryMeterPlugin plugin;

    private UniqueLog uniqueLog;

    private final ItemManager itemManager;

    RaidsPanel raidsPanel;

    SelectionPanel selectionPanel;

    EmptyPanel emptyPanel;

    @Getter
    private final JPanel namePanel = new JPanel();

    public RaidsDryMeterPanel(RaidsDryMeterPlugin raidsDryMeterPlugin, ItemManager itemManager)
    {
        super();

        this.itemManager = itemManager;

        this.plugin = raidsDryMeterPlugin;

        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setLayout(new BorderLayout());

        final GridBagConstraints c = createGridBag();

        showSelectionView();

        c.gridy++;


    }

    public void addLog(final RaidRecord r)
    {
        if (uniqueLog == null)
        {
            requestUniqueLog(r.getType(), r.getName());
        }
        else if (uniqueLog.getName().equalsIgnoreCase(r.getName()))
        {
            raidsPanel.addedRecord(r);
        }
    }


    public void useLog(final UniqueLog log)
    {
        uniqueLog = log;
        showRaidView();
    }

    public void requestUniqueLog(final LootRecordType type, final String name)
    {
        // For some reason removing all the components when there's a lot of names in the selectionPanel causes lag.
        // Removing them here seems to mitigate the lag

        plugin.requestUniqueLog(type, name);
    }


    public void refreshUI()
    {
        if(raidsPanel != null)
        {
            remove(raidsPanel);
            raidsPanel = null;
        }

		// Fixes bug when entering or leaving TOA rooms, panel sometimes resets to selection view
		if (uniqueLog != null) {
			useLog(uniqueLog);
		} else
		{
			showSelectionView();
		}
    }

    public void showSelectionView()
    {
        if(selectionPanel != null)
        {
            this.remove(selectionPanel);
            selectionPanel = null;
        }

        selectionPanel = new SelectionPanel(this, itemManager);

        final GridBagConstraints c = createGridBag();
        this.add(selectionPanel, c);
        c.gridy++;

        this.revalidate();
        this.repaint();
    }

    public void addEmptyPanel(GridBagConstraints c)
    {
        emptyPanel = new EmptyPanel("test");
        this.add(emptyPanel);

        this.revalidate();
        this.repaint();
    }


    public void showRaidView()
    {
        if(emptyPanel !=null) {

            this.remove(emptyPanel);
            emptyPanel = null;
        }

        if(raidsPanel != null)
        {
            this.remove(raidsPanel);
        }

        final GridBagConstraints c = createGridBag();

        raidsPanel = new RaidsPanel(uniqueLog, itemManager);

        c.gridy++;

        this.add(raidsPanel, c);

        this.revalidate();
        this.repaint();
    }

    private JLabel createIconLabel(final BufferedImage icon)
    {
        final JLabel label = new JLabel();
        label.setIcon(new ImageIcon(icon));
        label.setOpaque(true);
        label.setBackground(BACKGROUND_COLOR);

        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                label.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                label.setBackground(BACKGROUND_COLOR);
            }
        });

        return label;
    }

    public GridBagConstraints createGridBag()
    {
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.setLayout(new GridBagLayout());


        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        return c;
    }

    public void setUniqueLogToNull()
    {
        uniqueLog = null;
    }

}
