package me.emmy.tulip.game;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.game.impl.DefaultGameImpl;
import me.emmy.tulip.feature.kit.Kit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
@Getter
public class GameRepository {
    private final List<AbstractGame> matches = new ArrayList<>();

    /**
     * Load all FFA matches
     */
    public void loadFFAMatches() {
        FileConfiguration config = ConfigService.getInstance().getFfaConfig();

        ConfigurationSection ffaConfig = config.getConfigurationSection("ffa");
        if (ffaConfig == null) {
            return;
        }

        for (String kitName : ffaConfig.getKeys(false)) {
            String name = "ffa." + kitName;

            Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);
            if (kit == null) {
                continue;
            }

            String arenaName = config.getString(name + ".arena");
            Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
            if (arena == null) {
                continue;
            }

            int maxPlayers = config.getInt(name + ".maxPlayers");
            matches.add(new DefaultGameImpl(kitName, arena, kit, maxPlayers));
        }
    }

    /**
     * Save all FFA matches
     */
    public void saveFFAMatches() {
        matches.forEach(this::saveFFAMatch);
    }

    /**
     * Save an FFA match
     *
     * @param match The match to save
     */
    public void saveFFAMatch(AbstractGame match) {
        String name = "ffa." + match.getKit().getName();

        FileConfiguration config = ConfigService.getInstance().getFfaConfig();
        config.set(name, null);
        config.set(name + ".arena", match.getArena().getName());
        config.set(name + ".maxPlayers", match.getMaxPlayers());
        Tulip.getInstance().getConfigService().saveConfig(Tulip.getInstance().getConfigService().getConfigFile("storage/ffa.yml"), config);
    }

    /**
     * Create a new FFA match
     *
     * @param arena The arena the match is being played in
     * @param kit The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public void createFFAMatch(Arena arena, Kit kit, int maxPlayers) {
        DefaultGameImpl match = new DefaultGameImpl(kit.getName(), arena, kit, maxPlayers);
        matches.add(match);
        Tulip.getInstance().getGameRepository().saveFFAMatch(match);
    }

    /**
     * Delete an FFA match
     *
     * @param match The match to delete
     */
    public void deleteFFAMatch(AbstractGame match) {
        matches.remove(match);
        FileConfiguration config = ConfigService.getInstance().getFfaConfig();
        config.set("ffa." + match.getKit().getName(), null);
        Tulip.getInstance().getGameRepository().saveFFAMatches();
    }

    /**
     * Get an FFA match by kit name
     *
     * @param kitName The name of the kit
     * @return The FFA match
     */
    public AbstractGame getFFAMatch(String kitName) {
        return matches.stream().filter(match -> match.getKit().getName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
    }

    /**
     * Get an FFA match by player
     *
     * @param player The player
     * @return The FFA match
     */
    public AbstractGame getFFAMatch(Player player) {
        return matches.stream().filter(match -> match.getPlayers().contains(player)).findFirst().orElse(null);
    }
}
