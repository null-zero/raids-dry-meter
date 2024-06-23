package com.raidsdrymeter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import com.google.inject.Binder;
import javax.inject.Inject;
import javax.swing.*;

import com.raidsdrymeter.data.RaidTab;
import com.raidsdrymeter.data.UniqueItem;
import com.raidsdrymeter.data.UniqueLog;
import com.raidsdrymeter.storage.RaidRecord;
import com.raidsdrymeter.storage.RecordWriter;
import com.raidsdrymeter.storage.UniqueEntry;
import com.raidsdrymeter.ui.RaidsDryMeterPanel;
import com.raidsdrymeter.features.pointstracker.PointsTracker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loottracker.LootReceived;

import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import net.runelite.http.api.loottracker.LootRecordType;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.raidsdrymeter.module.ComponentManager;
import com.raidsdrymeter.module.RaidsDryMeterModule;

@Slf4j
@PluginDescriptor(
	name = "Dry Meter for Raids",
	description = "Tracks how dry you are while raiding with different group sizes"
)
public class RaidsDryMeterPlugin extends Plugin
{
	private static final Pattern NUMBER_PATTERN = Pattern.compile("([0-9]+)");
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private SpriteManager spriteManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private RecordWriter writer;

	String raidType;

	private RuneScapeProfileType profileType;
	@Inject
	private PointsTracker pointsTracker;

	private RaidsDryMeterPanel panel;
	private NavigationButton navButton;

	@Getter
	private SetMultimap<LootRecordType, String> lootNames = HashMultimap.create();

	boolean personalUnique = false;
	boolean teamUnique = false;
	int partySize = 0;
	int raidLevel = 0;

	@Getter
	public static int invocationLevel = 0;
	List<RaidRecord> records;

	private boolean prepared = false;

	private Map<String, Integer> killCountMap = new HashMap<>();

	private ComponentManager componentManager = null;

