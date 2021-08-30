package com.elmakers.mine.bukkit.utility.platform.v1_11;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class Platform extends com.elmakers.mine.bukkit.utility.platform.v1_10.Platform {

    public Platform(Plugin plugin, Logger logger) {
        super(plugin, logger);
    }

    @Override
    protected com.elmakers.mine.bukkit.utility.platform.EntityUtils createEntityUtils() {
        return new EntityUtils(this);
    }
}
