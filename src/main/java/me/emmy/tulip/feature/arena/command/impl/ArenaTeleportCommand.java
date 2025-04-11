package me.emmy.tulip.feature.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 20:35
 */
public class ArenaTeleportCommand extends BaseCommand {
    @Override
    @Command(name = "arena.teleport", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena teleport &b<name>"));
            return;
        }

        String arenaName = args[0];

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate(Locale.ARENA_DOES_NOT_EXIST.getStringPath()).replace("{arena}", arenaName));
            return;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate(Locale.ARENA_CENTER_NOT_SET.getStringPath()).replace("{arena}", arenaName));
            return;
        }

        player.teleport(Tulip.getInstance().getArenaRepository().getArena(arenaName).getCenter());
        player.sendMessage(CC.translate("&aTeleported to the center of arena " + arenaName + "!"));
    }
}
