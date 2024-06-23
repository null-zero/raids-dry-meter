package com.raidsdrymeter.module;

import com.raidsdrymeter.RaidsDryMeterConfig;
import com.raidsdrymeter.util.RaidState;

public interface PluginLifecycleComponent
{

	default boolean isEnabled(RaidsDryMeterConfig config, RaidState raidState)
	{
		return true;
	}

	void startUp();

	void shutDown();

}
