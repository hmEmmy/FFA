package me.emmy.tulip.feature.hotbar;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project FFA
 * @date 28/07/2024 - 23:12
 */
@UtilityClass
public class HotbarUtility {
    /**
     * Apply hotbar items.
     *
     * @param player the player
     */
    public void applyHotbarItems(Player player) {
        Arrays.stream(HotbarItem.values())
                .filter(HotbarItem::isItemEnabledInConfig)
                .forEach(hotbarItem -> player.getInventory().setItem(hotbarItem.getSlot(), hotbarItem.createItem()))
        ;
    }

    /**
     * Remove only the hotbar items.
     *
     * @param player the player
     */
    public void removeHotbarItems(Player player) {
        Arrays.stream(HotbarItem.values())
                .filter(HotbarItem::isItemEnabledInConfig)
                .forEach(hotbarItem -> player.getInventory().setItem(hotbarItem.getSlot(), null))
        ;
    }
}