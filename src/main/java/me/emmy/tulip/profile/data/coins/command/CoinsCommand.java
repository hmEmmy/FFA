package me.emmy.tulip.profile.data.coins.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 22/11/2024 - 20:52
 */
public class CoinsCommand extends BaseCommand {
    @Command(name = "coins", aliases = {"money", "balance"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&d======================================"));
        player.sendMessage(CC.translate(" &d&lTULIP &7- &d&lCOIN COMMANDS"));
        player.sendMessage(CC.translate("  &e▢ /coins &7- &dSends this message."));
        player.sendMessage(CC.translate("  &e▢ /coins donate &d<player> <amount> &7- &dDonate coins to another player."));
        player.sendMessage(CC.translate("  &e▢ /coins set &d<player> <amount> &7- &dSet a player's coins."));
        player.sendMessage(CC.translate("  &e▢ /coins request &d<player> <amount> &7- &dRequest coins from another player."));
        //player.sendMessage(CC.translate("  &e▢ /coins top &7- &dView the top 10 players with the most coins."));
        player.sendMessage(CC.translate("&d======================================"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&7You currently have &e" + Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getCoinsData().getCoins() + " &7coins."));
        player.sendMessage(CC.translate("&7You can earn more coins by playing games or purchasing coin packs in our store."));
    }
}