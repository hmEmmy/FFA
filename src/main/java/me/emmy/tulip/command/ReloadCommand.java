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
 * @date 28/07/2024 - 23:57
 */
public class ReloadCommand extends BaseCommand {
    @Override
    @Command(name = "tulip.reload", aliases = {"trl"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(CC.translate("&cReloading Tulip..."));
        Tulip.getInstance().getConfigService().reloadConfigs();
        sender.sendMessage(CC.translate("&aTulip has been reloaded."));
    }
}
