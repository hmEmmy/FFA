package me.emmy.tulip.profile.data.coins.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 22/11/2024 - 20:57
 */
public class CoinsDonateCommand extends BaseCommand {
    @Command(name = "coins.donate", aliases = {"money.donate", "balance.donate", "donate"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/coins donate &b<player> <amount>"));
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

        if (amount > Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getCoinsData().getCoins()) {
            player.sendMessage(CC.translate("&cYou do not have enough coins."));
            return;
        }

        Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getCoinsData().removeCoins(amount);
        Tulip.getInstance().getProfileRepository().getProfile(target.getUniqueId()).getCoinsData().addCoins(amount);

        player.sendMessage(CC.translate("&7You have donated &e" + amount + " &7coins to &e" + target.getName() + "&7."));
        target.sendMessage(CC.translate("&7You have been given &e" + amount + " &7coins by &e" + player.getName() + "&7."));
    }
}