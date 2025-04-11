package me.emmy.tulip.game;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.game.enums.EnumFFAState;
import me.emmy.tulip.feature.kit.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
@Getter
@Setter
public abstract class AbstractGame {

    private EnumFFAState state = EnumFFAState.SPAWN;

    private final String name;
    private final Arena arena;
    private final Kit kit;
    private int maxPlayers;
    private List<Player> players;

    /**
     * Constructor for the AbstractGame class
     *
     * @param name The name of the match
     * @param arena The arena the match is being played in
     * @param kit The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public AbstractGame(String name, Arena arena, Kit kit, int maxPlayers) {
        this.name = name;
        this.arena = arena;
        this.kit = kit;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
    }

    public abstract void join(Player player);
    public abstract void setupPlayer(Player player);
    public abstract void handleDeath(Player player, Player killer);
    public abstract void handleRespawn(Player player);
    public abstract void leave(Player player);

    /**
     * Broadcast a message to all players in the match
     *
     * @param message The message
     */
    public void broadcastMessage(String message) {
        players.forEach(player -> player.sendMessage(message));
    }
}
