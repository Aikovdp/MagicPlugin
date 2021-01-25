package com.elmakers.mine.bukkit.tasks;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import com.elmakers.mine.bukkit.api.magic.Mage;
import com.elmakers.mine.bukkit.magic.MagicController;

public class MageUpdateTask implements Runnable {
    private final MagicController controller;

    public MageUpdateTask(MagicController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        Collection<? extends Mage> mages = controller.getMutableMages();
        for (Iterator<? extends Mage> iterator = mages.iterator(); iterator.hasNext();) {
            Mage mage = iterator.next();

            if (!mage.isValid())
            {
                // Players are handled by logout
                if (!mage.isPlayer() && !mage.hasPending()) {
                    mage.removed();
                    iterator.remove();
                }
                continue;
            }
            try {
               mage.tick();
            } catch (Exception ex) {
                controller.getLogger().log(Level.WARNING, "Error ticking Mage " + mage.getName(), ex);
            }
        }
    }
}
