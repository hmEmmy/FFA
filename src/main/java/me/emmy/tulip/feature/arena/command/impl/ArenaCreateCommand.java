package me.emmy.tulip.feature.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 19:04
 */
public class ArenaCreateCommand extends BaseCommand {
    @Override
    @Command(name = "arena.create", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena create &b<name>"));
            return;
        }

        String arenaName = args[0];

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena != null) {
            player.sendMessage(CC.translate(Locale.ARENA_ALREADY_EXISTS.getStringPath()).replace("{arena}", arenaName));
            return;
        }

        Tulip.getInstance().getArenaRepository().createArena(arenaName, String.valueOf(player.getLocation()), null, null, null);
        player.sendMessage(CC.translate(Locale.ARENA_CREATED.getStringPath()).replace("{arena}", arenaName));
    }
}
