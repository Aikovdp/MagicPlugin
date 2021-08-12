package com.elmakers.mine.bukkit.wand;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.api.magic.MageContext;
import com.elmakers.mine.bukkit.api.requirements.Requirement;
import com.elmakers.mine.bukkit.utility.ConfigurationUtils;

public class RequirementProperties {
    private final Collection<Requirement> requirements;
    private final ConfigurationSection properties;
    private boolean status;

    public RequirementProperties(ConfigurationSection configuration) {
        requirements = ConfigurationUtils.getRequirements(configuration);
        properties = configuration.getConfigurationSection("properties");
    }

    public boolean check(MageContext context) {
        status = context.getController().checkRequirements(context, requirements) == null;
        return status;
    }

    public boolean hasChanged(MageContext context) {
        boolean current = status;
        return (check(context) != current);
    }

    public boolean isEmpty() {
        return requirements == null || requirements.isEmpty() || properties == null || properties.getKeys(false).isEmpty();
    }

    public ConfigurationSection getProperties() {
        return properties;
    }
}
