package me.emmy.tulip.profile.listener;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.feature.hotbar.HotbarUtility;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 18:38
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(player.getUniqueId());
        profile.loadProfile();

        ProfileRepository profileRepository = Tulip.getInstance().getProfileRepository();
        profileRepository.addProfile(profile.getUuid(), profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ProfileRepository profileRepository = Tulip.getInstance().getProfileRepository();
        Profile profile = profileRepository.getProfile(player.getUniqueId());
        profile.setName(player.getName());
        profile.setState(EnumProfileState.SPAWN);

        if (profile.isFirstJoin()) {
            this.sendWelcomeMessage("first-join-message", player);
        } else {
            this.sendWelcomeMessage("welcome-message", player);
        }

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        player.getInventory().setHeldItemSlot(0);
        HotbarUtility.applyHotbarItems(player);

        if (player.isOp()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.saveProfile();
        event.setQuitMessage(null);
    }

    /**
     * Sends a welcome message to the player.
     *
     * @param path   The path in the config file for the message.
     * @param player The player to send the message to.
     */
    private void sendWelcomeMessage(String path, Player player) {
        List<String> messages = ConfigService.getInstance().getLocaleConfig().getStringList(path);
        for (String message : messages) {
            player.sendMessage(CC.translate(message).replace("{player}", player.getName()));
        }
    }
}