package io.github.aflaiderlirk.questboard;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class QuestMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Доска заданий")) {
            return;
        }
        event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getView().getTitle().equals("Доска заданий")) {
            return;
        }
        event.setCancelled(true);
    }
}
