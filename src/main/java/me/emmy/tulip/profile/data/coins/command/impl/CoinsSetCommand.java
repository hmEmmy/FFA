package me.emmy.tulip.profile.data.coins.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 22/11/2024 - 20:55
 */
public class CoinsSetCommand extends BaseCommand {
    @Command(name = "coins.set", aliases = {"money.set", "balance.set"}, permission = "tulip.coins.set")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/coins set &b<player> <amount>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
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

        Tulip.getInstance().getProfileRepository().getProfile(target.getUniqueId()).getCoinsData().setCoins(amount);
        target.sendMessage(CC.translate("&7You have been given &e" + amount + " &7coins by &e" + player.getName() + "&7."));
        player.sendMessage(CC.translate("&7You have given &e" + target.getName() + " &7" + amount + " &7coins."));
    }
}
