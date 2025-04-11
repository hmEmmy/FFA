package me.emmy.tulip.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 17:55
 */
public class TulipCommand extends BaseCommand {
    @Command(name = "tulip", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&d======================================"));
        sender.sendMessage(CC.translate(" &d&lTULIP FFA CORE"));
        sender.sendMessage(CC.translate("  &e▢ Author: &d" + Tulip.getInstance().getDescription().getAuthors().get(0)));
        sender.sendMessage(CC.translate("  &e▢ Version: &d" + Tulip.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate("  &e▢ Github: &dhttps://github.com/hmEmmy/Tulip"));
        sender.sendMessage(CC.translate("  &e▢ Discord: &dhttps://discord.gg/eT4B65k5E4"));
        sender.sendMessage(CC.translate("&d======================================"));
        sender.sendMessage("");
    }
}
