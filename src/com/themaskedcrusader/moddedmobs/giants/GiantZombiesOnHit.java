package com.themaskedcrusader.moddedmobs.giants;

import com.themaskedcrusader.bukkit.Random;
import com.themaskedcrusader.bukkit.config.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GiantZombiesOnHit extends Giants implements Listener {
    private JavaPlugin plugin;
    Random random = new Random();

    public GiantZombiesOnHit(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void spawnZombiesOnGiantDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.GIANT) {
            int min = Settings.getConfig().getInt(SYSTEM + HIT_ZOMBIES + MINIMUM);
            int max = Settings.getConfig().getInt(SYSTEM + HIT_ZOMBIES + MAXIMUM);
            int zombies = random.nextIntBetween(min, max);
            Location location = event.getEntity().getLocation();
            World world = location.getWorld();

            for (int i = 0 ; i < zombies ; i++) {
                LivingEntity zombie = (LivingEntity) world.spawnEntity(location, EntityType.ZOMBIE);
                CreatureSpawnEvent spawn = new CreatureSpawnEvent(zombie, CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
                plugin.getServer().getPluginManager().callEvent(spawn);
            }
        }
    }
}
