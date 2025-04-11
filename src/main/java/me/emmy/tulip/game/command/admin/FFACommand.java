package me.emmy.tulip.game.command.admin;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project FFA
 * @date 07/09/2024 - 10:56
 */
public class FFACommand extends BaseCommand {
    @Command(name = "ffa", permission = "Tulip.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&d======================================"));
        sender.sendMessage(CC.translate(" &d&lTULIP &7- &d&lFFA CREATION"));
        sender.sendMessage(CC.translate("  &e▢ /ffa create &d<arena> <kit> <maxPlayers>"));
        sender.sendMessage(CC.translate("  &e▢ /ffa delete &d<name>"));
        sender.sendMessage(CC.translate("  &e▢ /ffa kick &d<player>"));
        sender.sendMessage(CC.translate("  &e▢ /ffa list"));
        sender.sendMessage(CC.translate("  &e▢ /ffa listplayers &d<kit>"));
        sender.sendMessage(CC.translate("  &e▢ /ffa setmaxplayers &d<name> <maxPlayers>"));
        sender.sendMessage(CC.translate("&d======================================"));
        sender.sendMessage("");
    }
}
