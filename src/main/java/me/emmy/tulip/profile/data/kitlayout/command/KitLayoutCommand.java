package me.emmy.tulip.profile.data.kitlayout.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.profile.data.kitlayout.menu.KitLayoutSelectionMenu;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 10/08/2024 - 18:56
 */
public class KitLayoutCommand extends BaseCommand {

    @Override
    @Command(name = "kitlayout", aliases = {"kiteditor"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.SPAWN) {
            player.sendMessage(CC.translate("&cYou can only edit your Kit Layout in spawn."));
            return;
        }

        new KitLayoutSelectionMenu().openMenu(player);
    }
}
