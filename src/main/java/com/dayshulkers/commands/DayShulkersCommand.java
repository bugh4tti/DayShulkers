package com.dayshulkers.commands;

import com.dayshulkers.DayShulkers;
import com.dayshulkers.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DayShulkersCommand implements CommandExecutor, TabCompleter {

    private final DayShulkers plugin;

    public DayShulkersCommand(DayShulkers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                if (!sender.hasPermission("dayshulkers.reload")) {
                    sender.sendMessage(ColorUtils.colorize("&cNo tienes permiso para usar este comando."));
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(ColorUtils.colorize("&#00DAFF&l[DayShulkers] &fConfiguración recargada correctamente."));
            }
            case "help" -> sendHelp(sender);
            default -> sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ColorUtils.colorize("&#00DAFF&l------- DayShulkers -------"));
        sender.sendMessage(ColorUtils.colorize("&fVersión: &b" + plugin.getDescription().getVersion()));
        sender.sendMessage(ColorUtils.colorize("&fAutor: &b" + String.join(", ", plugin.getDescription().getAuthors())));
        sender.sendMessage("");
        sender.sendMessage(ColorUtils.colorize("&e/dayshulkers reload &7- Recarga config.yml sin reiniciar."));
        sender.sendMessage(ColorUtils.colorize("&e/dayshulkers help &7- Muestra esta ayuda."));
        sender.sendMessage("");
        sender.sendMessage(ColorUtils.colorize("&fClic derecho con una Shulker en la mano para abrirla sin colocarla."));
        sender.sendMessage(ColorUtils.colorize("&7Funciona con las 16 shulkers de color y la sin teñir."));
        sender.sendMessage(ColorUtils.colorize("&#00DAFF&l---------------------------"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "help");
        }
        return Collections.emptyList();
    }
                  }
