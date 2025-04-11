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
 * @date 07/09/2024 - 16:08
 */
public class ToggleTablistCommand extends BaseCommand {
    @Command(name = "toggletablist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        FileConfiguration config = ConfigService.getInstance().getLocaleConfig();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.getSettingsData().setShowTablist(!profile.getSettingsData().isShowTablist());
        player.sendMessage(profile.getSettingsData().isShowTablist() ? CC.translate(config.getString("profile-settings.tablist.enabled")) : CC.translate(config.getString("profile-settings.tablist.disabled")));

    }
}
