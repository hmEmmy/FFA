package me.emmy.tulip.feature.hotbar.command;

import me.emmy.tulip.feature.hotbar.HotbarUtility;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 28/07/2024 - 21:08
 */
public class HotbarItemsCommand extends BaseCommand {
    @Override
    @Command(name = "hotbaritems", aliases = {"spawnitems"}, permission = "tulip.command.hotbaritems")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            HotbarUtility.applyHotbarItems(player);
            player.sendMessage(CC.translate("&aYou have added all hotbar items to your inventory."));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        HotbarUtility.applyHotbarItems(targetPlayer);
        player.sendMessage(CC.translate("&aYou have added all hotbar items to &d" + targetPlayer.getName() + "'s &ainventory."));
        targetPlayer.sendMessage(CC.translate("&aYour hotbar items have been added to your inventory."));
    }
}
