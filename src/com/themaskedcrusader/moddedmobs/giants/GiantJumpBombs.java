package com.themaskedcrusader.moddedmobs.giants;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GiantJumpBombs extends Giants implements Listener {
    private JavaPlugin plugin;

    public GiantJumpBombs(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

//    @EventHandler
//    public void explodeOnJump() {}
}
