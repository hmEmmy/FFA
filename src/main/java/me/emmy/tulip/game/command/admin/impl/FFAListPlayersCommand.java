package me.emmy.tulip.game.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.game.AbstractGame;
import me.emmy.tulip.locale.Locale;
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
public class FFAListPlayersCommand extends BaseCommand {
    @Command(name = "ffa.listplayers", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa listplayers &b<kit>"));
            return;
        }
        
        String kitName = args[0];
        AbstractGame match = Tulip.getInstance().getGameRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate(Locale.FFA_MATCH_DOES_NOT_EXIST.getStringPath()).replace("{kit}", kitName));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &d&l" + match.getKit().getName() + " Player List &f(" + match.getPlayers().size() + "&f)"));
        if (match.getPlayers().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Players available."));
        }
        match.getPlayers().forEach(participant -> player.sendMessage(CC.translate("      &f● &d" + participant.getName())));
        player.sendMessage("");
    }
}
