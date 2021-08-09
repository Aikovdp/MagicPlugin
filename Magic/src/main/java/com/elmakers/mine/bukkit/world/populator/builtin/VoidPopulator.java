package com.elmakers.mine.bukkit.world.populator.builtin;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.world.BlockResult;
import com.elmakers.mine.bukkit.world.populator.BaseBlockPopulator;

public class VoidPopulator extends BaseBlockPopulator {
    @Override
    public boolean onLoad(ConfigurationSection configuration) {
        return true;
    }

    @Override
    public BlockResult populate(Block block, Random random) {
        block.setType(Material.AIR, false);
        return BlockResult.CANCEL;
    }
}
