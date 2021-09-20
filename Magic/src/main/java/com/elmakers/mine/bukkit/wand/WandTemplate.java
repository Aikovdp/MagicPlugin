package com.elmakers.mine.bukkit.wand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.elmakers.mine.bukkit.api.effect.EffectContext;
import com.elmakers.mine.bukkit.api.effect.EffectPlayer;
import com.elmakers.mine.bukkit.api.item.ItemData;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.wand.Wand;
import com.elmakers.mine.bukkit.block.MaterialAndData;
import com.elmakers.mine.bukkit.configuration.MageParameters;
import com.elmakers.mine.bukkit.magic.TemplateProperties;
import com.elmakers.mine.bukkit.utility.ConfigurationUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class WandTemplate extends TemplateProperties implements com.elmakers.mine.bukkit.api.wand.WandTemplate {
    private Map<String, Collection<EffectPlayer>> effects = new HashMap<>();
    private Set<String> tags;
    private @Nonnull Set<String> categories = ImmutableSet.of();
    private String creator;
    private String creatorId;
    private String migrateTemplate;
    private String migrateIcon;
    private String icon;
    private boolean restorable;
    private Map<String, String> migrateIcons;
    private ConfigurationSection attributes;
    private String attributeSlot;
    private String parentKey;
    private String description;
    private String name;
    private Map<String, String> messageKeys = new HashMap<>();

    public WandTemplate(MageController controller, String key, ConfigurationSection node) {
        super(controller, key, node);
        effects.clear();
        creator = node.getString("creator");
        creatorId = node.getString("creator_id");
        migrateTemplate = node.getString("migrate_to");
        migrateIcon = node.getString("migrate_icon");
        restorable = node.getBoolean("restorable", true);
        icon = node.getString("icon");
        attributeSlot = node.getString("item_attribute_slot", node.getString("attribute_slot"));
        parentKey = node.getString("inherit");
        name = node.getString("name", key);
        description = node.getString("description", "");

        // Remove some properties that should not transfer to wands
        clearProperty("creator");
        clearProperty("creator_id");
        clearProperty("migrate_to");
        clearProperty("migrate_icon");
        clearProperty("restorable");
        clearProperty("hidden");
        clearProperty("enabled");
        clearProperty("inherit");

        ConfigurationSection migrateConfig = node.getConfigurationSection("migrate_icons");
        // This ! may look odd, but we only want to use legacy icon migration if we're *not* using legacy icons,
        // since the intention is to migrate the legacy icons to the new icons.
        if (!controller.isLegacyIconsEnabled()) {
            ConfigurationSection migrateLegacyConfig = node.getConfigurationSection("migrate_legacy_icons");
            if (migrateLegacyConfig != null) {
                migrateConfig = migrateLegacyConfig;
            }
            String legacyIcon = node.getString("legacy_icon");
            if (legacyIcon != null && !legacyIcon.isEmpty() && icon != null) {
                migrateIcons = new HashMap<>();
                migrateIcons.put(legacyIcon, icon);
                // This is unfortunately needed to handle aliases like wand_icon being converted to their
                // base item
                ItemData converted = controller.getItem(legacyIcon);
                if (converted != null) {
                    MaterialAndData convertedItem = new MaterialAndData(converted.getItemStack());
                    String convertedIcon = convertedItem.getKey();
                    if (!convertedIcon.equals(legacyIcon)) {
                        migrateIcons.put(convertedIcon, icon);
                    }
                }
            }
        } else {
            icon = node.getString("legacy_icon", icon);
        }
        if (migrateConfig != null) {
            if (migrateIcons == null) {
                migrateIcons = new HashMap<>();
            }
            Set<String> keys = migrateConfig.getKeys(false);
            for (String migrateKey : keys) {
                migrateIcons.put(migrateKey, migrateConfig.getString(migrateKey));
            }
            clearProperty("migrate_icons");
        }

        if (node.contains("effects")) {
            ConfigurationSection effectsNode = node.getConfigurationSection("effects");
            Collection<String> effectKeys = effectsNode.getKeys(false);
            for (String effectKey : effectKeys) {
                if (effectsNode.isString(effectKey)) {
                    String referenceKey = effectsNode.getString(effectKey);
                    if (effects.containsKey(referenceKey)) {
                        effects.put(effectKey, new ArrayList<>(effects.get(referenceKey)));
                    } else {
                        Collection<EffectPlayer> baseEffects = controller.getEffects(referenceKey);
                        effects.put(effectKey, baseEffects);
                    }
                } else {
                    effects.put(effectKey, controller.loadEffects(effectsNode, effectKey));
                }
            }
        }

        Collection<String> tagList = ConfigurationUtils.getStringList(node, "tags");
        if (tagList != null) {
            tags = new HashSet<>(tagList);
            clearProperty("tags");
        } else {
            tags = null;
        }

        Collection<String> categoriesList = ConfigurationUtils.getStringList(node, "categories");
        if (categoriesList != null) {
            clearProperty("categories");
            categories = ImmutableSet.copyOf(categoriesList);
        }
    }

    protected WandTemplate(WandTemplate copy, ConfigurationSection configuration) {
        super(copy.controller, copy.getKey(), configuration);
        load(configuration);

        this.effects = copy.effects;
        this.tags = copy.tags;
        this.categories = copy.categories;
        this.creator = copy.creator;
        this.creatorId = copy.creatorId;
        this.migrateTemplate = copy.migrateTemplate;
        this.migrateIcon = copy.migrateIcon;
        this.icon = copy.icon;
        this.restorable = copy.restorable;
        this.migrateIcons = copy.migrateIcons;
        this.attributes = copy.attributes;
        this.attributeSlot = copy.attributeSlot;
        this.parentKey = copy.parentKey;
    }


    public WandTemplate getMageTemplate(Mage mage) {
        MageParameters parameters = new MageParameters(mage, "Wand " + getKey());
        ConfigurationUtils.addConfigurations(parameters, configuration);
        return new WandTemplate(this, parameters);
    }

    @Override
    public String getName() {
        return controller.getMessages().get("wands." + getKey() + ".name", name);
    }

    @Override
    public String getDescription() {
        return controller.getMessages().get("wands." + getKey() + ".description", description);
    }

    @Override
    public Collection<com.elmakers.mine.bukkit.api.effect.EffectPlayer> getEffects(String key) {
        Collection<EffectPlayer> effectList = effects.get(key);
        if (effectList == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(effectList);
    }

    @Override
    public boolean playEffects(Wand wand, String key)
    {
        return playEffects(wand.getMage(), wand, key, 1.0f);
    }

    @Override
    public boolean playEffects(Wand wand, String effectName, float scale) {
        return playEffects(wand.getMage(), wand, getKey(), scale);
    }

    @Override
    @Deprecated
    public boolean playEffects(Mage mage, String key)
    {
        return playEffects(mage, mage.getActiveWand(), key, 1.0f);
    }

    @Override
    @Deprecated
    public boolean playEffects(Mage mage, String effectName, float scale) {
        return playEffects(mage, mage.getActiveWand(), effectName, scale);
    }

    private boolean playEffects(Mage mage, Wand wand, String effectName, float scale)
    {
        Preconditions.checkNotNull(mage, "mage");
        // First check the wand for overridden effects
        Collection<com.elmakers.mine.bukkit.api.effect.EffectPlayer> effects = null;
        String effectKey = wand.getString("effects." + effectName);
        if (effectKey != null && !effectKey.isEmpty()) {
            effects = controller.getEffects(effectKey);
        }
        if (effects == null || effects.isEmpty()) {
            effects = getEffects(effectName);
        }
        if (effects.isEmpty()) return false;

        Entity sourceEntity = mage.getEntity();
        for (com.elmakers.mine.bukkit.api.effect.EffectPlayer player : effects)
        {
            EffectContext context = wand.getContext();
            // Track effect plays for cancelling
            context.trackEffects(player);

            // Set scale
            player.setScale(scale);

            // Set material and color
            player.setColor(wand.getEffectColor());
            String overrideParticle = wand.getEffectParticleName();
            player.setParticleOverride(overrideParticle);

            Location source = player.getSourceLocation(context);
            if (source == null) {
                source = mage.getLocation();
            }

            player.start(source, sourceEntity, null, null, null);
        }

        return true;
    }

    @Override
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public Set<String> getCategories() {
        return categories;
    }

    @Nullable
    @Override
    public WandTemplate getMigrateTemplate() {
        return migrateTemplate == null ? null : controller.getWandTemplate(migrateTemplate);
    }

    @Override
    public String migrateIcon(String currentIcon) {
        if (icon != null && migrateIcon != null && migrateIcon.equals(currentIcon)) {
            return icon;
        }
        if (migrateIcons != null) {
            String newIcon = migrateIcons.get(currentIcon);
            if (newIcon != null) {
                return newIcon;
            }
        }
        return currentIcon;
    }

    @Override
    @Deprecated
    public boolean isSoul() {
        return false;
    }

    @Override
    public boolean isRestorable() {
        return restorable;
    }

    @Override
    public ConfigurationSection getAttributes() {
        return attributes;
    }

    @Override
    public String getAttributeSlot() {
        return attributeSlot;
    }

    @Nullable
    @Override
    public WandTemplate getParent() {
        if (parentKey != null && !parentKey.isEmpty() && !parentKey.equalsIgnoreCase("false")) {
            return controller.getWandTemplate(parentKey);
        }
        return null;
    }

    @Override
    public String getMessageKey(String key) {
        if (!messageKeys.containsKey(key)) {
            String wandKey = "wands." + this.getKey() + "." + key;
            if (controller.getMessages().containsKey(wandKey)) {
                messageKeys.put(key, wandKey);
            } else {
                WandTemplate parent = parentKey == null || parentKey.isEmpty() ? null : controller.getWandTemplate(parentKey);
                String parentMessageKey = parent == null ? null : parent.getMessageKey(key);
                messageKeys.put(key, parentMessageKey);
            }
        }
        return messageKeys.get(key);
    }
}
