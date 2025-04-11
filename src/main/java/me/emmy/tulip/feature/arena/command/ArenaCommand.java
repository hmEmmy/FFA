package me.emmy.tulip.feature.arena.command;

import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 19:02
 */
public class ArenaCommand extends BaseCommand {
    @Override
    @Command(name = "arena", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&d======================================"));
        player.sendMessage(CC.translate(" &d&lTULIP &7- &d&lARENA CREATION"));
        player.sendMessage(CC.translate("  &e▢ /arena create &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /arena delete &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /arena info &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /arena list"));
        player.sendMessage(CC.translate("  &e▢ /arena setcenter &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /arena setsafepos &d<name> &d<pos>"));
        player.sendMessage(CC.translate("  &e▢ /arena setspawn &d<name>"));
        player.sendMessage(CC.translate("  &e▢ /arena teleport &d<name>"));
        player.sendMessage(CC.translate("&d======================================"));
        player.sendMessage("");
    }
}