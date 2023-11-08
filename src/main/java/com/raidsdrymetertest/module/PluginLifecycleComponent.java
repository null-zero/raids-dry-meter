package com.raidsdrymetertest.module;

import com.raidsdrymetertest.RaidsDryMeterTestConfig;
import com.raidsdrymetertest.util.RaidState;

public interface PluginLifecycleComponent
{

	default boolean isEnabled(RaidsDryMeterTestConfig config, RaidState raidState)
	{
		return true;
	}

	void startUp();

	void shutDown();

}
