package com.dayshulkers.listeners;

import com.dayshulkers.DayShulkers;
import com.dayshulkers.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShulkerListener implements Listener {

    private final DayShulkers plugin;
    private final Map<UUID, Session> openSessions = new HashMap<>();

    public ShulkerListener(DayShulkers plugin) {
        this.plugin = plugin;
    }

    // Cubre tanto click derecho sobre un bloque (RIGHT_CLICK_BLOCK) como
    // click derecho mirando al cielo / sin nada en frente (RIGHT_CLICK_AIR).
    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission("dayshulkers.use")) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !isShulkerBox(item.getType())) {
            return;
        }

        if (!(item.getItemMeta() instanceof BlockStateMeta meta) || !(meta.getBlockState() instanceof ShulkerBox)) {
            return;
        }

        // Cancelamos la colocación por completo: en su lugar, la abrimos.
        event.setCancelled(true);
        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.DENY);

        openShulker(player, item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        HumanEntity human = event.getPlayer();
        if (!(human instanceof Player player)) {
            return;
        }

        Session session = openSessions.remove(player.getUniqueId());
        if (session == null) {
            return;
        }

        ShulkerBox shulkerBox = session.shulkerBox;
        shulkerBox.update();

        session.meta.setBlockState(shulkerBox);
        session.item.setItemMeta(session.meta);

        Map<Integer, ItemStack> leftover = player.getInventory().addItem(session.item);
        for (ItemStack extra : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), extra);
        }

        sendFeedback(player, "close-shulker");
    }

    private void openShulker(Player player, ItemStack clickedItem) {
        ItemStack itemToOpen;

        if (clickedItem.getAmount() > 1) {
            // Las shulkers con items nunca se pueden stackear, así que si hay más de una
            // en el stack, están vacías: separamos una sola copia para editar.
            itemToOpen = clickedItem.clone();
            itemToOpen.setAmount(1);
            clickedItem.setAmount(clickedItem.getAmount() - 1);
        } else {
            itemToOpen = clickedItem.clone();
            int slot = player.getInventory().first(clickedItem);
            if (slot != -1) {
                player.getInventory().setItem(slot, new ItemStack(Material.AIR));
            } else {
                // Fallback: mano principal
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
        }

        BlockStateMeta meta = (BlockStateMeta) itemToOpen.getItemMeta();
        ShulkerBox shulkerBox = (ShulkerBox) meta.getBlockState();

        openSessions.put(player.getUniqueId(), new Session(itemToOpen, meta, shulkerBox));
        player.openInventory(shulkerBox.getInventory());

        sendFeedback(player, "open-shulker");
    }

    private boolean isShulkerBox(Material material) {
        return material.name().endsWith("SHULKER_BOX");
    }

    private void sendFeedback(Player player, String path) {
        var cfg = plugin.getConfig();

        String message = cfg.getString(path + ".message", "");
        String title = cfg.getString(path + ".title", "");
        String subtitle = cfg.getString(path + ".subtitle", "");
        String soundName = cfg.getString(path + ".sound", "");

        if (message != null && !message.isEmpty()) {
            player.sendMessage(ColorUtils.colorize(message));
        }

        if ((title != null && !title.isEmpty()) || (subtitle != null && !subtitle.isEmpty())) {
            int fadeIn = cfg.getInt("title-fade-in", 10);
            int stay = cfg.getInt("title-stay", 40);
            int fadeOut = cfg.getInt("title-fade-out", 10);

            player.sendTitle(
                    ColorUtils.colorize(title),
                    ColorUtils.colorize(subtitle),
                    fadeIn, stay, fadeOut
            );
        }

        if (soundName != null && !soundName.isEmpty()) {
            try {
                Sound sound = Sound.valueOf(soundName.toUpperCase());
                player.playSound(player.getLocation(), sound, 1f, 1f);
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Sonido inválido en config.yml: " + soundName);
            }
        }
    }

    private static class Session {
        final ItemStack item;
        final BlockStateMeta meta;
        final ShulkerBox shulkerBox;

        Session(ItemStack item, BlockStateMeta meta, ShulkerBox shulkerBox) {
            this.item = item;
            this.meta = meta;
            this.shulkerBox = shulkerBox;
        }
    }
        }
