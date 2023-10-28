package com.raidsdrymetertest;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RaidsDryMeterTestPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(RaidsDryMeterTestPlugin.class);
		RuneLite.main(args);
	}
}