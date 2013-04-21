package com.themaskedcrusader.moddedmobs.commands;

import com.themaskedcrusader.bukkit.util.StringUtils;
import com.themaskedcrusader.moddedmobs.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class Commands {
    JavaPlugin plugin = new Plugin();

    public Commands(JavaPlugin plugin) {
        if (this.plugin == null) {
            this.plugin = plugin;
        }
    }

    public void processCommand(CommandSender sender, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("spawn"))         { spawnEntity(sender, args);   return; }
            if (args[0].equalsIgnoreCase("butcher"))       { butcherMobs(sender, args);   return; }
            if (args[0].equalsIgnoreCase("help"))          { commandHelp(sender);         return; }

        } catch (Exception ignored) {
            plugin.getLogger().info(ignored.getMessage());
        }

        sender.sendMessage(ChatColor.RED + "Command not found... please try again");
    }

    private void spawnEntity(CommandSender sender, String[] args) {
        Location locationToSpawn;
        int x, y, z;
        String worldName;

        try {
            String entityType = args[1];

            if (sender instanceof Player && args.length < 3) {
                locationToSpawn = ((Player) sender).getLocation();
            } else {
                x = Integer.parseInt(args[2]);
                y = Integer.parseInt(args[3]);
                z = Integer.parseInt(args[4]);
                if (args.length == 6) {
                    worldName  = args[5];
                    World world = plugin.getServer().getWorld(worldName);
                    locationToSpawn = new Location(world, x, y, z);
                } else {
                    locationToSpawn = ((Player) sender).getLocation();
                    locationToSpawn.setX(x);
                    locationToSpawn.setY(y);
                    locationToSpawn.setZ(z);
                }
            }

            World world = locationToSpawn.getWorld();
            LivingEntity entity = (LivingEntity) world.spawnEntity(locationToSpawn, EntityType.valueOf(entityType.toUpperCase()));
            CreatureSpawnEvent event = new CreatureSpawnEvent(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            try {
                plugin.getServer().getPluginManager().callEvent(event);
            } catch (Exception ignored) {} // for some reason, this throws an NPE, but still works.

        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Syntax Error: Command Format:");
            sender.sendMessage(ChatColor.RED + "spawn <entityType> [ x y z worldName ]");
        }
    }

    private void butcherMobs(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Collection<Entity> worldEntities;
            World world = ((Player) sender).getWorld();
            try {
                String entityType = StringUtils.capitalize(args[1]);
                Class clazz = Class.forName("org.bukkit.entity." + entityType);
                worldEntities = world.getEntitiesByClass(clazz);
            } catch (Exception ignored) {
                worldEntities = world.getEntities();
            }

            for (Entity entity : worldEntities) {
                if (!(entity instanceof Player)) {
                    Location location = entity.getLocation();
                    location.setY(-1);
                    entity.teleport(location);
                }
            }

        } else {
            sender.sendMessage(ChatColor.RED + "This command must be executed in game");
        }
    }

    private void commandHelp(CommandSender sender) {
        ChatColor G = ChatColor.GREEN;
        ChatColor R = ChatColor.RED;
        ChatColor B = ChatColor.AQUA;

        sender.sendMessage(G + "Available Commands:");
        sender.sendMessage(G + "Spawn: Spawns a mob");
        sender.sendMessage(R + "Syntax: " + B + "/mobadmin spawn <mob name> [ x y z [world name]]");
        sender.sendMessage(R + "Example: " + B + "/mobadmin spawn giant 0 64 0 world");
        sender.sendMessage(G + "Butcher: Kills all mobs or all of a type");
        sender.sendMessage(R + "Syntax: " + B + "/mobadmin butcher <mob type>");
        sender.sendMessage(R + "Kill All Zombies: " + B + "/mobadmin butcher zombie");
        sender.sendMessage(R + "Kill All Mobs: " + B + "/mobadmin butcher");

    }
}
