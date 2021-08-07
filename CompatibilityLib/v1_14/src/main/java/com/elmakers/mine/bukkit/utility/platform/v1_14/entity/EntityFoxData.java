package com.elmakers.mine.bukkit.utility.platform.v1_14.entity;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;

import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.utility.ConfigUtils;
import com.elmakers.mine.bukkit.utility.platform.base.entity.EntityAnimalData;

public class EntityFoxData extends EntityAnimalData {
    private Fox.Type type;
    private Boolean crouching;

    public EntityFoxData() {

    }

    public EntityFoxData(ConfigurationSection parameters, MageController controller) {
        super(parameters, controller);
        Logger log = controller.getLogger();
        String typeString = parameters.getString("fox_type");
        if (typeString != null && !typeString.isEmpty()) {
            try {
                type = Fox.Type.valueOf(typeString.toUpperCase());
            } catch (Exception ex) {
                log.log(Level.WARNING, "Invalid fox_type: " + typeString, ex);
            }
        }
        crouching = ConfigUtils.getOptionalBoolean(parameters, "crouching");
    }

    public EntityFoxData(Entity entity) {
        super(entity);
        if (entity instanceof Fox) {
            Fox fox = (Fox)entity;
            type = fox.getFoxType();
            crouching = fox.isCrouching();
        }
    }

    @Override
    public void apply(Entity entity) {
        super.apply(entity);
        if (entity instanceof Fox) {
            Fox fox = (Fox)entity;
            if (type != null) {
                fox.setFoxType(type);
            }
            if (crouching != null) fox.setCrouching(crouching);
        }
    }

    @Override
    public boolean cycle(Entity entity) {
        if (!canCycle(entity)) {
            return false;
        }
        Fox fox = (Fox)entity;
        Fox.Type type = fox.getFoxType();
        Fox.Type[] typeValues = Fox.Type.values();
        int typeOrdinal = (type.ordinal() + 1) % typeValues.length;
        type = typeValues[typeOrdinal];
        fox.setFoxType(type);
        return true;
    }

    @Override
    public boolean canCycle(Entity entity) {
        return entity instanceof Fox;
    }
}
