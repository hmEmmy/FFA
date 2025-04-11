package me.emmy.tulip.feature.kit.command;

import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 22:13
 */
public class KitCommand extends BaseCommand {
    @Override
    @Command(name = "kit", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&d======================================"));
        player.sendMessage(CC.translate(" &d&lTULIP &7- &d&lKIT CREATION"));
        player.sendMessage(CC.translate("  &e▢ /kit create &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit delete &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit info &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit list"));
        player.sendMessage(CC.translate("  &e▢ /kit setdescription &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit setinv &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit getinv &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit seticon &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /kit toggle &d<name>"));
        player.sendMessage(CC.translate("&d======================================"));
        player.sendMessage("");
    }
}
