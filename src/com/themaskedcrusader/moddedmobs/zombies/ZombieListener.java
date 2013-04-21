package com.themaskedcrusader.moddedmobs.zombies;

import com.themaskedcrusader.bukkit.config.Settings;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieListener extends Zombies implements Listener {

    public ZombieListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void slowTheZombiesDown(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.ZOMBIE) {
            int duration = Settings.getConfig().getInt(FASTER_ZOMBIES + SLOW_WHEN_HIT + DURATION) * 20;
            PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, duration, 3);
            ((LivingEntity) event.getEntity()).addPotionEffect(slowness);
        }
    }
}
