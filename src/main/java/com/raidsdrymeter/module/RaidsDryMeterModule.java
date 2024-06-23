package com.raidsdrymeter.module;

import com.raidsdrymeter.RaidsDryMeterConfig;
import com.raidsdrymeter.features.pointstracker.PartyPointsTracker;
import com.raidsdrymeter.features.pointstracker.PointsOverlay;
import com.raidsdrymeter.features.pointstracker.PointsTracker;
import com.raidsdrymeter.util.RaidStateTracker;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;

@Slf4j
public class RaidsDryMeterModule extends AbstractModule
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
	RaidsDryMeterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RaidsDryMeterConfig.class);
	}
}
