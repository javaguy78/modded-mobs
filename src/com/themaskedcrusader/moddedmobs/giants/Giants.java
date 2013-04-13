package com.themaskedcrusader.moddedmobs.giants;

import com.themaskedcrusader.bukkit.config.Settings;
import com.themaskedcrusader.moddedmobs.Common;
import org.bukkit.plugin.java.JavaPlugin;

public class Giants extends Common {

    protected static final String SYSTEM = "giants";
    protected static final String HIT_ZOMBIES = ".zombies-on-hit";
    protected static final String JUMP_BOMB   = ".explode-on-jump";

    protected Giants() {}

    public Giants(JavaPlugin plugin) {

        if (Settings.getConfig().getBoolean(SYSTEM + ENABLED)) {

            if (Settings.getConfig().getBoolean(SYSTEM + HIT_ZOMBIES + ENABLED)) {
                new GiantZombiesOnHit(plugin);
            }

            if (Settings.getConfig().getBoolean(SYSTEM + JUMP_BOMB)) {
                new GiantJumpBombs(plugin);
            }

        }

    }

}
