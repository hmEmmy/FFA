package me.emmy.tulip.profile.stats.command.admin.set;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 30/07/2024 - 22:52
 */
public class SetDeathsCommand extends BaseCommand {
    @Override
    @Command(name = "setdeaths", permission = "tulip.command.setdeaths", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cUsage: /setdeaths (player) (value) (kit)"));
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

        int deaths;

        try {
            deaths = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (!Tulip.getInstance().getFfaRepository().getMatches().stream().anyMatch(match -> match.getKit().equals(kit))) {
            sender.sendMessage(CC.translate("&cKit is not a part of any FFA match."));
            return;
        }

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        profile.getStats().setKitDeaths(kit, deaths);
        sender.sendMessage(CC.translate("&aYou have set the deaths of " + target.getName() + " to " + deaths + "."));
    }
}
