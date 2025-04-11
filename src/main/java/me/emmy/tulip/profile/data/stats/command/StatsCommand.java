package me.emmy.tulip.profile.data.stats.command;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.data.stats.menu.StatsMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 31/07/2024 - 13:57
 */
public class StatsCommand extends BaseCommand {
    @Override
    @Command(name = "stats", permission = "tulip.command.stats")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            new StatsMenu(player).openMenu(player);
            return;
        }

        String playerName = args[0];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerName);

        new StatsMenu(targetPlayer).openMenu(player);
    }
}
