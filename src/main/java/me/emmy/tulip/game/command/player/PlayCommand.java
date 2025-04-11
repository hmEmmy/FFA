package me.emmy.tulip.game.command.player;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.game.menu.FFAMenu;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 01/06/2024 - 00:14
 */
public class PlayCommand extends BaseCommand {
    @Override
    @Command(name = "play")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            player.sendMessage(CC.translate(Locale.FFA_ALREADY_IN_MATCH.getStringPath()));
            return;
        }

        new FFAMenu().openMenu(player);
    }
}
