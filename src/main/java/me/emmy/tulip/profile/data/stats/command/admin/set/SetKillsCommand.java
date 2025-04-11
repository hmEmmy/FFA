package me.emmy.tulip.profile.data.stats.command.admin.set;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 30/07/2024 - 22:50
 */
public class SetKillsCommand extends BaseCommand {
    @Override
    @Command(name = "setkills", permission = "tulip.command.setkills", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cUsage: /setkills (player) (value) (kit)"));
            return;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        String kitName = args[2];
        Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        int kills;

        try {
            kills = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (!Tulip.getInstance().getGameRepository().getMatches().stream().anyMatch(match -> match.getKit().equals(kit))) {
            sender.sendMessage(CC.translate("&cKit is not a part of any FFA match."));
            return;
        }

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        profile.getStatsData().setKitKills(kit, kills);
        sender.sendMessage(CC.translate("&aYou have set the kills of " + target.getName() + " to " + kills + "."));
    }
}
