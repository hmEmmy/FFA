package me.emmy.tulip.game.command.player;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
public class LeaveCommand extends BaseCommand {
    @Command(name = "leaveffa", aliases = "ffa.leave")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getGame() == null) {
            player.sendMessage(CC.translate(Locale.FFA_NOT_IN_MATCH.getStringPath()));
            return;
        }

        profile.getGame().leave(player);
    }
}