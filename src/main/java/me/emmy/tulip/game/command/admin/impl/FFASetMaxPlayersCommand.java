package me.emmy.tulip.game.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.game.AbstractGame;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 5/27/2024
 */
public class FFASetMaxPlayersCommand extends BaseCommand {
    @Command(name = "ffa.setmaxplayers", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa setmaxplayers &b<kit> <maxPlayers>"));
            return;
        }

        String kitName = args[0];
        int maxPlayers = Integer.parseInt(args[1]);

        AbstractGame match = Tulip.getInstance().getGameRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate(Locale.FFA_MATCH_DOES_NOT_EXIST.getStringPath()).replace("{kit}", kitName));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        Tulip.getInstance().getGameRepository().saveFFAMatch(match);
        player.sendMessage(CC.translate(Locale.FFA_MAX_PLAYERS_SET.getStringPath()).replace("{kit}", kitName).replace("{maxPlayers}", String.valueOf(maxPlayers)));
    }
}
