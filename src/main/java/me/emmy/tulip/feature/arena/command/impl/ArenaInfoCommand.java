package me.emmy.tulip.feature.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 20:35
 */
public class ArenaInfoCommand extends BaseCommand {
    @Override
    @Command(name = "arena.info", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena info &b<name>"));
            return;
        }

        String arenaName = args[0];

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);

        if (arena == null) {
            player.sendMessage(CC.translate(Locale.ARENA_DOES_NOT_EXIST.getStringPath()).replace("{arena}", arenaName));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&d&lArena Information"));
        player.sendMessage(CC.translate(" &7- &dArena Name: &7"+ arena.getName()));
        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate(" &7- &dCenter: &cNot set"));
        } else {
            player.sendMessage(CC.translate(" &7- &dCenter: &7" + arena.getCenter().getWorld().getName() + "&d:"));
            player.sendMessage(CC.translate("   &7* &dX: &7" + arena.getCenter().getBlockX()));
            player.sendMessage(CC.translate("   &7* &dY: &7" + arena.getCenter().getBlockY()));
            player.sendMessage(CC.translate("   &7* &dZ: &7" + arena.getCenter().getBlockZ()));
            if (ConfigService.getInstance().getLocaleConfig().getBoolean("arena.extend-info-command")) {
                player.sendMessage(CC.translate("   &7* &dPitch: &7" + arena.getCenter().getPitch()));
                player.sendMessage(CC.translate("   &7* &dYaw: &7" + arena.getCenter().getYaw()));
            }
        }

        if (arena.getSpawn() == null) {
            player.sendMessage(CC.translate(" &7- &dSpawn: &cNot set"));
        } else {
            player.sendMessage(CC.translate(" &7- &dSpawn: &7" + arena.getSpawn().getWorld().getName() + "&d:"));
            player.sendMessage(CC.translate("   &7* &dX: &7" + arena.getSpawn().getBlockX()));
            player.sendMessage(CC.translate("   &7* &dY: &7" + arena.getSpawn().getBlockY()));
            player.sendMessage(CC.translate("   &7* &dZ: &7" + arena.getSpawn().getBlockZ()));
            if (ConfigService.getInstance().getLocaleConfig().getBoolean("arena.extend-info-command")) {
                player.sendMessage(CC.translate("   &7* &dPitch: &7" + arena.getSpawn().getPitch()));
                player.sendMessage(CC.translate("   &7* &dYaw: &7" + arena.getSpawn().getYaw()));
            }
        }
        player.sendMessage("");
    }
}
