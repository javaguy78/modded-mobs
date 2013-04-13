package com.themaskedcrusader.moddedmobs.zombies;

import com.themaskedcrusader.moddedmobs.mobs.FasterZombie;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombieSpawns extends Zombies implements Listener {
    JavaPlugin plugin;

    public ZombieSpawns(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void changeStandardZombiesToFasterZombies(CreatureSpawnEvent event) {
        if (event.isCancelled()) return;
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) return;

        Location location = event.getLocation();
        Entity entity = event.getEntity();
        World world = location.getWorld();

        net.minecraft.server.v1_5_R2.World mcWorld = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_5_R2.Entity mcEntity = (((CraftEntity) entity).getHandle());

        if (entity.getType() == EntityType.ZOMBIE && mcEntity instanceof FasterZombie == false){
            FasterZombie fasterZombie = new FasterZombie(mcWorld);

            fasterZombie.setPosition(location.getX(), location.getY(), location.getZ());

            mcWorld.removeEntity(mcEntity);
            mcWorld.addEntity(fasterZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);

        }
    }
}
