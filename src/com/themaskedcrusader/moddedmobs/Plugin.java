package com.themaskedcrusader.moddedmobs;

import com.themaskedcrusader.bukkit.Library;
import com.themaskedcrusader.bukkit.config.Settings;
import com.themaskedcrusader.moddedmobs.commands.Commands;
import com.themaskedcrusader.moddedmobs.giants.Giants;
import com.themaskedcrusader.moddedmobs.zombies.Zombies;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Plugin extends JavaPlugin {

    public void onDisable() {
        this.getLogger().info("modded mobs shut down");
    }

    public void onEnable() {
        try {
            loadDefaultSettings();
            new Zombies(this);
            new Giants(this);
            getLogger().info("TMC Modded Mobs Loaded!");
        } catch (NoClassDefFoundError e) {
            getLogger().log(Level.SEVERE,  "TMC-LIB Library Missing or cannot load: Disabling Plugin.");
            getLogger().log(Level.SEVERE,  "See install instructions at http://dev.bukkit.org/server-mods/tmc-lib/");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void loadDefaultSettings() {
        Settings.init(this);
        if (Settings.getConfig().getBoolean("check-for-updates")) {
            Library.checkForNewVersion(getServer().getConsoleSender());
        }
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Commands c = new Commands(this);
        c.processCommand(commandSender, strings);
        return true;
    }

}
