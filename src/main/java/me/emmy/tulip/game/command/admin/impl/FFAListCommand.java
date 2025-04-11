package me.emmy.tulip.game.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 5/27/2024
 */
public class FFAListCommand extends BaseCommand {
    @Command(name = "ffa.list", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage("");
        player.sendMessage(CC.translate("     &d&lFFA Match List &f(" + Tulip.getInstance().getGameRepository().getMatches().size() + "&f)"));
        if (Tulip.getInstance().getGameRepository().getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Matches available."));
        }
        Tulip.getInstance().getGameRepository().getMatches().forEach(match -> player.sendMessage(CC.translate("      &f● &d" + match.getKit().getName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage("");
    }
}
