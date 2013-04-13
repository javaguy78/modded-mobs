package com.themaskedcrusader.moddedmobs;

import com.themaskedcrusader.bukkit.config.Settings;
import com.themaskedcrusader.moddedmobs.commands.Commands;
import com.themaskedcrusader.moddedmobs.giants.Giants;
import com.themaskedcrusader.moddedmobs.zombies.Zombies;
import net.minecraft.server.v1_5_R2.MobSpawnerAbstract;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Plugin extends JavaPlugin {
    public File[] libs =  new File[] {
            new File(getDataFolder(), "tmc-lib.jar"),
    };

    public void onDisable() {
        this.getLogger().info("modded mobs shut down");
    }

    public void onEnable() {
        LibLoader.loadLibraries(this);
        loadDefaultSettings();
        new Zombies(this);
        new Giants(this);
        getLogger().info("TMC Modded Mobs Loaded!");
    }

    private void loadDefaultSettings() {
        Settings.init(this);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Commands c = new Commands(this);
        c.processCommand(commandSender, strings);
        return true;
    }

}
