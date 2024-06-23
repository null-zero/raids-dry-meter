package com.raidsdrymeter.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.runelite.http.api.loottracker.LootRecordType;

import java.util.Collection;

@Data
@RequiredArgsConstructor
public class RaidRecord {
	private final String name;
	private final int killCount;
	private int raidLevel;
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
	public RaidRecord(String n, int kc, int ps,int pp, int tp, int prd, int trd, int pd, int td, LootRecordType t, Collection<UniqueEntry> d) {
		name = n;
		killCount = kc;
		partySize = ps;
		personalPoints = pp;
		teamPoints = tp;
		personalRaidsDry = prd;
		teamRaidsDry = trd;
		personalDeaths = pd;
		teamDeaths = td;
		type = t;
		uniques = d;
	}

	public RaidRecord(String n, int kc, int rl, int ps, int pp, int tp, int prd, int trd, int pd, int td, LootRecordType t, Collection<UniqueEntry> d) {
		name = n;
		killCount = kc;
		raidLevel = rl;
		partySize = ps;
		personalPoints = pp;
		teamPoints = tp;
		personalRaidsDry = prd;
		teamRaidsDry = trd;
		personalDeaths = pd;
		teamDeaths = td;
		type = t;
		uniques = d;
	}

	public void addUnique(UniqueEntry uniqueEntry)
	{
		uniques.add(uniqueEntry);
	}

}

