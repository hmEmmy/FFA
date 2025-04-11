package me.emmy.tulip.game.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 5/27/2024
 */
public class FFACreateCommand extends BaseCommand {
    @Command(name = "ffa.create", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa create &b<arena> <kit> <maxPlayers>"));
            return;
        }

        String arenaName = args[0];
        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate(Locale.ARENA_DOES_NOT_EXIST.getStringPath()).replace("{arena}", arenaName));
            return;
        }

        String kitName = args[1];
        Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_DOES_NOT_EXIST.getStringPath()).replace("{kit}", kitName));
            return;
        }

        int maxPlayers = Integer.parseInt(args[2]);

        if (Tulip.getInstance().getGameRepository().getFFAMatch(kitName) != null) {
            player.sendMessage(CC.translate(Locale.FFA_MATCH_ALREADY_EXISTS.getStringPath()).replace("{kit}", kitName));
            return;
        }

        if (!Tulip.getInstance().getKitRepository().getKit(kitName).isEnabled()) {
            player.sendMessage(CC.translate(Locale.KIT_DISABLED.getStringPath()).replace("{kit}", kitName));
            return;
        }

        Tulip.getInstance().getGameRepository().createFFAMatch(arena, kit, maxPlayers);
        player.sendMessage(CC.translate(Locale.FFA_MATCH_CREATED.getStringPath()).replace("{kit}", kitName));
    }
}
