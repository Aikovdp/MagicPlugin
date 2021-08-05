package com.elmakers.mine.bukkit.magic;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.api.magic.MageController;

public abstract class TemplateProperties extends BaseMagicProperties {
    private final String key;

    protected TemplateProperties(@Nonnull MageController controller, @Nonnull String key) {
        super(controller);
        checkNotNull(key, "key");
        this.key = key;
    }

    protected TemplateProperties(@Nonnull MageController controller, String key, ConfigurationSection configuration) {
        super(controller, configuration);
        checkNotNull(key, "key");
        this.key = key;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nullable
    public abstract String getName();

    @Nullable
    public abstract String getDescription();

    @Nullable
    public TemplateProperties getParent() {
        return null;
    }

    @Nonnull
    public ConfigurationSection getPropertyConfiguration(String key) {
        TemplateProperties parent = getParent();
        if (parent == null || configuration.contains(key)) {
            return configuration;
        }
        return parent.getPropertyConfiguration(key);
    }

    public @Nullable String getIconKey() {
        return getIcon(controller.isLegacyIconsEnabled());
    }

    public @Nullable String getIconDisabledKey() {
        return getIcon(controller.isLegacyIconsEnabled(), "icon_disabled");
    }
}
