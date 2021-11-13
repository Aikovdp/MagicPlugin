package com.elmakers.mine.bukkit.action.builtin;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.block.MaterialBrush;
import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.spell.Spell;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import com.elmakers.mine.bukkit.block.DefaultMaterials;
import com.elmakers.mine.bukkit.block.MaterialAndData;
import com.elmakers.mine.bukkit.spell.BaseSpell;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;

public class BreakBlockAction extends ModifyBlockAction {
    private double durabilityAmount;
    private double durabilityPercent;
    private double maxDistanceSquared;
    private Material breakMaterial = Material.AIR;

    @Override
    public void prepare(CastContext context, ConfigurationSection parameters) {
        super.prepare(context, parameters);
        durabilityPercent = parameters.getDouble("break_percent", 0);
        durabilityAmount = parameters.getDouble("break_durability", durabilityPercent > 0 ? 0 : 1);
        double maxDistance = parameters.getDouble("durability_max_distance");
        maxDistanceSquared = maxDistance * maxDistance;

        String breakMaterialKey = parameters.getString("break_material");
        if (breakMaterialKey != null && !breakMaterialKey.isEmpty()) {
            try {
                breakMaterial = Material.valueOf(breakMaterialKey.toUpperCase());
            } catch (Exception ex) {
                context.getLogger().warning("Invalid material for break_material: " + breakMaterialKey);
                breakMaterial = Material.AIR;
            }
        }
    }

    @Override
    public SpellResult perform(CastContext context) {
        Block block = context.getTargetBlock();
        if (DefaultMaterials.isAir(block.getType()) || !context.isDestructible(block)) {
            return SpellResult.NO_TARGET;
        }
        context.registerForUndo(block);
        MageController controller = context.getController();
        double durability = controller.getBlockDurability(block);
        double scaledAmount = durabilityAmount + durabilityPercent * durability;
        if (maxDistanceSquared > 0) {
            double distanceSquared = context.getTargetCenterLocation().distanceSquared(block.getLocation());
            if (distanceSquared > maxDistanceSquared) {
                return SpellResult.NO_TARGET;
            }
            if (distanceSquared > 0) {
                scaledAmount = scaledAmount * (1 - distanceSquared / maxDistanceSquared);
            }
        }

        double breakAmount = 1;
        if (durability > 0) {
            double breakPercentage = scaledAmount / durability;
            breakAmount = context.registerBreaking(block, breakPercentage);
        }

        if (breakAmount >= 1 && context.hasBreakPermission(block)) {
            context.playEffects("break");
            CompatibilityLib.getCompatibilityUtils().clearBreaking(block);
            BlockState blockState = block.getState();
            if (blockState != null && MaterialAndData.shouldClearItemsIn(blockState)) {
                CompatibilityLib.getCompatibilityUtils().clearItems(blockState.getLocation());
            }
            MaterialBrush brush = context.getBrush();
            if (brush == null) {
                brush = new com.elmakers.mine.bukkit.block.MaterialBrush(context.getMage(), breakMaterial, (byte)0);
                context.setBrush(brush);
            } else {
                brush.setMaterial(breakMaterial);
            }
            super.perform(context);
            context.unregisterBreaking(block);
            context.playEffects("broken");
        } else {
            CompatibilityLib.getCompatibilityUtils().setBreaking(block, breakAmount);
        }
        return SpellResult.CAST;
    }

    @Override
    public void getParameterNames(Spell spell, Collection<String> parameters) {
        super.getParameterNames(spell, parameters);
        parameters.add("durability");
        parameters.add("durability_max_distance");
    }

    @Override
    public void getParameterOptions(Spell spell, String parameterKey, Collection<String> examples) {
        if (parameterKey.equals("durability") || parameterKey.equals("durability_max_distance")) {
            examples.addAll(Arrays.asList(BaseSpell.EXAMPLE_SIZES));
        } else {
            super.getParameterOptions(spell, parameterKey, examples);
        }
    }

    @Override
    public boolean usesBrush() {
        return false;
    }

    @Override
    public boolean requiresBuildPermission() {
        return false;
    }
}
