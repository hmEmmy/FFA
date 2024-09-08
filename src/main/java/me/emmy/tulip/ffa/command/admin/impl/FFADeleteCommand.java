package me.emmy.tulip.ffa.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 5/27/2024
 */
public class FFADeleteCommand extends BaseCommand {
    @Command(name = "ffa.delete", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa delete &b<name>"));
            return;
        }

        String kitName = args[0];
        AbstractFFAMatch match = Tulip.getInstance().getFfaRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate(Locale.FFA_MATCH_DOES_NOT_EXIST.getStringPath()).replace("{kit}", kitName));
            return;
        }

        Tulip.getInstance().getFfaRepository().deleteFFAMatch(match);
        player.sendMessage(CC.translate(Locale.FFA_MATCH_DELETED.getStringPath()).replace("{kit}", kitName));
    }
}
