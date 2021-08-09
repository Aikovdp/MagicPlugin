package com.elmakers.mine.bukkit.api.protection;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;

import com.elmakers.mine.bukkit.api.block.MaterialAndData;

public class PlayerWarp {
    private final @Nonnull String name;
    private final @Nonnull Location location;
    private final @Nullable String description;
    private final @Nullable MaterialAndData icon;

    public PlayerWarp(@Nonnull String name, @Nonnull Location location, @Nullable String description, @Nullable MaterialAndData icon) {
        checkNotNull(name, "Name cannot be null");
        checkNotNull(location, "Location cannot be null");

        this.name = name;
        this.description = description;
        this.icon = icon;
        this.location = location;
    }

    public PlayerWarp(@Nonnull String name, @Nonnull Location location) {
        checkNotNull(name, "Name cannot be null");
        checkNotNull(location, "Location cannot be null");

        this.name = name;
        this.location = location;
        this.description = null;
        this.icon = null;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public MaterialAndData getIcon() {
        return icon;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }
}
