package com.raidsdrymetertest.ui;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.raidsdrymetertest.data.UniqueItem;
import com.raidsdrymetertest.data.UniqueLog;
import com.raidsdrymetertest.storage.RaidRecord;
import com.raidsdrymetertest.storage.UniqueEntry;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RaidsPanel extends PluginPanel {

    private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;

    private final ItemManager itemManager;
    private final UniqueLog uniqueLog;

    private int totalUniques = 0;
    private double totalGPFromUniques = 0;

    RaidsPanel(
            final UniqueLog log,
            final ItemManager itemManager
    ){
        this.uniqueLog = log;
        this.itemManager = itemManager;

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(3, 5, 0, 5));
        setBackground(BACKGROUND_COLOR);

        createPanel(log);
    }

    private void createPanel(final UniqueLog uniqueLog)
    {
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        final Multimap<Integer, UniqueItem> positionMap = ArrayListMultimap.create();
        final Set<Integer> uniqueIds = new HashSet<>();

        for(final UniqueItem item : uniqueLog.getUniques())
        {
            final int id = item.getItemID();
            if (id != -1)
            {
                uniqueIds.add(id);
            }

            final int linkedId = item.getLinkedID();
            if (linkedId != -1)
            {
                uniqueIds.add(linkedId);
            }

            final UniqueEntry entry = uniqueLog.getConsolidated().get(id);
            final UniqueEntry notedEntry = uniqueLog.getConsolidated().get(linkedId);
            final int qty = (entry == null ? 0 : entry.getQuantity() + (notedEntry == null ? 0 : notedEntry.getQuantity()));
            item.setQty(qty);
            positionMap.put(item.getPosition(), item);
            totalUniques += qty;
            totalGPFromUniques += (item.getPrice() * qty);
        }

        for (final int position : positionMap.keySet())
        {
            final Collection<UniqueItem> uniques = positionMap.get(position);

            final UniquePanel p = new UniquePanel(uniques, this.itemManager, 35);
            this.add(p, c);
            c.gridy++;
        }

        if(uniqueLog.getRecords().size() > 0)
        {
            final int amount = uniqueLog.getRecords().size();
            final RaidRecord record = uniqueLog.getRecords().get(amount - 1);
            int personalRaidsDry = record.getPersonalRaidsDry();

            int personalPointsDry = 0;
            double personalRaidsOdds = 0;
            int totalPoints = 0;
            int personalStreak = 0;

            if(personalRaidsDry != 0)
            {
                int x = uniqueLog.getRecords().size() - personalRaidsDry;
                for(;x < uniqueLog.getRecords().size(); x++)
                {
                    totalPoints += uniqueLog.getRecords().get(x).getPersonalPoints();
                    if(totalPoints < 867600)
                    {
                        personalPointsDry += uniqueLog.getRecords().get(x).getPersonalPoints();
                    }
                    if(totalPoints > 867600)
                    {
                        totalPoints -= 867600;
                        personalStreak++;
                        personalPointsDry += uniqueLog.getRecords().get(x).getPersonalPoints();
                    }
                }

                personalRaidsOdds = 867600/(double)(personalPointsDry/personalRaidsDry);

            }

            if(record.getKillCount() != -1)
            {
                final DataPanel p = new DataPanel("KillCount: ", record.getKillCount());
                this.add(p, c);
                c.gridy++;

                System.out.println(uniqueLog.getName() + uniqueLog.getRecords().size());
                final DataPanel p2 = new DataPanel("Logged KC: ", uniqueLog.getRecords().size());
                this.add(p2, c);
                c.gridy++;

                final DataPanel p3 = new DataPanel("Total Uniques: ", totalUniques);
                this.add(p3, c);
                c.gridy++;

                String holder = String.format("%,.0f", (double) personalPointsDry);
                final DataPanel p4 = new DataPanel("Personal Points Dry: ", holder);
                this.add(p4, c);
                c.gridy++;

                final DataPanel p6 = new DataPanel("Personal Raids Dry: ", personalRaidsDry);
                this.add(p6, c);
                c.gridy++;

                holder = String.format("%,.2f", personalRaidsOdds);
                final DataPanel p8 = new DataPanel("Personal Raids Odds: ", holder);
                this.add(p8, c);
                c.gridy++;

                final DataPanel p9 = new DataPanel("Personal Raids Dry Streak: ", personalStreak);
                this.add(p9, c);
                c.gridy++;

                final DataPanel p12 = new DataPanel("Gp From Personal Uniques: ", QuantityFormatter.quantityToStackSize((long)totalGPFromUniques));
                this.add(p12, c);
                c.gridy++;

                holder = String.format("%,.2f", getEstimateGpPerPoint());
                final DataPanel p13 = new DataPanel("Est. Gp per Point: ", holder);
                this.add(p13, c);
                c.gridy++;

                holder = String.format("%,.2f", getActualGpPerPoints());
                final DataPanel p14 = new DataPanel("Actual. Gp Per Personal Point: ", holder);
                this.add(p14, c);
                c.gridy++;

            }
        }
        else if(uniqueLog.getName().equals("Theater of Blood"))
        {
                final DataPanel holder = new DataPanel("Coming soon!");
                this.add(holder, c);
                c.gridy++;
        }
    }

    void addedRecord(final RaidRecord record)
    {
        uniqueLog.addRecord(record);

        // TODO: Smarter update system so it only repaints necessary Item and Text Panels
        this.removeAll();
        this.createPanel(uniqueLog);

        this.revalidate();
        this.repaint();
    }

    double getEstimateGpPerPoint()
    {
        double gpPerPoint = 0;
        List<UniqueItem> uniqueItems = new ArrayList<>(UniqueItem.getUniquesForBoss("Chambers of Xeric"));
        for (UniqueItem uniqueItem : uniqueItems) {
            double dropRate = uniqueItem.getDropRate();
            gpPerPoint += (uniqueItem.getPrice() / (712500 / dropRate));
        }
        return gpPerPoint;
    }

    double getActualGpPerPoints()
    {
        double gp = 0;
        List<RaidRecord> records = new ArrayList<>(uniqueLog.getRecords());
        List<UniqueItem> uniqueItems = new ArrayList<>(UniqueItem.getUniquesForBoss("Chambers of Xeric"));
        double totalPoints = 0;
        for (RaidRecord record : records)
        {
            List<UniqueEntry> uniqueDrops = new ArrayList<>(record.getUniques());
            totalPoints += record.getPersonalPoints();
            for(UniqueEntry uniqueDrop : uniqueDrops)
            {
                for(UniqueItem uniqueItem : uniqueItems)
                {
                    if(uniqueDrop.getId() == uniqueItem.getItemID())
                        gp += uniqueItem.getPrice();
                }
            }

        }

        return gp / totalPoints;
    }
}
