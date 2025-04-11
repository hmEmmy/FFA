package me.emmy.tulip.feature.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 10:37
 */
public class KitListCommand extends BaseCommand {
    @Override
    @Command(name = "kit.list", permission = "Tulip.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&dKit List &7(&d" + Tulip.getInstance().getKitRepository().getKits().size() + "&7)"));
        if (Tulip.getInstance().getKitRepository().getKits().isEmpty()) {
            sender.sendMessage(CC.translate("&7There are no kits available."));
        }
        Tulip.getInstance().getKitRepository().getKits().forEach(arena -> sender.sendMessage(CC.translate(" &7- " + arena.getName())));
        sender.sendMessage("");
    }
}
