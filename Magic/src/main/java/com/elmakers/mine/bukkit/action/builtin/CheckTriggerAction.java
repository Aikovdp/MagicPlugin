package com.elmakers.mine.bukkit.action.builtin;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.action.CheckAction;
import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.spell.Spell;

public class CheckTriggerAction extends CheckAction {
    private String trigger;

    @Override
    public void prepare(CastContext context, ConfigurationSection parameters)
    {
        super.prepare(context, parameters);
        trigger = parameters.getString("trigger", "");
    }

    @Override
    protected boolean isAllowed(CastContext context) {
        Long lastTrigger = context.getMage().getLastTrigger(trigger);
        long lastTriggerTime = context.getLastTriggerTime();
        boolean isTriggered = (lastTrigger != null && lastTrigger > lastTriggerTime);
        if (isTriggered) {
            context.updateLastTriggerTime();
        }
        return isTriggered;
    }

    @Override
    public void getParameterNames(Spell spell, Collection<String> parameters) {
        super.getParameterNames(spell, parameters);
        parameters.add("trigger");
    }
}
