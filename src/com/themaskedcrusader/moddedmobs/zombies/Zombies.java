package com.themaskedcrusader.moddedmobs.zombies;

import com.themaskedcrusader.bukkit.config.Settings;
import com.themaskedcrusader.moddedmobs.Common;
import com.themaskedcrusader.moddedmobs.mobs.FasterZombie;
import net.minecraft.server.v1_5_R3.EntityTypes;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class Zombies extends Common {

    public static final String SYSTEM         = "zombies";

    public static final String FASTER_ZOMBIES = "faster-zombies";
        public static final String SLOW_WHEN_HIT = ".slow-when-damaged";
        public static final String DURATION      = ".duration";

    public static final String CUSTOM_DROPS       = ".custom-drops";
        public static final String COMMON_ITEMS   = ".common-items";
        public static final String RARE_CHANCE    = ".rare-chance";
        public static final String RARE_ITEMS     = ".rare-items";

    public static final String PLAYER_ZOMBIE  = ".player-zombies";
        public static final String PZ_INV     = ".inventory";

    public static final String BABY_ZOMBIES   = "baby-zombies";
        public static final String CHANCE     = ".chance";

    protected Zombies() {}

    public Zombies(JavaPlugin plugin) {

        if (Settings.getConfig().getBoolean(FASTER_ZOMBIES + ENABLED)) {
            try{
                Class[] args = new Class[3];
                args[0] = Class.class;
                args[1] = String.class;
                args[2] = int.class;

                Method a = EntityTypes.class.getDeclaredMethod("a", args);
                a.setAccessible(true);

                a.invoke(a, FasterZombie.class, "Zombie", 54);

            } catch (Exception e){
                plugin.getLogger().info(e.getMessage());
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
            plugin.getLogger().info("Registered new faster zombies!");
            new ZombieSpawns(plugin);

            if (Settings.getConfig().getBoolean(FASTER_ZOMBIES + SLOW_WHEN_HIT + ENABLED)) {
                new ZombieListener(plugin);
            }

        }

        if (Settings.getConfig().getBoolean(SYSTEM + CUSTOM_DROPS + ENABLED)) {
            new ZombieCustomDrops(plugin);
        }

        if (Settings.getConfig().getBoolean(SYSTEM + PLAYER_ZOMBIE + ENABLED)) {
            new ZombiePlayers(plugin);
        }

    }
}
