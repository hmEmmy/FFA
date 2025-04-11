package me.emmy.tulip.util;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.Tulip;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 21:30
 */
@UtilityClass
public class CC {
    public String prefix = translate("&8[&dTulip&8] &7");
    public String errorPrefix = translate("&4[&cError&4] &7");

    /**
     * Translate a string with color codes
     *
     * @param string the string to translate
     * @return the translated string
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Translate a list of strings with color codes
     *
     * @param message the list of strings to translate
     * @return the translated list of strings
     */
    public List<String> translate(List<String> message) {
        List<String> list = new ArrayList<>();

        for (String line : message) {
            list.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return list;
    }

    /**
     * Send the startup message to the console
     */
    public void sendStartupMessage() {
        List<String> message = Arrays.asList(
                "&7&m--------------------------------------------",
                "&d&lTULIP FFA CORE",
                "",
                "{",
                "",
                "  Author: &d" + Tulip.getInstance().getDescription().getAuthors().get(0),
                "  Version: &d" + Tulip.getInstance().getDescription().getVersion(),
                "  Github: &dhttps://github.com/hmEmmy/Tulip",
                "  Discord: &dhttps://discord.gg/eT4B65k5E4",
                "  Description: &d" + Tulip.getInstance().getDescription().getDescription(),
                "",
//                "  Database: &aConnected.",
//                "   &8(&7" + ConfigHandler.getInstance().getSettingsConfig().getString("mongo.uri") + " - " + Tulip.getInstance().getMongoService().getMongoDatabase().getName() + "&8)",
//                "",
                "  Kits: &d" + Tulip.getInstance().getKitRepository().getKits().size(),
                "  Arenas: &d" + Tulip.getInstance().getArenaRepository().getArenas().size(),
                "  FFA Arenas: &d" + Tulip.getInstance().getGameRepository().getMatches().size(),
                "",
                "}",
                "",
                "&7&m--------------------------------------------"
        );
        message.forEach(line -> Tulip.getInstance().getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }

    /**
     * Send the shutdown message to the console
     */
    public void sendShutdownMessage() {
        List<String> message = Arrays.asList(
                "",
                "&c&lDisabled Tulip...",
                ""
        );
        message.forEach(line -> Tulip.getInstance().getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }
}