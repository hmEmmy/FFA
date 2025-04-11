package me.emmy.tulip.feature.spawn.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.game.AbstractGame;
import me.emmy.tulip.feature.hotbar.HotbarUtility;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.PlayerUtil;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 29/07/2024 - 23:09
 */
public class SpawnCommand extends BaseCommand {
    @Override
    @Command(name = "spawn", permission = "tulip.command.spawn")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            AbstractGame ffaMatch = Tulip.getInstance().getGameRepository().getFFAMatch(player);
            if (ffaMatch != null) {
                ffaMatch.leave(player);
                return;
            }
        }

        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        PlayerUtil.reset(player);
        HotbarUtility.applyHotbarItems(player);
        player.sendMessage(CC.translate(Locale.SPAWN_TELEPORTED.getStringPath()));
    }
}
