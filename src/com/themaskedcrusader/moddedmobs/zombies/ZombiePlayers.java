package com.themaskedcrusader.moddedmobs.zombies;

import com.themaskedcrusader.bukkit.chest.MaskedItem;
import com.themaskedcrusader.bukkit.config.Settings;
import com.themaskedcrusader.moddedmobs.mobs.FasterZombie;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ZombiePlayers extends Zombies implements Listener {
    private JavaPlugin plugin;
    HashMap<UUID, ArrayList<MaskedItem>> playerZombies = new HashMap<UUID, ArrayList<MaskedItem>>();

    public ZombiePlayers(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void spawnZombieOnPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();
        World world = location.getWorld();
        Entity zombie;

        if (Settings.getConfig().getBoolean(FASTER_ZOMBIES + ENABLED)) {
            net.minecraft.server.v1_4_R1.World mcWorld = ((CraftWorld) world).getHandle();
            FasterZombie playerZombie = new FasterZombie(mcWorld);
            playerZombie.setPosition(location.getX(), location.getY(), location.getZ());
            mcWorld.addEntity(playerZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);

            zombie = playerZombie.getBukkitEntity();
        } else {
            zombie = world.spawnEntity(location, EntityType.ZOMBIE);
        }

        if (Settings.getConfig().getBoolean(SYSTEM + PLAYER_ZOMBIE + PZ_INV)) {
            ArrayList<MaskedItem> playerInv = new ArrayList<MaskedItem>();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    MaskedItem ma = new MaskedItem(item);
                    playerInv.add(ma);
                }
            }

            playerInv.add(getMaskedItem(player.getEquipment().getHelmet()));
            playerInv.add(getMaskedItem(player.getEquipment().getChestplate()));
            playerInv.add(getMaskedItem(player.getEquipment().getLeggings()));
            playerInv.add(getMaskedItem(player.getEquipment().getBoots()));

            playerZombies.put(zombie.getUniqueId(), playerInv);
            event.getDrops().clear();

            ((LivingEntity) zombie).getEquipment().setHelmet(getPlayerHead(player));
            ((LivingEntity) zombie).getEquipment().setChestplate(player.getEquipment().getChestplate());
            ((LivingEntity) zombie).getEquipment().setLeggings(player.getEquipment().getLeggings());
            ((LivingEntity) zombie).getEquipment().setBoots(player.getEquipment().getBoots());

        }

        ((LivingEntity) zombie).setRemoveWhenFarAway(false);
    }

    @EventHandler
    public void dropInventoryOnPlayerZombieDeath(EntityDeathEvent event) {
        if (playerZombies.containsKey(event.getEntity().getUniqueId())) {
            event.getDrops().clear();
            event.getDrops().addAll(unmaskInventory(playerZombies.get(event.getEntity().getUniqueId())));
            ItemStack head = event.getEntity().getEquipment().getHelmet().clone();
            event.getDrops().add(head);
            playerZombies.remove(event.getEntity().getUniqueId());
        }
    }

    private ArrayList<ItemStack> unmaskInventory(ArrayList<MaskedItem> maskedItems) {
        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();
        for (MaskedItem item : maskedItems) {
            if (item != null) {
                toReturn.add(item.unmask());
            }
        }
        return toReturn;
    }

    private MaskedItem getMaskedItem(ItemStack item) {
        return (item != null) ? new MaskedItem(item): null;
    }

    private ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        MaskedItem itemHead = new MaskedItem(skull);
        itemHead.setPlayerName(player.getDisplayName());
        return itemHead.unmask();
    }

}
