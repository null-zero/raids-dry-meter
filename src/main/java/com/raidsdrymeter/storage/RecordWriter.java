package com.raidsdrymeter.storage;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import lombok.extern.slf4j.Slf4j;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.loottracker.LootRecordType;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.io.*;
import java.util.*;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

@Slf4j
@Singleton
public class RecordWriter {
	private static final String FILE_EXTENSION = ".log";
	private static final File LOOT_RECORD_DIR = new File(RUNELITE_DIR, "raids-dry-meter");

	private File playerFolder = LOOT_RECORD_DIR;

	private final Map<LootRecordType, File> eventFolders = new HashMap<>();
	private String name;

	@Inject
	public RecordWriter()
	{
		LOOT_RECORD_DIR.mkdir();
	}

	public void setPlayerUsername(final String username)
	{
		if (username.equalsIgnoreCase(name))
		{
			return;
		}

		playerFolder = new File(LOOT_RECORD_DIR, username);
		playerFolder.mkdir();
		name = username;
		createSubFolders();
	}

	private void createSubFolders()
	{
		eventFolders.clear();
		for (final LootRecordType type : LootRecordType.values())
		{

			if(type.name().equals("EVENT")) {
				final File folder = new File(playerFolder, type.name().toLowerCase());
				folder.mkdir();
				eventFolders.put(type, folder);
			}
		}
	}


	private static String raidNameToFileName(final String raidName)
	{
		return raidName.toLowerCase().trim() + FILE_EXTENSION;
	}

	public SetMultimap<LootRecordType, String> getKnownFileNames()
	{
		final SetMultimap<LootRecordType, String> fileNames = HashMultimap.create();

		for (final Map.Entry<LootRecordType, File> entry : eventFolders.entrySet())
		{
			final File[] files = entry.getValue().listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));
			if (files != null)
			{
				for (final File f : files)
				{
					fileNames.put(entry.getKey(), f.getName().replace(FILE_EXTENSION, ""));
				}
			}
		}

		return fileNames;
	}
	public synchronized Collection<RaidRecord> loadRaidTrackerRecords(LootRecordType recordType, String npcName)
	{
		return loadRaidTrackerRecords(npcName, eventFolders.get(recordType));
	}

	@Deprecated
	public synchronized Collection<RaidRecord> loadRaidTrackerRecords(String npcName, File folder)
	{
		final String fileName = raidNameToFileName(npcName);
		final File file = new File(folder, fileName);
		final Collection<RaidRecord> data = new ArrayList<>();

		try (final BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				// Skips the empty line at end of file
				if (line.length() > 0)
				{
					final RaidRecord r = RuneLiteAPI.GSON.fromJson(line, RaidRecord.class);
					if (!Objects.equals(r.getProfileType(), "STANDARD") && !Objects.equals(r.getProfileType(), null)) {
						continue;
					}

					if (r.getKillCount() == -1) {
						continue;
					}

					data.add(r);
				}
			}

		}
		catch (FileNotFoundException e)
		{
			log.debug("File not found: {}", fileName);
		}
		catch (IOException e)
		{
			log.warn("IOException for file {}: {}", fileName, e.getMessage());
		}

		return data;
	}

	public synchronized boolean addRaidRecord(RaidRecord rec)
	{
		if(rec.getName().equals("Chambers of Xeric") || rec.getName().equals("Theater of Blood") || rec.getName().equals("Tombs of Amascut")) {
			// Grab file
			final String fileName = raidNameToFileName(rec.getName());
			final File lootFile = new File(eventFolders.get(rec.getType()), fileName);

			// Convert entry to JSON
			final String dataAsString = RuneLiteAPI.GSON.toJson(rec);

			// Open File in append mode and write new data
			try {
				final BufferedWriter file = new BufferedWriter(new FileWriter(String.valueOf(lootFile), true));
				file.append(dataAsString);
				file.newLine();
				file.close();
				return true;
			} catch (IOException ioe) {
				log.warn("Error writing loot data to file {}: {}", fileName, ioe.getMessage());
				return false;
			}
		}
		return false;
	}

}
