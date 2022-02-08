package com.elmakers.mine.bukkit.action.builtin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.elmakers.mine.bukkit.action.BaseSpellAction;
import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.integration.ModelEngine;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;

public class MountAction extends BaseSpellAction {
    private boolean eject = false;
    private boolean dismount = false;

    @Override
    public void prepare(CastContext context, ConfigurationSection parameters) {
        eject = parameters.getBoolean("eject", false);
        dismount = parameters.getBoolean("dismount", false);
    }

    @Override
    public SpellResult perform(CastContext context) {
        LivingEntity source = context.getLivingEntity();
        if (source == null) {
            return SpellResult.LIVING_ENTITY_REQUIRED;
        }

        // Make it so this spell can be used to get someone off of you
        if (eject) {
            source.eject();
            return SpellResult.CAST;
        }

        Entity current = source.getVehicle();
        if (current != null) {
            current.eject();
        }
        if (dismount) {
            return current != null ? SpellResult.CAST : SpellResult.NO_TARGET;
        }
        Entity targetEntity = context.getTargetEntity();
        if (targetEntity == null) {
            return SpellResult.NO_TARGET;
        }

        ModelEngine modelEngine = context.getController().getModelEngine();
        while (targetEntity instanceof ComplexEntityPart) {
            targetEntity = ((ComplexEntityPart)targetEntity).getParent();
        }
        Entity passenger = CompatibilityLib.getDeprecatedUtils().getPassenger(targetEntity);
        while (passenger != null) {
            targetEntity = passenger;
            passenger = CompatibilityLib.getDeprecatedUtils().getPassenger(targetEntity);
        }

        if (targetEntity == CompatibilityLib.getDeprecatedUtils().getPassenger(source)
            || source == CompatibilityLib.getDeprecatedUtils().getPassenger(targetEntity)) {
            return SpellResult.NO_TARGET;
        }

        if (modelEngine != null && modelEngine.addPassenger(targetEntity, source)) {
            return SpellResult.CAST;
        }
        CompatibilityLib.getDeprecatedUtils().setPassenger(targetEntity, source);

        return SpellResult.CAST;
    }
}
