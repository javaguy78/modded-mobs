package com.themaskedcrusader.moddedmobs.zombies;

import com.themaskedcrusader.bukkit.Random;
import com.themaskedcrusader.bukkit.config.Settings;
import com.themaskedcrusader.moddedmobs.mobs.FasterZombie;
import net.minecraft.server.v1_4_R1.EntityZombie;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombieSpawns extends Zombies implements Listener {
    JavaPlugin plugin;
    Random random = new Random();

    public ZombieSpawns(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void changeStandardZombiesToFasterZombies(CreatureSpawnEvent event) {
        if (event.isCancelled()) return;
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) return;

        Location location = event.getLocation();
        Entity entity = event.getEntity();
        World world = location.getWorld();

        net.minecraft.server.v1_4_R1.World mcWorld = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_4_R1.Entity mcEntity = (((CraftEntity) entity).getHandle());

        if (entity.getType() == EntityType.ZOMBIE && !(mcEntity instanceof FasterZombie)){
            FasterZombie fasterZombie = new FasterZombie(mcWorld);

            fasterZombie.setPosition(location.getX(), location.getY(), location.getZ());

            mcWorld.removeEntity(mcEntity);
            mcWorld.addEntity(fasterZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void changeRegularZombieToBabyZombie(CreatureSpawnEvent event) {
        if (Settings.getConfig().getBoolean(SYSTEM + BABY_ZOMBIES + ENABLED)) {
            if (random.nextInt() < Settings.getConfig().getInt(SYSTEM + BABY_ZOMBIES + CHANCE) &&
                    event.getEntityType() == EntityType.ZOMBIE) {
                Entity entity = event.getEntity();
                EntityZombie zombie = (EntityZombie) ((CraftEntity) entity).getHandle();
                zombie.setBaby(true);
            }
        }
    }
}
