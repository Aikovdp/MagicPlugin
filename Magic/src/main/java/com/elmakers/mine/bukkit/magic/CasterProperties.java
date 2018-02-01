package com.elmakers.mine.bukkit.magic;

import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.spell.SpellKey;
import com.elmakers.mine.bukkit.api.spell.SpellTemplate;
import com.elmakers.mine.bukkit.wand.Wand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CasterProperties extends BaseMagicConfigurable {
    protected int effectiveManaMax = 0;
    protected int effectiveManaRegeneration = 0;

    public CasterProperties(MagicPropertyType type, MageController controller) {
        super(type, controller);
    }

    public boolean hasOwnMana() {
        return hasOwnProperty("mana_max");
    }

    public int getManaRegeneration() {
        return getInt("mana_regeneration", getInt("xp_regeneration"));
    }

    public int getManaMax() {
        return getInt("mana_max", getInt("xp_max"));
    }

    public void setMana(float mana) {
        if (isCostFree()) {
            setProperty("mana", null);
        } else {
            setProperty("mana", Math.max(0, mana));
        }
    }

    public void setManaMax(int manaMax) {
        setProperty("mana_max", Math.max(0, manaMax));
    }

    public void setManaRegeneration(int manaRegeneration) {
        setProperty("mana_regeneration", Math.max(0, manaRegeneration));
    }

    public float getMana() {
        return getFloat("mana", getFloat("xp"));
    }

    public void removeMana(float amount) {
        setMana(getMana() - amount);
    }

    public float getManaRegenerationBoost() {
        return getFloat("mana_regeneration_boost", getFloat("xp_regeneration_boost"));
    }

    public float getManaMaxBoost() {
        return getFloat("mana_max_boost", getFloat("xp_max_boost"));
    }

    public float getCostReduction() {
        if (isCostFree()) return 1.0f;
        return controller.getCostReduction() + getFloat("cost_reduction") * controller.getMaxCostReduction();
    }

    public boolean isCostFree() {
        return getFloat("cost_reduction") > 1;
    }

    public int getEffectiveManaMax() {
        return effectiveManaMax;
    }

    public int getEffectiveManaRegeneration() {
        return effectiveManaRegeneration;
    }

    protected long getLastManaRegeneration() {
        return getLong("mana_timestamp");
    }

    @Override
    public void loadProperties() {
        super.loadProperties();
        updateMaxMana(null);
    }

    protected boolean updateMaxMana(Mage mage) {
        if (!usesMana()) {
            return false;
        }
        int currentMana = effectiveManaMax;
        int currentManaRegen = effectiveManaRegeneration;

        float effectiveBoost = getManaMaxBoost();
        float effectiveRegenBoost = getManaRegenerationBoost();
        if (mage != null)
        {
            Collection<Wand> activeArmor = mage.getActiveArmor();
            for (Wand armorWand : activeArmor) {
                effectiveBoost += armorWand.getManaMaxBoost();
                effectiveRegenBoost += armorWand.getManaRegenerationBoost();
            }
            Wand activeWand = mage.getActiveWand();
            if (activeWand != null && !activeWand.isPassive()) {
                effectiveBoost += activeWand.getManaMaxBoost();
                effectiveRegenBoost += activeWand.getManaRegenerationBoost();
            }
            Wand offhandWand = mage.getOffhandWand();
            if (offhandWand != null && !offhandWand.isPassive()) {
                effectiveBoost += offhandWand.getManaMaxBoost();
                effectiveRegenBoost += offhandWand.getManaRegenerationBoost();
            }
        }
        effectiveManaMax = getManaMax();
        if (effectiveBoost != 0) {
            effectiveManaMax = (int)Math.ceil(effectiveManaMax + effectiveBoost * effectiveManaMax);
        }
        effectiveManaRegeneration = getManaRegeneration();
        if (effectiveRegenBoost != 0) {
            effectiveManaRegeneration = (int)Math.ceil(effectiveManaRegeneration + effectiveRegenBoost * effectiveManaRegeneration);
        }

        return (currentMana != effectiveManaMax || effectiveManaRegeneration != currentManaRegen);
    }

    public boolean usesMana() {
        if (isCostFree()) return false;
        return getManaMax() > 0;
    }

    public boolean tickMana() {
        boolean updated = false;
        if (usesMana() && hasOwnMana()) {
            long now = System.currentTimeMillis();
            long lastManaRegeneration = getLastManaRegeneration();
            int manaRegeneration = getManaRegeneration();
            int effectiveManaRegeneration = getEffectiveManaRegeneration();
            if (manaRegeneration > 0 && lastManaRegeneration > 0 && effectiveManaRegeneration > 0)
            {
                long delta = now - lastManaRegeneration;
                int effectiveManaMax = getEffectiveManaMax();
                int manaMax = getManaMax();
                float mana = getMana();
                if (effectiveManaMax == 0 && manaMax > 0) {
                    effectiveManaMax = manaMax;
                }
                setMana(Math.min(effectiveManaMax, mana + (float) effectiveManaRegeneration * (float)delta / 1000));
                updated = true;
            }
            lastManaRegeneration = now;
            setProperty("mana_timestamp", lastManaRegeneration);
        }

        return updated;
    }

    public void tick() {
        tickMana();
    }

    @Override
    public boolean addSpell(String spellKey) {
        Set<String> spells = getSpells();
        SpellKey key = new SpellKey(spellKey);
        boolean modified = spells.add(key.getBaseKey());
        if (modified) {
            setProperty("spells", new ArrayList<>(spells));
        }
        boolean levelModified = false;
        if (key.getLevel() > 1) {
            levelModified = upgradeSpellLevel(key.getBaseKey(), key.getLevel());
        }

        return modified || levelModified;
    }

    @Override
    public boolean addBrush(String brushKey) {
        Set<String> brushes = getBrushes();
        boolean modified = brushes.add(brushKey);
        if (modified) {
            setProperty("brushes", new ArrayList<>(brushes));
        }

        return modified;
    }

    public boolean removeSpell(String spellKey) {
        Set<String> spells = getSpells();
        SpellKey key = new SpellKey(spellKey);
        boolean modified = spells.remove(key.getBaseKey());
        if (modified) {
            setProperty("spells", new ArrayList<>(spells));
            Map<String, Object> spellLevels = getSpellLevels();
            if (spellLevels.remove(key.getBaseKey()) != null) {
                setProperty("spell_levels", spellLevels);
            }
        }

        return modified;
    }

    public boolean removeBrush(String brushKey) {
        Set<String> brushes = getBrushes();
        boolean modified = brushes.remove(brushKey);
        if (modified) {
            setProperty("brushes", new ArrayList<>(brushes));
        }

        return modified;
    }

    public int getSpellLevel(String spellKey) {
        Map<String, Object> spellLevels = getSpellLevels();
        Object level = spellLevels.get(spellKey);
        return level == null || !(level instanceof Integer) ? 1 : (Integer)level;
    }

    public SpellTemplate getBaseSpell(String spellKey) {
        SpellKey key = new SpellKey(spellKey);
        Set<String> spells = getSpells();
        if (!spells.contains(key.getBaseKey())) return null;
        SpellKey baseKey = new SpellKey(key.getBaseKey(), getSpellLevel(key.getBaseKey()));
        return controller.getSpellTemplate(baseKey.getKey());
    }

    public void updateMana() {

    }

    public Set<String> getSpells() {
        Object existingSpells = getObject("spells");
        Set<String> spells = new HashSet<>();
        if (existingSpells != null) {
            if (!(existingSpells instanceof List)) {
                controller.getLogger().warning("Spell list in " + type + " is " + existingSpells.getClass().getName() + ", expected List");
            } else {
                @SuppressWarnings("unchecked")
                List<String> existingList = (List<String>)existingSpells;
                spells.addAll(existingList);
            }
        }
        return spells;
    }

    public Set<String> getBrushes() {
         Object existingBrushes = getObject("brushes");
        Set<String> brushes = new HashSet<>();
        if (existingBrushes != null) {
            if (!(existingBrushes instanceof List)) {
                controller.getLogger().warning("Brush list in " + type + " is " + existingBrushes.getClass().getName() + ", expected List");
            } else {
                @SuppressWarnings("unchecked")
                List<String> existingList = (List<String>)existingBrushes;
                brushes.addAll(existingList);
            }
        }
        return brushes;
    }
}
