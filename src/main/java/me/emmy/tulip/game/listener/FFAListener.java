package me.emmy.tulip.game.listener;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.feature.cooldown.Cooldown;
import me.emmy.tulip.feature.cooldown.CooldownRepository;
import me.emmy.tulip.game.spawn.FFASpawnHandler;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.task.ClearLagTask;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Emmy
 * @project FFA
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            event.setDeathMessage(null);

            List<Item> droppedItems = new ArrayList<>();
            for (ItemStack drop : event.getDrops()) {
                if (drop != null && drop.getType() != Material.AIR) {
                    droppedItems.add(player.getWorld().dropItemNaturally(player.getLocation(), drop));
                }
            }
            event.getDrops().clear();

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Item item : droppedItems) {
                        if (item != null && item.isValid()) {
                            item.remove();
                        }
                    }
                }
            }.runTaskLater(Tulip.getInstance(), 100L);

            Player killer = PlayerUtil.getLastAttacker(player);

            Tulip.getInstance().getServer().getScheduler().runTaskLater(Tulip.getInstance(), () -> player.spigot().respawn(), 1L);

            Bukkit.getScheduler().runTaskLater(Tulip.getInstance(), () -> profile.getGame().handleDeath(player, killer), 1L);
        }
    }


    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            profile.getGame().leave(player);
        }
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            profile.getGame().leave(player);
        }
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(event.getEntity().getUniqueId());
            if (profile.getState() == EnumProfileState.FFA) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                PlayerUtil.setLastAttacker(player, damager);
            }
        }
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        FFASpawnHandler ffaSpawnHandler = Tulip.getInstance().getFfaSpawnHandler();

        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();

            if (ffaSpawnHandler.getCuboid().isIn(victim) || ffaSpawnHandler.getCuboid().isIn(attacker)) {
                event.setCancelled(true);
                attacker.sendMessage(CC.translate("&cYou cannot fight at spawn."));
                return;
            }
        }

        if (event.getDamager() instanceof FishHook) {
            FishHook fishHook = (FishHook) event.getDamager();
            if (fishHook.getShooter() instanceof Player) {
                Player shooter = (Player) fishHook.getShooter();
                if (ffaSpawnHandler.getCuboid().isIn(victim) || ffaSpawnHandler.getCuboid().isIn(shooter)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                if (ffaSpawnHandler.getCuboid().isIn(victim) || ffaSpawnHandler.getCuboid().isIn(shooter)) {
                    event.setCancelled(true);
                    shooter.sendMessage(CC.translate("&cYou cannot fight at spawn."));
                    return;
                }

                double health = Math.ceil((victim.getHealth() - event.getFinalDamage()) / 2.0D);
                if (health > 0.0D && !victim.getName().equals(shooter.getName())) {
                    shooter.sendMessage(CC.translate(ConfigService.getInstance().getLocaleConfig().getString("game.arrow-hit")
                            .replace("{victim}", victim.getName())
                            .replace("{hearts}", String.valueOf(health)))
                    );
                }
            }
        }
    }

    /**
     * Handles the BlockPlaceEvent. The event is cancelled if the player is in the FFA state and tries to place a block.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.isBuildMode()) return;
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the BlockBreakEvent. The event is cancelled if the player is in the FFA state and tries to break a block.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.isBuildMode()) return;
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the PlayerInteractEvent. The event is cancelled if the player is in the FFA state and tries to interact with an item.
     *
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = event.getItem();

        if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP && player.getHealth() < 19.5) {
            player.setHealth(Math.min(player.getHealth() + 7.0, 20.0));
            player.getItemInHand().setType(Material.BOWL);
            player.updateInventory();
        }

        if (profile.getState() == EnumProfileState.FFA && item != null && item.getType() == Material.ENDER_PEARL) {
            if (event.getAction().name().contains("RIGHT_CLICK")) {
                if (Tulip.getInstance().getFfaSpawnHandler().getCuboid().isIn(player)) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
                    return;
                }

                CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));

                if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(CC.translate(Locale.FFA_ENDERPEARL_COOLDOWN.getStringPath().replace("{time}", String.valueOf(optionalCooldown.get().remainingTime()))));
                    return;
                }

                Cooldown cooldown = optionalCooldown.orElseGet(() -> {
                    Cooldown newCooldown = new Cooldown(15 * 1000L, () -> player.sendMessage(CC.translate(Locale.FFA_COOLDOWN_EXPIRED.getStringPath())));
                    cooldownRepository.addCooldown(player.getUniqueId(), "ENDERPEARL", newCooldown);
                    return newCooldown;
                });

                cooldown.resetCooldown();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            FFASpawnHandler ffaSpawnHandler = Tulip.getInstance().getFfaSpawnHandler();

            if (profile.getState() == EnumProfileState.FFA) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if (ffaSpawnHandler.getCuboid().isIn(player)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            new ClearLagTask().runTaskLater(Tulip.getInstance(), 20L);
        }
    }
}
