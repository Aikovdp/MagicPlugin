package com.elmakers.mine.bukkit.utility.platform.base.entity;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;

import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.entity.EntityExtraData;
import com.elmakers.mine.bukkit.utility.ConfigUtils;

public class EntityAnimalData extends EntityExtraData {
    private UUID owner;
    private Boolean tamed;
    protected Boolean sitting;

    public EntityAnimalData() {

    }

    public EntityAnimalData(ConfigurationSection parameters, MageController controller) {
        Logger log = controller.getLogger();
        String tamer = parameters.getString("owner");
        if (tamer != null && !tamer.isEmpty()) {
            try {
                this.owner = UUID.fromString(tamer);
            } catch (Exception ex) {
                log.log(Level.WARNING, "Invalid owner UUID: " + tamer, ex);
            }
        }
        if (this.owner != null) {
            this.tamed = true;
        } else {
            tamed = ConfigUtils.getOptionalBoolean(parameters, "tamed");
        }
        sitting = ConfigUtils.getOptionalBoolean(parameters, "sitting");
    }

    public EntityAnimalData(Entity entity) {
        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable)entity;
            AnimalTamer tamer = tameable.getOwner();
            if (tamer != null) {
                this.owner = tamer.getUniqueId();
            }
            this.tamed = tameable.isTamed();
        }
        this.sitting = getPlatform().getCompatibilityUtils().isSitting(entity);
    }

    @Override
    public void apply(Entity entity) {
        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable)entity;
            if (owner != null) {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(this.owner);
                if (owner != null) {
                    tameable.setOwner(owner);
                }
            }
            if (tamed != null) {
                tameable.setTamed(tamed);
            }
        }
        if (sitting != null) {
            getPlatform().getCompatibilityUtils().setSitting(entity, sitting);
        }
    }
}
