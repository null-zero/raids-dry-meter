package com.raidsdrymetertest.module;

import com.raidsdrymetertest.RaidsDryMeterTestConfig;
import com.raidsdrymetertest.features.pointstracker.PartyPointsTracker;
import com.raidsdrymetertest.features.pointstracker.PointsOverlay;
import com.raidsdrymetertest.features.pointstracker.PointsTracker;
import com.raidsdrymetertest.util.RaidStateTracker;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;

@Slf4j
public class RaidsDryMeterTestModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		Multibinder<PluginLifecycleComponent> lifecycleComponents = Multibinder.newSetBinder(binder(), PluginLifecycleComponent.class);
		lifecycleComponents.addBinding().to(PartyPointsTracker.class);
		lifecycleComponents.addBinding().to(PointsOverlay.class);
		lifecycleComponents.addBinding().to(PointsTracker.class);
		lifecycleComponents.addBinding().to(RaidStateTracker.class);
	}

	@Provides
	@Singleton
	RaidsDryMeterTestConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RaidsDryMeterTestConfig.class);
	}

}
