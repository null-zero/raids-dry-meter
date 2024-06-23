package com.raidsdrymeter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(RaidsDryMeterConfig.CONFIG_GROUP)
public interface RaidsDryMeterConfig extends Config
{
	String CONFIG_GROUP = "RaidsDryMeterTest";
	@ConfigSection(
		name = "Points Tracker",
		description = "<html>Tracks points for the raid, used in calculating drop chance." +
			"<br/>NOTE: For teams, you MUST use the RuneLite Party plugin to receive team drop chance.</html>",
		position = 500
	)
	String SECTION_POINTS_TRACKER = "sectionPointsTracker";

	@ConfigItem(
		keyName = "pointsTrackerOverlayEnable",
		name = "Enable Overlay",
		description = "Show points earned within the raid.",
		position = 501,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerOverlayEnable()
	{
		return false;
	}

	@ConfigItem(
		keyName = "pointsTrackerShowRoomPoints",
		name = "Separate Room Points",
		description = "Show points for the current room separate from total points.",
		position = 502,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerShowRoomPoints()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pointsTrackerShowUniqueChance",
		name = "Show Unique %",
		description = "Show unique chance on the overlay.",
		position = 503,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerShowUniqueChance()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pointsTrackerShowPetChance",
		name = "Show Pet %",
		description = "Show pet chance on the overlay.",
		position = 504,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerShowPetChance()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pointsTrackerPostRaidMessage",
		name = "Points Total Message",
		description = "Show the total points in chat after the raid, akin to the Chambers of Xeric.",
		position = 505,
		section = SECTION_POINTS_TRACKER
	)
	default boolean pointsTrackerPostRaidMessage()
	{
		return true;
	}

}
