package me.emmy.tulip.profile.data.coins.command.impl;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 22/11/2024 - 20:59
 */
public class CoinsRequestCommand extends BaseCommand {
    @Command(name = "coins.request", aliases = {"money.request", "balance.request", "request"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/coins request &b<player> <amount>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (amount <= 0) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        player.sendMessage(CC.translate("&7You have requested &e" + amount + " &7coins from &e" + target.getName() + "&7."));
        target.sendMessage(CC.translate("&e" + player.getName() + " &7has requested &e" + amount + " &7coins from you."));
        target.sendMessage(CC.translate("&7Type &e/coins donate " + player.getName() + " " + amount + " &7to donate the coins, or just ignore this message and be a mean person."));
    }
}
