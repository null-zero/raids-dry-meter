package com.raidsdrymeter.data;

import com.google.common.collect.ImmutableMultimap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum  UniqueItem {

	//Chambers of Xeric uniques
	//Common
	ARCANE_PRAYER_SCROLL(ItemID.ARCANE_PRAYER_SCROLL, RaidTab.COX, 0, 0.14495),
	DEXTEROUS_PRAYER_SCROLL(ItemID.DEXTEROUS_PRAYER_SCROLL, RaidTab.COX, 0, 0.14495),

	//Rare
	TWISTED_BUCKLER(ItemID.TWISTED_BUCKLER, RaidTab.COX, 0, 0.029),
	DRAGON_HUNTER_CROSSBOW(ItemID.DRAGON_HUNTER_CROSSBOW, RaidTab.COX, 0, 0.029),

	//Very Rare
	DINHS_BULWARK(ItemID.DINHS_BULWARK, RaidTab.COX, 1, 0.0087),

	ANCESTRAL_HAT(ItemID.ANCESTRAL_HAT, RaidTab.COX, 1, 0.0087),
	ANCESTRAL_ROBE_TOP(ItemID.ANCESTRAL_ROBE_TOP, RaidTab.COX, 1, 0.0087),
	ANCESTRAL_ROBE_BOTTOM(ItemID.ANCESTRAL_ROBE_BOTTOM, RaidTab.COX, 1, 0.0087),

	DRAGON_CLAWS(ItemID.DRAGON_CLAWS, RaidTab.COX, 2, 0.0087),

	//Mega Rare
	ELDER_MAUL(ItemID.ELDER_MAUL, RaidTab.COX, 2, 0.00966666),
	KODAI_INSIGNIA(ItemID.KODAI_INSIGNIA, RaidTab.COX, 2, 0.00966666),
	TWISTED_BOW(ItemID.TWISTED_BOW, RaidTab.COX, 2, 0.00966666),

	//Theater of Blood uniques
	//Common
	AVERNIC_DEFENDER_HILT(ItemID.AVERNIC_DEFENDER_HILT, RaidTab.TOB, 0, 0.0463),

	//Rare
	GHRAZI_RAPIER(ItemID.GHRAZI_RAPIER, RaidTab.TOB, 0, 0),
	SANGUINESTI_STAFF(ItemID.SANGUINESTI_STAFF_UNCHARGED, RaidTab.TOB, 0, ((double)8/19)),

	JUSTICIAR_FACEGUARD(ItemID.JUSTICIAR_FACEGUARD, RaidTab.TOB, 0, ((double)2/19)),
	JUSTICIAR_CHESTGUARD(ItemID.JUSTICIAR_CHESTGUARD, RaidTab.TOB, 1, ((double)2/19)),
	JUSTICIAR_LEGUARDS(ItemID.JUSTICIAR_LEGGUARDS, RaidTab.TOB, 1, ((double)2/19)),

	//Very Rare
	SCYTHE_OF_VITUR(ItemID.SCYTHE_OF_VITUR, RaidTab.TOB, 1, ((double)1/19)),

	//Tombs of Amascut uniques
	//Common
	OSMUMTENS_FANG(ItemID.OSMUMTENS_FANG, RaidTab.TOA, 0, ((double)1/3.429)),
	LIGHTBEARER(ItemID.LIGHTBEARER, RaidTab.TOA, 0, ((double)1/3.429)),

	//Rare
	ELIDINIS_WARD(ItemID.ELIDINIS_WARD, RaidTab.TOA, 0, ((double)1/8)),

	//Very Rare
	MASORI_MASK(ItemID.MASORI_MASK, RaidTab.TOA, 1, ((double)1/12)),
	MASORI_BODY(ItemID.MASORI_BODY, RaidTab.TOA, 1, ((double)1/12)),
	MASORI_CHAPS(ItemID.MASORI_CHAPS, RaidTab.TOA, 1, ((double)1/12)),

	//Mega Rare
	TUMEKENS_SHADOW_UNCHARGED(ItemID.TUMEKENS_SHADOW_UNCHARGED, RaidTab.TOA, 2, ((double)1/24))
	;

	private int itemID;
	private RaidTab[] raids;
	private int position;
	private double dropRate;
	private String name;
	private int price;
	private int linkedID;

	@Setter
	private int qty;

	private static final ImmutableMultimap<String, UniqueItem> RAID_MAP;
	static
	{
		final ImmutableMultimap.Builder<String, UniqueItem> map = ImmutableMultimap.builder();
		for (UniqueItem item : values())
		{
			for (RaidTab b : item.getRaids())
			{
				map.put(b.getName(), item);
			}
		}

		RAID_MAP = map.build();
	}

	UniqueItem(int id, RaidTab raid, int position, double dropRate)
	{
		this.itemID = id;
		this.raids = new RaidTab[]{raid};
		this.position = position;
		this.dropRate = dropRate;
	}

	public static void prepareUniqueItems(final ItemManager itemManager)
	{
		for (final UniqueItem item : values())
		{
			if (item.getName() != null)
			{
				return;
			}

			final ItemComposition c = itemManager.getItemComposition(item.getItemID());
			item.name = c.getName();
			item.linkedID = c.getLinkedNoteId();
			item.price = itemManager.getItemPrice(c.getId());
		}
	}

	public static List<Integer> getUniqueItemList(final ItemManager itemManager)
	{
		List<Integer> uniques = new ArrayList<>();
		for (final UniqueItem item : values())
		{
			uniques.add(item.getItemID());
		}

		return uniques;
	}

	public static Collection<UniqueItem> getUniquesForBoss(final String raidName)
	{
		return RAID_MAP.get(raidName);
	}

}