	@Override
	public void configure(Binder binder)
	{
		binder.install(new RaidsDryMeterModule());
	}
	@Override
	protected void startUp() throws Exception
	{
		if (componentManager == null)
		{
			componentManager = injector.getInstance(ComponentManager.class);
		}
		componentManager.onPluginStart();
		panel = new RaidsDryMeterPanel(this, itemManager);
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/util/dry_raids_icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Dry Meter for Raids")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);

		if (!prepared)
		{
			prepared = true;
			clientThread.invokeLater(() ->
			{
				switch (client.getGameState())
				{
					case UNKNOWN:
					case STARTING:
						return false;
				}

				UniqueItem.prepareUniqueItems(itemManager);
				return true;
			});
		}

		if (client.getGameState().equals(GameState.LOGGED_IN) || client.getGameState().equals(GameState.LOADING))
		{
			updateWriterUsername();
		}

	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			onLoggedInGameState();
		}
//		} else if (event.getGameState() == GameState.LOGIN_SCREEN && previouslyLoggedIn) {
//			//this randomly fired at night hours after i had logged off...so i'm adding this guard here.
//			if (currentlyLoggedInAccount != null && client.getGameState() != GameState.LOGGED_IN) {
//				handleLogout();
//			}
//		}
	}

	private void onLoggedInGameState() {
		//keep scheduling this task until it returns true (when we have access to a display name)
		clientThread.invokeLater(() ->
		{
			//we return true in this case as something went wrong and somehow the state isn't logged in, so we don't
			//want to keep scheduling this task.
			if (client.getGameState() != GameState.LOGGED_IN) {
				return true;
			}

			final Player player = client.getLocalPlayer();

			//player is null, so we can't get the display name so, return false, which will schedule
			//the task on the client thread again.
			if (player == null) {
				return false;
			}

			final String name = player.getName();

			if (name == null) {
				return false;
			}

			if (name.equals("")) {
				return false;
			}

			profileType = RuneScapeProfileType.getCurrent(client);
			updateWriterUsername();
			//stops scheduling this task
			return true;
		});
	}

	@Subscribe
	public void onLootReceived(final LootReceived event)
	{

		if(event.getName().equals("Chambers of Xeric")) {

			Collection<UniqueEntry> drops;
			raidLevel = 0;
			partySize = client.getPlayers().size();

			drops = convertToUniqueRecords(event.getItems());

			int totalPoints = client.getVarbitValue(Varbits.TOTAL_POINTS);
			int personalPoints = client.getVarbitValue(Varbits.PERSONAL_POINTS);

			for (int uniqueId : UniqueItem.getUniqueItemList(itemManager)){
				for(ItemStack item : event.getItems())
				{
					if(item.getId() == uniqueId)
					{
						personalUnique = true;
					}
				}
			}

			int personalRaidsDry;
			int teamRaidsDry;

			records = new ArrayList<>(getDataByName(event.getType(), event.getName()));
			if(records.isEmpty())
			{
				personalRaidsDry = 0;
				teamRaidsDry = 0;
			}
			else {
				int index = records.size() - 1;
				personalRaidsDry = records.get(index).getPersonalRaidsDry();
				teamRaidsDry = records.get(index).getTeamRaidsDry();
			}

			if(personalUnique)
				personalRaidsDry = 0;
			else
				personalRaidsDry++;

			if(teamUnique)
				teamRaidsDry = 0;
			else
				teamRaidsDry++;

			final int kc = killCountMap.getOrDefault(event.getName().toUpperCase(), -1);
			final RaidRecord record = new RaidRecord(event.getName(), profileType.toString(), kc, partySize, personalPoints, totalPoints, personalRaidsDry,
				teamRaidsDry, 0, 0, event.getType(), drops);
			addRecord(record);

			personalUnique = false;
			teamUnique = false;


			SwingUtilities.invokeLater(() -> panel.refreshUI());
		} else if (event.getName().equals("Tombs of Amascut")) {

			Collection<UniqueEntry> drops;
			raidLevel = client.getVarbitValue(Varbits.TOA_RAID_LEVEL);
			partySize = client.getPlayers().size();

			drops = convertToUniqueRecords(event.getItems());

			int totalPoints = pointsTracker.getTotalPoints();
			int personalPoints = pointsTracker.getPersonalTotalPoints();

			for (int uniqueId : UniqueItem.getUniqueItemList(itemManager)){
				for(ItemStack item : event.getItems())
				{
					if(item.getId() == uniqueId)
					{
						personalUnique = true;
					}
				}
			}

			int personalRaidsDry;
			int teamRaidsDry;

			records = new ArrayList<>(getDataByName(event.getType(), event.getName()));
			if(records.isEmpty())
			{
				personalRaidsDry = 0;
				teamRaidsDry = 0;
			}
			else {
				int index = records.size() - 1;
				personalRaidsDry = records.get(index).getPersonalRaidsDry();
				teamRaidsDry = records.get(index).getTeamRaidsDry();
			}

			if(personalUnique)
				personalRaidsDry = 0;
			else
				personalRaidsDry++;

			if(teamUnique)
				teamRaidsDry = 0;
			else
				teamRaidsDry++;

			final int kc = killCountMap.getOrDefault(event.getName().toUpperCase(), -1);
			final RaidRecord record = new RaidRecord(event.getName(), profileType.toString() , kc, getInvocationLevel(), partySize, personalPoints, totalPoints, personalRaidsDry,
				teamRaidsDry, 0, 0, event.getType(), drops);
			addRecord(record);

			personalUnique = false;
			teamUnique = false;


			SwingUtilities.invokeLater(() -> panel.refreshUI());
		}
	}

	private void setRaidType(String raidType)
	{
		this.raidType = raidType;
	}

	private void updateWriterUsername()
	{
		writer.setPlayerUsername(Objects.requireNonNull(client.getLocalPlayer().getName()));
		localPlayerNameChanged();
	}

	private void localPlayerNameChanged()
	{
		lootNames = writer.getKnownFileNames();
		SwingUtilities.invokeLater(() -> panel.refreshUI());
	}

	private Collection<UniqueEntry> convertToUniqueRecords(Collection<ItemStack> stacks)
	{
		return stacks.stream().map(i -> createUniqueRecord(i.getId(), i.getQuantity())).collect(Collectors.toList());
	}

	private UniqueEntry createUniqueRecord(final int id, final int qty)
	{
		final ItemComposition c = itemManager.getItemComposition(id);
		final int realId = c.getNote() == -1 ? c.getId() : c.getLinkedNoteId();
		final int price = itemManager.getItemPrice(realId);
		return new UniqueEntry(c.getName(), id, qty, price);
	}

	private void addRecord(final RaidRecord record)
	{
		writer.addRaidRecord(record);
		lootNames.put(record.getType(), record.getName().toLowerCase());

		SwingUtilities.invokeLater(() -> panel.addLog(record));

	}

	public void requestUniqueLog(final LootRecordType type, final String name)
	{
		clientThread.invoke(() ->
		{
			final Collection<RaidRecord> records = getDataByName(type, name);
			final UniqueLog log = new UniqueLog(records, name);
			SwingUtilities.invokeLater(() -> panel.useLog(log));
		});
	}
	public Collection<RaidRecord> getDataByName(LootRecordType type, String name)
	{
		final RaidTab tab = RaidTab.getByName(name);
		if (tab != null)
		{
			name = tab.getName();
		}

		return writer.loadRaidTrackerRecords(type, name);
	}


	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		final String chatMessage = Text.removeTags(event.getMessage());
		Widget invoWidget = client.getWidget(WidgetID.TOA_RAID_GROUP_ID, 42);

		if(invoWidget != null) {
			String invoLevel = invoWidget.getText();
			invocationLevel = Integer.parseInt(invoLevel.replaceAll("[^0-9]", ""));
		}

		if (chatMessage.contains("Challenge Mode")) {
			raidLevel = 1;
		}

		// Raids KC
		if (chatMessage.startsWith("Your completed Chambers of Xeric"))
		{
			Matcher n = NUMBER_PATTERN.matcher(chatMessage);
			if (n.find())
			{
				killCountMap.put("CHAMBERS OF XERIC", Integer.valueOf(n.group()));
				return;
			}
		}

		// Tob KC
		if (chatMessage.startsWith("Your completed Theatre of Blood count is"))
		{
			Matcher n = NUMBER_PATTERN.matcher(chatMessage);
			if (n.find())
			{
				killCountMap.put("THEATRE OF BLOOD", Integer.valueOf(n.group()));
				return;
			}
		}

		// Toa KC
		if (chatMessage.startsWith("Your completed Tombs of Amascut"))
		{
			Matcher n = NUMBER_PATTERN.matcher(chatMessage);
			if (n.find())
			{
				killCountMap.put("TOMBS OF AMASCUT", Integer.valueOf(n.group()));
				return;
			}
		}

		if (chatMessage.startsWith("Special loot:"))
		{
			teamUnique = true;
		}
	}
}