package me.emmy.tulip.feature.spawn.listener;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.profile.data.kitlayout.menu.KitLayoutEditorMenu;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author Emmy
 * @project FFA
 * @date 29/07/2024 - 00:11
 */
public class SpawnListener implements Listener {
    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        if (ConfigService.getInstance().getSettingsConfig().getBoolean("listeners.weather-change")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onHunger(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onMoveItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Menu openedMenu = Menu.currentlyOpenedMenus.get(player.getName());
        if (openedMenu instanceof KitLayoutEditorMenu) {
            return;
        }

        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
            if (event.getWhoClicked() instanceof Player) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    if (event.getClickedInventory() != null && event.getClickedInventory().equals(player.getInventory())) {
                        event.setCancelled(true);
                    }

                    if (event.getSlotType() == InventoryType.SlotType.CRAFTING || event.isShiftClick() || event.getClick().isKeyboardClick()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onItemPickUp(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
                if (player.getLocation().getY() < 0) {
                    Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
                }

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onEntityBlockDamage(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() == EnumProfileState.SPAWN) {
                event.setCancelled(true);
            }
        }
    }
}
