package me.emmy.tulip.game.spawn.task;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.game.enums.EnumFFAState;
import me.emmy.tulip.util.Cuboid;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project FFA
 * @date 12/06/2024 - 22:26
 */
public class FFASpawnTask extends BukkitRunnable {
    private final Cuboid cuboid;
    private final Tulip plugin;
    private final Map<UUID, EnumFFAState> playerStates;

    /**
     * Constructor for the FFASpawnTask class.
     *
     * @param cuboid The cuboid of the safezone.
     * @param plugin The main class instance.
     */
    public FFASpawnTask(Cuboid cuboid, Tulip plugin) {
        this.cuboid = cuboid;
        this.plugin = plugin;
        this.playerStates = new HashMap<>();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
            if (profile != null && profile.getState() == EnumProfileState.FFA) {
                EnumFFAState currentState = playerStates.getOrDefault(player.getUniqueId(), EnumFFAState.FIGHTING);
                boolean isInSpawn = cuboid.isIn(player);
                if (isInSpawn && currentState != EnumFFAState.SPAWN) {
                    profile.getGame().setState(EnumFFAState.SPAWN);
                    playerStates.put(player.getUniqueId(), EnumFFAState.SPAWN);
                    player.sendMessage(CC.translate("&aYou have entered the FFA spawn."));
                } else if (!isInSpawn && currentState != EnumFFAState.FIGHTING) {
                    profile.getGame().setState(EnumFFAState.FIGHTING);
                    playerStates.put(player.getUniqueId(), EnumFFAState.FIGHTING);
                    player.sendMessage(CC.translate("&cYou have exited the FFA spawn."));
                }
            }
        }
    }
}