package com.raidsdrymetertest.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.runelite.http.api.loottracker.LootRecordType;

import java.util.Collection;

@Data
@AllArgsConstructor
public class RaidRecord {
    private final String name;
    private final int killCount;
    private final int partySize;
    private final int personalPoints;
    private final int teamPoints;
    private final int personalRaidsDry;
    private final int teamRaidsDry;
    private final int personalDeaths;
    private final int teamDeaths;
    private LootRecordType type;
    final Collection<UniqueEntry> uniques;

    public void addUnique(UniqueEntry uniqueEntry)
    {
        uniques.add(uniqueEntry);
    }

}
