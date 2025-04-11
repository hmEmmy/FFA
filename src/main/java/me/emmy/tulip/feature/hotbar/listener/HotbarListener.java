package me.emmy.tulip.feature.hotbar.listener;

import me.emmy.tulip.feature.hotbar.HotbarItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Emmy
 * @project FFA
 * @date 28/07/2024 - 21:10
 */
public class HotbarListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) {
            return;
        }

        HotbarItem hotbarItem = HotbarItem.getItem(event.getItem());
        if (hotbarItem == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (hotbarItem.getCommand() != null) {
            player.performCommand(hotbarItem.getCommand());
        }

        event.setCancelled(true);
    }
}
