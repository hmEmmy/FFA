package me.emmy.tulip.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 21:30
 */
@UtilityClass
public class Logger {
    /**
     * Logs an info message to the console
     *
     * @param message the message to log
     */
    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.errorPrefix + message));
    }
}