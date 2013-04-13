package com.themaskedcrusader.moddedmobs.zombies;

import com.themaskedcrusader.bukkit.Random;
import com.themaskedcrusader.bukkit.chest.MaskedItem;
import com.themaskedcrusader.bukkit.config.Settings;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ZombieCustomDrops extends Zombies implements Listener {
    private JavaPlugin plugin;
    ArrayList<MaskedItem> commonItems = new ArrayList<MaskedItem>();
    ArrayList<MaskedItem> rareItems   = new ArrayList<MaskedItem>();
    Random random = new Random();

    public ZombieCustomDrops(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void customDropsOnZombieDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.ZOMBIE &&
                Settings.getConfig().getBoolean(SYSTEM + CUSTOM_DROPS + ENABLED)) {
            event.getDrops().clear();
            event.getDrops().addAll(getCustomZombieDrops());
        }
    }

    private ArrayList<ItemStack> getCustomZombieDrops() {
        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();

        if (commonItems.size() == 0) {
            loadItems(commonItems, COMMON_ITEMS);
        }

        if (rareItems.size() == 0) {
            loadItems(rareItems, RARE_ITEMS);
        }

        int min = Settings.getConfig().getInt(SYSTEM + CUSTOM_DROPS + MINIMUM);
        int max = Settings.getConfig().getInt(SYSTEM + CUSTOM_DROPS + MAXIMUM);
        int total = random.nextIntBetween(min, max);

        for (int i = 0 ; i < total; i++) {
            if (random.nextInt() < Settings.getConfig().getInt(SYSTEM + CUSTOM_DROPS + RARE_CHANCE)) {
                toReturn.add(getRandomItem(rareItems));
            } else {
                toReturn.add(getRandomItem(commonItems));
            }
        }

        return toReturn;
    }

    private ItemStack getRandomItem(ArrayList<MaskedItem> list) {
        int index = random.nextInt(list.size());
        return list.get(index).unmask();
    }

    private void loadItems(ArrayList<MaskedItem> list, String key) {
        List<String> itemsInStringFormat = Settings.getConfig().getStringList(SYSTEM + CUSTOM_DROPS + key);
        for (String s : itemsInStringFormat) {
            MaskedItem item = new MaskedItem(s);
            list.add(item);
        }
    }
}
