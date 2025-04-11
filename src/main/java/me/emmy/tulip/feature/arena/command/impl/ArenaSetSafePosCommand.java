package me.emmy.tulip.feature.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 12:35
 */
public class ArenaSetSafePosCommand extends BaseCommand {
    @Override
    @Command(name = "arena.setsafepos", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setsafepos &b<name> &b<1/2>"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getArenaRepository().getArena(name) == null) {
            player.sendMessage(CC.translate(Locale.ARENA_DOES_NOT_EXIST.getStringPath()).replace("{arena}", name));
            return;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cPlease enter a valid number!"));
            return;
        }

        if (Integer.parseInt(args[1]) != 1 && Integer.parseInt(args[1]) != 2) {
            player.sendMessage(CC.translate("&cPlease enter a valid number!"));
            return;
        }

        if (args[1].equals("1")) {
            Tulip.getInstance().getArenaRepository().getArena(name).setSafePos1(player.getLocation());
            player.sendMessage(CC.translate(Locale.ARENA_SAFE_POS_SET.getStringPath()).replace("{arena}", name).replace("{pos}", "1"));
            return;
        }

        if (args[1].equals("2")) {
            Tulip.getInstance().getArenaRepository().getArena(name).setSafePos2(player.getLocation());
            player.sendMessage(CC.translate(Locale.ARENA_SAFE_POS_SET.getStringPath()).replace("{arena}", name).replace("{pos}", "2"));
            return;
        }

        Tulip.getInstance().getArenaRepository().saveArena(Tulip.getInstance().getArenaRepository().getArena(name));
    }
}
