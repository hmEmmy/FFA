package me.emmy.tulip.profile.data.settings.command;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.data.settings.menu.SettingsMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 01/08/2024 - 14:58
 */
public class SettingsCommand extends BaseCommand {
    @Override
    @Command(name = "ffasettings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new SettingsMenu().openMenu(player);
    }
}
