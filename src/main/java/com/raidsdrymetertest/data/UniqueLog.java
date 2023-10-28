package com.raidsdrymetertest.data;

import com.raidsdrymetertest.storage.RaidRecord;
import com.raidsdrymetertest.storage.UniqueEntry;
import lombok.Getter;
import net.runelite.http.api.loottracker.LootRecordType;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class UniqueLog {
    private final String name;
    private final LootRecordType type;

    private final List<RaidRecord> records = new ArrayList<>();
    private final Map<Integer, UniqueEntry> consolidated = new HashMap<>();
    private final Collection<UniqueItem> uniques;

    public UniqueLog(final Collection<RaidRecord> records, final String name)
    {
        this.records.addAll(records);
        this.name = name;

        if(records.size() == 0)
        {
            this.type = LootRecordType.UNKNOWN;
        }
        else
        {
            final RaidRecord record = this.records.get(0);
            this.type = record.getType();
        }

        for(final RaidRecord record : records)
        {
            for(final UniqueEntry entry : record.getUniques())
            {
                addItemEntryToMap(entry);
            }
        }

        final Collection<UniqueItem> unsorted = UniqueItem.getUniquesForBoss(name);
        if (unsorted == null)
        {
            uniques = new ArrayList<>();
            return;
        }

        uniques = unsorted.stream().sorted(Comparator.comparingInt(UniqueItem::getPosition)).collect(Collectors.toList());
    }

    public synchronized void addRecord(final RaidRecord record)
    {
        records.add(record);
        for (final UniqueEntry entry : record.getUniques())
        {
            addItemEntryToMap(entry);
        }
    }

    private void addItemEntryToMap(UniqueEntry unique)
    {
        final String itemNameLowercased = unique.getName().toLowerCase();

        if(type != null)
        {
            unique = new UniqueEntry(unique.getName(), unique.getId(), unique.getQuantity(), unique.getPrice());
        }

        final UniqueEntry oldEntry = consolidated.get(unique.getId());
        if(oldEntry != null)
        {
            oldEntry.setQuantity(oldEntry.getQuantity() + unique.getQuantity());
        }
        else
        {
            consolidated.put(unique.getId(), new UniqueEntry(unique.getName(), unique.getId(), unique.getQuantity(), unique.getPrice()));
        }
    }
}
