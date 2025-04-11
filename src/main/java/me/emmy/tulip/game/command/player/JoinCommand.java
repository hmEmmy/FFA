package me.emmy.tulip.game.command.player;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.game.GameRepository;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

public class JoinCommand extends BaseCommand {

    @Command(name = "ffa.join")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa join &b<kit>"));
            return;
        }

        String kitName = args[0];
        Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            player.sendMessage("Kit not found.");
            return;
        }

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            player.sendMessage(CC.translate("&cYou are already in a match."));
            return;
        }

        GameRepository gameRepository = Tulip.getInstance().getGameRepository();
        gameRepository.getMatches().stream()
                .filter(match -> match.getKit().equals(kit))
                .filter(match -> match.getPlayers().size() < match.getMaxPlayers())
                .findFirst()
                .ifPresent(match -> match.join(player));

    }
}