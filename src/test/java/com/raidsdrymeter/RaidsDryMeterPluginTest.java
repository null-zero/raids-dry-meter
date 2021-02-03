package com.raidsdrymeter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RaidsDryMeterPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(RaidsDryMeterPlugin.class);
		RuneLite.main(args);
	}
}