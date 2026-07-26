package com.dayshulkers;

import com.dayshulkers.commands.DayShulkersCommand;
import com.dayshulkers.listeners.ShulkerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DayShulkers extends JavaPlugin {

    private static DayShulkers instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new ShulkerListener(this), this);

        DayShulkersCommand executor = new DayShulkersCommand(this);
        getCommand("dayshulkers").setExecutor(executor);
        getCommand("dayshulkers").setTabCompleter(executor);

        getLogger().info("DayShulkers activado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DayShulkers desactivado.");
    }

    public static DayShulkers getInstance() {
        return instance;
    }
}
