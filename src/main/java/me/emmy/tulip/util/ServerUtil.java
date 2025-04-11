package me.emmy.tulip.util;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.Tulip;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Level;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 15:06
 */
@UtilityClass
public class ServerUtil {
    /**
     * Shutdown the server.
     */
    public void shutDown(int delay) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&cServer is shutting down..."));
        System.exit(delay);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.prefix + "&fDisabling the plugin..."));
        Bukkit.getPluginManager().disablePlugin(Tulip.getInstance());
    }

    /**
     * Disconnect all players.
     */
    public void disconnectPlayers() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&cDisconnecting players..."));
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(CC.translate("&cServer is restarting...")));
    }

    /**
     * Stop all tasks.
     */
    public void stopTasks() {
        if (Bukkit.getScheduler().getActiveWorkers().isEmpty()) {
            return;
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&cStopping tasks..."));
        Bukkit.getScheduler().cancelTasks(Tulip.getInstance());
    }

    /**
     * Set up the world, clear entities, and set the time to day and disable weather.
     */
    public void setupWorld() {
        for (World world : Bukkit.getWorlds()) {
            if (world.isThundering() || world.hasStorm()) {
                world.setThundering(false);
                world.setStorm(false);
                world.setDifficulty(Difficulty.EASY);
            }

            world.setTime(6000L);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");

            world.getEntities().forEach(entity -> {
                if (entity.getType().name().equals("DROPPED_ITEM")) {
                    entity.remove();
                }
            });
        }
    }
}