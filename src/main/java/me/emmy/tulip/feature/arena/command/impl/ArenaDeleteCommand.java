package me.emmy.tulip.feature.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 19:29
 */
public class ArenaDeleteCommand extends BaseCommand {
    @Override
    @Command(name = "arena.delete", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena delete &b<name>"));
            return;
        }

        String arenaName = args[0];

        if (Tulip.getInstance().getArenaRepository().getArena(arenaName) == null) {
            player.sendMessage(CC.translate(Locale.ARENA_DOES_NOT_EXIST.getStringPath()).replace("{arena}", arenaName));
            return;
        }

        Tulip.getInstance().getArenaRepository().deleteArena(arenaName);
        player.sendMessage(CC.translate(Locale.ARENA_DELETED.getStringPath()).replace("{arena}", arenaName));
    }
}
