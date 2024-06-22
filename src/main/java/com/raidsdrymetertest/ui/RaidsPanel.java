package com.raidsdrymetertest.ui;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.raidsdrymetertest.data.UniqueItem;
import com.raidsdrymetertest.data.UniqueLog;
import com.raidsdrymetertest.storage.RaidRecord;
import com.raidsdrymetertest.storage.UniqueEntry;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.QuantityFormatter;

public class RaidsPanel extends PluginPanel {

	@Inject
	private Client client;

    private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;

    private final ItemManager itemManager;
    private final UniqueLog uniqueLog;

    private int totalUniques = 0;
    private double totalGPFromUniques = 0;

	private com.raidsdrymetertest.features.pointstracker.PointsTracker pointsTracker;

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
		c.gridwidth = GridBagConstraints.REMAINDER;

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

        if(!uniqueLog.getRecords().isEmpty() && uniqueLog.getName().equals("Chambers of Xeric"))
//        if(uniqueLog.getName().equals("Chambers of Xeric"))
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

				holder = String.format("%d", uniqueLog.getRecords().stream().filter(r -> r.getRaidLevel() == 1).count());
				final DataPanel p15 = new DataPanel("cm count test:  ", holder);
				this.add(p15, c);
				c.gridy++;
            }
        }
        else if(uniqueLog.getName().equals("Theater of Blood"))
        {
                final DataPanel holder = new DataPanel("Coming soon!");
                this.add(holder, c);
                c.gridy++;
        }
        else if( !uniqueLog.getRecords().isEmpty() && uniqueLog.getName().equals("Tombs of Amascut"))
        {

            final int amount = uniqueLog.getRecords().size();
            final RaidRecord record = uniqueLog.getRecords().get(amount - 1);
            int personalRaidsDry = record.getPersonalRaidsDry();

            int averageRaidLevel = 0;
			int entryRaids = 0;
			int entryKC = 0;
			int normalRaids = 0;
			int normalKC = 0;
			int expertRaids = 0;
			int expertKC = 0;
            int personalPointsDry = 0;
            double personalRaidsOdds = 0;
            int totalPoints = 0;
            int personalStreak = 0;
			int raidLevelKC = 0;
			int totalKC = 0;

            if (uniqueLog.getRecords().size() > 0) {
                int raidLevel = 0;
                int x = 0;

                for (; x < uniqueLog.getRecords().size(); x++) {
					raidLevel = uniqueLog.getRecords().get(x).getRaidLevel();
					raidLevelKC = uniqueLog.getRecords().get(x).getKillCount();
                   	if (raidLevel <= 150) {
						   entryRaids++;
						   if (raidLevelKC > entryKC) {
							   entryKC = raidLevelKC;
						   }
					} else if (raidLevel > 300) {
						   expertRaids++;
						if (raidLevelKC > entryKC) {
							expertKC = raidLevelKC;
						}
					} else {
						if (raidLevelKC > entryKC) {
							normalKC = raidLevelKC;
						}
						   normalRaids++;
					}
                }
				totalKC = entryKC + expertKC + normalKC;
            }

            if(personalRaidsDry != 0)
            {
                int x = uniqueLog.getRecords().size() - personalRaidsDry;
				if (x < 0) {
					x = 0;
				}

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
				final DataPanel p = new DataPanel("Entry", "Normal", "Expert");
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(4, 0, 0, 0);
				c.gridy++;
				this.add(p, c);

				final DataPanel p1 = new DataPanel("KillCount: ", totalKC, entryKC, normalKC, expertKC);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(4, 0, 0, 0);
                c.gridy++;
				this.add(p1, c);

                final DataPanel p2 = new DataPanel("Logged KC: ", uniqueLog.getRecords().size(), entryRaids, normalRaids, expertRaids);
				c.insets = new Insets(4, 0, 0, 0);
				c.gridy++;
				c.ipadx = 0;
				c.ipady = 0;
				this.add(p2, c);

				final DataPanel p6 = new DataPanel("Total Uniques: ", totalUniques);
				c.insets = new Insets(0, 0, 0, 0);
				c.gridx = 0;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.gridy++;
				this.add(p6, c);

                String holder = String.format("%,.0f", (double) personalPointsDry);
                final DataPanel p7 = new DataPanel("Personal Points Dry: ", holder);
				c.gridy++;
				this.add(p7, c);

                final DataPanel p8 = new DataPanel("Personal Raids Dry: ", personalRaidsDry);
				c.gridy++;
                this.add(p8, c);

                holder = String.format("%,.2f", personalRaidsOdds);
                final DataPanel p9 = new DataPanel("Personal Raids Odds: ", holder);
				c.gridy++;
                this.add(p9, c);

                final DataPanel p10 = new DataPanel("Personal Raids Dry Streak: ", personalStreak);
				c.gridy++;
                this.add(p10, c);

                final DataPanel p12 = new DataPanel("Gp From Personal Uniques: ", QuantityFormatter.quantityToStackSize((long)totalGPFromUniques));
				c.gridy++;
                this.add(p12, c);

                holder = String.format("%,.2f", getEstimateGpPerPoint());
                final DataPanel p13 = new DataPanel("Est. Gp per Point: ", holder);
				c.gridy++;
                this.add(p13, c);

                holder = String.format("%,.2f", getActualGpPerPoints());
                final DataPanel p14 = new DataPanel("Actual. Gp Per Personal Point: ", holder);
				c.gridy++;
                this.add(p14, c);

				c.gridy++;
            }
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
