package me.emmy.tulip.profile.command;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @since 11/04/2025
 */
public class BuildModeCommand extends BaseCommand {
    @Command(name = "buildmode", aliases = {"bm"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        Profile profile = this.main.getProfileRepository().getProfile(player.getUniqueId());
        profile.setBuildMode(!profile.isBuildMode());

        player.sendMessage(CC.translate("&aBuild mode " + (profile.isBuildMode() ? "&aenabled" : "&cdisabled") + "."));
    }
}