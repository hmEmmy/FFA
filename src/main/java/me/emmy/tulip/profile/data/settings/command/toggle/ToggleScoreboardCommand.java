package me.emmy.tulip.profile.data.settings.command.toggle;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 01/08/2024 - 14:44
 */
public class ToggleScoreboardCommand extends BaseCommand {
    @Override
    @Command(name = "togglescoreboard", aliases = {"tsb"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        FileConfiguration config = ConfigService.getInstance().getLocaleConfig();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.getSettingsData().setShowScoreboard(!profile.getSettingsData().isShowScoreboard());
        player.sendMessage(profile.getSettingsData().isShowScoreboard() ? CC.translate(config.getString("profile-settings.sidebar.enabled")) : CC.translate(config.getString("profile-settings.sidebar.disabled")));
    }
}
