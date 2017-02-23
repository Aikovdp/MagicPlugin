package com.elmakers.mine.bukkit.utility;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateInventoryTask extends BukkitRunnable {
	private final Player player;

	public UpdateInventoryTask(Player player) {
		this.player = player;
	}

	@Override
    @SuppressWarnings("deprecation")
	public void run() {
		DeprecatedUtils.updateInventory(player);
	}
}