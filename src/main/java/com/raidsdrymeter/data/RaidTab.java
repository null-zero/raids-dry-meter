package com.raidsdrymeter.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.http.api.loottracker.LootRecordType;


import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum RaidTab {

    COX("Chambers of Xeric", ItemID.OLMLET, "Raids", LootRecordType.EVENT),
    TOB("Theater of Blood", ItemID.LIL_ZIK, "Raids", LootRecordType.EVENT),
	TOA("Tombs of Amascut", ItemID.TUMEKENS_GUARDIAN, "Raids", LootRecordType.EVENT);

    private final String name;
    private final int itemID;
    private final String category;
    private final LootRecordType type;

    private static final Map<String, RaidTab> NAME_MAP;
    private static final Multimap<String, RaidTab> CATEGORY_MAP;
    static
    {
        final ImmutableMap.Builder<String, RaidTab> byName = ImmutableMap.builder();
        final ImmutableMultimap.Builder<String, RaidTab> categoryMap = ImmutableMultimap.builder();

        for (RaidTab tab : values())
        {
            byName.put(tab.getName().toUpperCase(), tab);
            categoryMap.put(tab.getCategory(), tab);

        }

        NAME_MAP = byName.build();
        CATEGORY_MAP = categoryMap.build();
    }

    public static RaidTab getByName(final String name)
    {
        return NAME_MAP.get(name.toUpperCase());
    }

    public static Collection<RaidTab> getByCategoryName(final String name)
    {
        return CATEGORY_MAP.get(name);
    }
}
