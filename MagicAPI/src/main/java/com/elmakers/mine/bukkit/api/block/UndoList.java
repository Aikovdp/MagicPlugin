package com.elmakers.mine.bukkit.api.block;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.batch.Batch;
import com.elmakers.mine.bukkit.api.entity.EntityData;
import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.api.magic.MageModifier;
import com.elmakers.mine.bukkit.api.spell.Spell;

public interface UndoList extends BlockList, Comparable<UndoList> {
    void commit();
    boolean commitNext();
    void undo();
    void undo(boolean blocking);
    void undoScheduled();
    void undoScheduled(boolean blocking);
    @Nullable
    BlockData undoNext(boolean applyPhysics);

    void setEntityUndo(boolean undoEntityEffects);
    void setEntityUndoTypes(Set<EntityType> undoTypes);

    void setScheduleUndo(int ttl);
    int getScheduledUndo();
    void updateScheduledUndo();
    boolean bypass();
    void setApplyPhysics(boolean physics);
    void setLockChunks(boolean lockChunks);
    void setModifyType(ModifyType modifyType);
    ModifyType getModifyType();
    long getCreatedTime();
    long getModifiedTime();
    long getScheduledTime();
    boolean isScheduled();
    void setUndoSpeed(double speed);
    void setSorted(boolean sorted);
    void setReversed(boolean reversed);
    boolean hasBeenScheduled();
    void setHasBeenScheduled();

    void setUndoBreakable(boolean breakable);
    void setUndoReflective(boolean reflective);

    void prune();

    void add(Entity entity);
    void add(Runnable runnable);
    void remove(Entity entity);
    @Nullable
    EntityData damage(Entity entity);
    @Nullable
    EntityData modify(Entity entity);
    void move(Entity entity);
    void modifyVelocity(Entity entity);
    void addPotionEffects(Entity entity);
    void addPotionEffectForRemoval(Entity entity, PotionEffectType potionEffectType);

    void addModifier(Entity entity, MageModifier modifiers);
    void addModifierForRemoval(Entity entity, String modifierKey);

    void convert(Entity entity, Block block);
    void fall(Entity entity, Block block);
    void explode(Entity entity, List<Block> explodedBlocks);
    void finalizeExplosion(Entity entity, List<Block> explodedBlocks);
    void cancelExplosion(Entity entity);
    void setBatch(Batch batch);
    void setSpell(Spell spell);
    void clearAttachables(Block block);

    boolean contains(Location location, int threshold);

    String getName();
    @Nullable
    Mage getOwner();
    @Nullable
    CastContext getContext();
    void setBypass(boolean bypass);
    boolean isBypass();
    Collection<Entity> getAllEntities();
    boolean getApplyPhysics();
    boolean isSynchronous();

    boolean isConsumed();
    void setConsumed(boolean consumed);
    boolean isUndone();
    boolean isUnbreakable();
    boolean isIndestructible(Block block);
    void setUnbreakable(boolean unbreakable);

    /**
     * Check to see if this list has any changes that would get normally auto-undone (e.g. by scheduled undo)
     *
     * <p>This generally only includes block changes, though if setEntityUndo(true) has been used it will include
     * entity changes as well.
     *
     * @return true if this list has changes to undo
     */
    boolean hasChanges();

    int getRunnableCount();
    @Nullable
    Runnable undoNextRunnable();
    EntityData getEntityData(Entity entity);
    boolean isUndoType(EntityType entityType);
    boolean affectsWorld(@Nonnull World world);
    void addDamage(Block block, double damage);
    @Deprecated
    void setUndoBreaking(boolean breaking);
    void registerFakeBlock(Block block, Collection<WeakReference<Player>> players);
}
