package com.elmakers.mine.bukkit.block;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import com.elmakers.mine.bukkit.api.block.BlockData;

public class BlockList implements com.elmakers.mine.bukkit.api.block.BlockList {
    protected final Map<String,BoundingBox> areas = new HashMap<>();
    protected final Deque<BlockData>    blockList = new ArrayDeque<>();
    protected final Map<Long, BlockData> blockIdMap = new HashMap<>();

    public BlockList()
    {

    }

    public BlockList(BlockList other)
    {
        for (BlockData block : other)
        {
            BlockData newBlock = new com.elmakers.mine.bukkit.block.BlockData(block);
            add(newBlock);
        }
    }

    @Override
    public boolean add(Block block)
    {
        if (contains(block))
        {
            return false;
        }
        BlockData newBlock = new com.elmakers.mine.bukkit.block.BlockData(block);
        return add(newBlock);
    }

    @Override
    public boolean add(BlockData blockData)
    {
        if (!contain(blockData))
        {
            return false;
        }

        synchronized (blockList) {
            blockIdMap.put(blockData.getId(), blockData);
            blockList.addLast(blockData);
        }
        return true;
    }

    @Nonnull
    protected BlockData get(Block block) {
        long id = com.elmakers.mine.bukkit.block.BlockData.getBlockId(block);
        add(block);
        return blockIdMap.get(id);
    }

    @Override
    public boolean contain(BlockData block) {
        // First do a sanity check with the map
        // Currently, we don't replace blocks!
        if (contains(block)) return false;
        BoundingBox area = areas.get(block.getWorldName());
        if (area == null) {
            area = new BoundingBox(block.getPosition(), block.getPosition());
            areas.put(block.getWorldName(), area);
        } else {
            area.contain(block.getPosition());
        }
        return true;
    }

    @Override
    @Deprecated
    public void contain(Vector vector)
    {
        BoundingBox area = areas.values().iterator().next();
        if (area != null) {
            area.contain(vector);
        }
    }

    @Override
    public boolean addAll(Collection<? extends BlockData> blocks)
    {
        // Iterate to maintain BB area
        boolean added = true;
        for (BlockData block : blocks)
        {
            added = added && add(block);
        }
        return added;
    }

    @Override
    public void clear()
    {
        synchronized (blockList) {
            blockIdMap.clear();
            blockList.clear();
        }
    }

    @Override
    public boolean contains(Block block)
    {
        boolean contains;
        synchronized (blockList) {
            contains = blockIdMap.containsKey(com.elmakers.mine.bukkit.block.BlockData.getBlockId(block));
        }
        return contains;
    }

    public boolean contains(BlockData blockData)
    {
        boolean contains;
        synchronized (blockList) {
            contains = blockIdMap.containsKey(blockData.getId());
        }
        return contains;
    }

    @Override
    public boolean contains(Object arg0)
    {
        if (arg0 instanceof Block) {
            return contains((Block)arg0);
        }
        if (arg0 instanceof BlockData) {
            return contains((BlockData)arg0);
        }
        // Fall back to map
        boolean contains;
        synchronized (blockList) {
            contains = blockIdMap.containsKey(arg0);
        }
        return contains;
    }

    @Override
    public boolean containsAll(Collection<?> arg0)
    {
        boolean contains;
        synchronized (blockList) {
            contains = blockIdMap.keySet().containsAll(arg0);
        }
        return contains;
    }

    // Collection interface- would be great if I could just extend HashSet and
    // have this "just work"

    // For now, this is here to keep the map up to date, and to pass through to
    // the blockList.

    @Override
    public BoundingBox getArea()
    {
        return areas.values().iterator().next();
    }

    @Override
    public Map<String, BoundingBox> getAreas() {
        return areas;
    }

    public Collection<BlockData> getBlockList()
    {
        return blockList;
    }

    @Override
    public int size()
    {
        return blockList.size();
    }

    @Override
    public boolean isEmpty()
    {
        return blockList.isEmpty();
    }

    @Override
    public Iterator<BlockData> iterator()
    {
        return blockList.iterator();
    }

    @Override
    public boolean remove(Object removeObject)
    {
        // Note that we never shrink the BB!
        boolean removed;
        synchronized (blockList) {
            if (removeObject instanceof BlockData)
            {
                blockIdMap.remove(((BlockData)removeObject).getId());
            }
            removed = blockList.remove(removeObject);
        }
        return removed;
    }

    @Override
    public boolean removeAll(Collection<?> removeCollection)
    {
        boolean removed;
        synchronized (blockList) {
            for (Object removeObject : removeCollection)
            {
                if (removeObject instanceof BlockData)
                {
                    blockIdMap.remove(((BlockData)removeObject).getId());
                }
            }
            removed = blockList.removeAll(removeCollection);;
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> arg0)
    {
        return blockList.retainAll(arg0);
    }

    @Override
    @Nullable
    public Object[] toArray() {
        return blockList.toArray();
    }

    @Override
    @Nullable
    public <T> T[] toArray(T[] arg0) {
        return blockList.toArray(arg0);
    }

    @Override
    public void load(ConfigurationSection node) {
        List<String> blockData = node.getStringList("blocks");
        if (blockData != null) {
            for (String blockString : blockData) {
                BlockData deserialized = com.elmakers.mine.bukkit.block.BlockData.fromString(blockString);
                if (deserialized == null) continue;
                add(deserialized);
            }
        }
    }

    @Override
    public void save(ConfigurationSection node) {
        synchronized (blockList) {
            if (!blockList.isEmpty()) {
                List<String> blockData = new ArrayList<>();
                for (BlockData block : blockList) {
                    if (!block.isFake()) {
                        blockData.add(block.toString());
                    }
                }
                node.set("blocks", blockData);
            }
        }
    }

    @Override
    @Nullable
    @Deprecated
    public String getWorldName() {
        return areas.keySet().iterator().next();
    }

    @Override
    @Nonnull
    public Set<Chunk> getChunks() {
        Set<Chunk> chunks = new HashSet<>();
        for (BlockData block : blockList) {
            chunks.add(block.getChunk());
        }
        return chunks;
    }

}
