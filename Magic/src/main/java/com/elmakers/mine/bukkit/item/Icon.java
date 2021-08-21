package com.elmakers.mine.bukkit.item;

import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.api.block.MaterialAndData;
import com.elmakers.mine.bukkit.api.item.ItemData;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.spell.BaseSpell;

public class Icon implements com.elmakers.mine.bukkit.api.item.Icon {
    private final MageController controller;
    private final String itemKey;
    private final String itemDisabledKey;
    private final String legacyItemKey;
    private final String legacyItemDisabledKey;
    private final String url;
    private final String urlDisabled;
    private final String glyph;
    private final boolean useUrl;

    public Icon(MageController controller) {
        this.controller = controller;
        itemKey = BaseSpell.DEFAULT_SPELL_ICON.name().toLowerCase();
        itemDisabledKey = null;
        legacyItemKey = null;
        legacyItemDisabledKey = null;
        url = null;
        urlDisabled = BaseSpell.DEFAULT_DISABLED_ICON_URL;
        glyph = null;
        useUrl = false;
    }

    public Icon(MageController controller, ConfigurationSection configuration) {
        this.controller = controller;
        itemKey = configuration.getString("item");
        itemDisabledKey = configuration.getString("item_disabled");
        legacyItemKey = configuration.getString("legacy_item");
        legacyItemDisabledKey = configuration.getString("legacy_item_disabled");
        url = configuration.getString("url");
        urlDisabled = configuration.getString("url_disabled");
        glyph = configuration.getString("glyph");
        useUrl = configuration.getBoolean("force_url", false);
    }

    public Icon(com.elmakers.mine.bukkit.api.item.Icon defaultAPI, com.elmakers.mine.bukkit.api.item.Icon baseAPI) {
        if (!(baseAPI instanceof Icon) || !(defaultAPI instanceof Icon)) {
            throw new IllegalStateException("Icon is not the correct implementation type");
        }
        Icon defaultIcon = (Icon)defaultAPI;
        Icon baseIcon = (Icon)baseAPI;
        this.controller = defaultIcon.controller;
        itemKey = baseIcon.itemKey != null ? baseIcon.itemKey : defaultIcon.itemKey;
        itemDisabledKey = baseIcon.itemDisabledKey != null ? baseIcon.itemDisabledKey : defaultIcon.itemDisabledKey;
        legacyItemKey = baseIcon.legacyItemKey != null ? baseIcon.legacyItemKey : defaultIcon.legacyItemKey;
        legacyItemDisabledKey = baseIcon.legacyItemDisabledKey != null ? baseIcon.legacyItemDisabledKey : defaultIcon.legacyItemDisabledKey;
        url = baseIcon.url != null ? baseIcon.url : defaultIcon.url;
        urlDisabled = baseIcon.urlDisabled != null ? baseIcon.urlDisabled : defaultIcon.urlDisabled;
        glyph = baseIcon.glyph != null ? baseIcon.glyph : defaultIcon.glyph;
        useUrl = baseIcon.useUrl;
    }

    public Icon(com.elmakers.mine.bukkit.api.item.Icon baseIcon, ConfigurationSection configuration, String itemIcon) {
        if (!(baseIcon instanceof Icon)) {
            throw new IllegalStateException("Icon is not the correct implementation type");
        }
        Icon other = (Icon)baseIcon;
        this.controller = other.controller;
        itemKey = configuration.getString("icon_item", itemIcon != null ? itemIcon : other.itemKey);
        itemDisabledKey = configuration.getString("icon_disabled", other.itemDisabledKey);
        legacyItemKey = configuration.getString("legacy_icon", other.legacyItemKey);
        legacyItemDisabledKey = configuration.getString("legacy_icon_disabled", other.legacyItemDisabledKey);
        url = configuration.getString("icon_url", other.url);
        urlDisabled = configuration.getString("icon_disabled_url", other.urlDisabled);
        glyph = configuration.getString("glyph", other.glyph);
        boolean onlyHasUrl = configuration.contains("icon_url") && itemIcon == null;
        useUrl = configuration.getBoolean("force_url", onlyHasUrl);
    }

    @Override
    @Nullable
    public String getUrl() {
        return url;
    }

    @Override
    @Nullable
    public String getUrlDisabled() {
        return urlDisabled;
    }

    @Override
    @Nullable
    public String getGlyph() {
        return glyph;
    }

    @Override
    public boolean forceUrl() {
        return useUrl;
    }

    private MaterialAndData getItem(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        ItemData itemData = controller.getOrCreateItem(key);
        return itemData == null ? null : itemData.getMaterialAndData();
    }

    @Override
    @Nullable
    public MaterialAndData getItemMaterial(boolean isLegacy) {
        String useKey = isLegacy && legacyItemKey != null && !legacyItemKey.isEmpty()
            ? legacyItemKey : itemKey;
        return getItem(useKey);
    }

    @Override
    @Nullable
    public MaterialAndData getItemDisabledMaterial(boolean isLegacy) {
        String useKey = isLegacy && legacyItemDisabledKey != null && !legacyItemDisabledKey.isEmpty()
            ? legacyItemDisabledKey : itemDisabledKey;
        return getItem(useKey);
    }
}