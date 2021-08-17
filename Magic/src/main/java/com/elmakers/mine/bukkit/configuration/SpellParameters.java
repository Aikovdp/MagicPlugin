package com.elmakers.mine.bukkit.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.magic.VariableScope;
import com.elmakers.mine.bukkit.api.spell.MageSpell;

public class SpellParameters extends MageParameters {
    private @Nullable ConfigurationSection castVariables;
    private @Nonnull ConfigurationSection spellVariables;
    private @Nullable ConfigurationSection mageVariables;
    private final @Nonnull Set<String> allParameters = new HashSet<>();
    private final @Nonnull MageSpell spell;
    private @Nullable CastContext context;

    public SpellParameters(@Nonnull MageSpell spell, @Nullable ConfigurationSection mageVariables, @Nullable ConfigurationSection variables) {
        super(spell.getMage(), "Spell: " + spell.getKey());
        this.spell = spell;
        this.spellVariables = spell.getVariables();
        this.mageVariables = mageVariables;
        Set<String> superParameters = super.getParameters();
        if (superParameters != null) {
            this.allParameters.addAll(superParameters);
        }
        // Only do this one, parameters don't change
        ConfigurationSection spellParameters = spell.getWorkingParameters();
        if (spellParameters != null) {
            this.allParameters.addAll(spellParameters.getKeys(false));
        }
        this.castVariables = variables;
    }

    public SpellParameters(@Nonnull MageSpell spell, @Nullable ConfigurationSection variables) {
        this(spell, spell.getMage() != null ? spell.getMage().getVariables() : null, variables);
    }

    public SpellParameters(@Nonnull MageSpell spell, @Nonnull CastContext context) {
        this(spell, context.getVariables());
        this.context = context;
    }

    public SpellParameters(@Nonnull MageSpell spell, @Nonnull CastContext context, ConfigurationSection config) {
        this(spell, context);
        wrap(config);
    }

    public SpellParameters(SpellParameters copy) {
        super(copy);
        this.spell = copy.spell;
        this.castVariables = copy.castVariables;
        this.spellVariables = copy.spellVariables;
        this.mageVariables = copy.mageVariables;
        this.allParameters.addAll(copy.allParameters);
        this.context = copy.context;
    }

    @Override
    public double getParameter(String parameter) {
        if (castVariables != null && castVariables.contains(parameter)) {
            return castVariables.getDouble(parameter);
        }
        if (spellVariables.contains(parameter)) {
            return spellVariables.getDouble(parameter);
        }
        if (mageVariables != null && mageVariables.contains(parameter)) {
            return mageVariables.getDouble(parameter);
        }
        Double value = context != null ? context.getAttribute(parameter) : spell.getAttribute(parameter);
        if (value == null) {
            ConfigurationSection spellParameters = spell.getWorkingParameters();
            if (spellParameters != null && spellParameters.contains(parameter)) {
                return spellParameters.getDouble(parameter);
            }
        }
        return value == null || Double.isNaN(value) || Double.isInfinite(value) ? 0 : value;
    }

    @Override
    public Set<String> getParameters() {
        if (castVariables != null) {
            this.allParameters.addAll(castVariables.getKeys(false));
        }
        this.allParameters.addAll(spellVariables.getKeys(false));
        if (mageVariables != null) {
            this.allParameters.addAll(mageVariables.getKeys(false));
        }
        return allParameters;
    }

    public void setMageVariables(@Nonnull ConfigurationSection variables) {
        this.mageVariables = checkNotNull(variables, "variables");
    }

    public void setSpellVariables(@Nonnull ConfigurationSection variables) {
        this.spellVariables = checkNotNull(variables, "variables");
    }

    @Nullable
    public ConfigurationSection getVariables(VariableScope scope) {
        switch (scope) {
            case CAST:
                return castVariables;
            case SPELL:
                return spellVariables;
            case MAGE:
                return mageVariables;
        }
        return null;
    }

    public void setContext(CastContext context) {
        this.context = context;
    }

    @Nullable
    public CastContext getContext() {
        return this.context;
    }
}
