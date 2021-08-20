package com.elmakers.mine.bukkit.requirements;

import org.bukkit.configuration.ConfigurationSection;

class RangedRequirement {
    public Double max;
    public Double min;
    public Double value;

    public RangedRequirement(String value) {
        if (value.startsWith("<")) {
            try {
                max = Double.parseDouble(value.substring(1));
            } catch (Exception ignore) {
            }
        } else if (value.startsWith(">")) {
            try {
                min = Double.parseDouble(value.substring(1));
            } catch (Exception ignore) {
            }
        }
        if (value.startsWith("=")) {
            value = value.substring(1);
        }
        try {
            this.value = Double.parseDouble(value);
        } catch (Exception ignore) {
        }
    }

    public RangedRequirement(ConfigurationSection configuration) {
        if (configuration.contains("min")) {
            min = configuration.getDouble("min");
        }
        if (configuration.contains("max")) {
            max = configuration.getDouble("max");
        }
        if (configuration.contains("value")) {
            value = configuration.getDouble("value");
        }
    }

    public boolean check(Double value) {
        if (this.value != null && (value == null || !value.equals(this.value))) return false;
        if (this.min != null && (value == null || value <= this.min)) return false;
        if (this.max != null && (value != null && value >= this.max)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "[Require =" + value + " from (" + min + " to " + max + ")]";
    }
}
