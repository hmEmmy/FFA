package me.emmy.tulip.util;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.command.ReloadCommand;
import me.emmy.tulip.command.TulipCommand;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.feature.arena.command.ArenaCommand;
import me.emmy.tulip.feature.arena.command.impl.*;
import me.emmy.tulip.feature.hotbar.command.HotbarItemsCommand;
import me.emmy.tulip.feature.hotbar.command.RemoveHotbarItemsCommand;
import me.emmy.tulip.feature.kit.command.KitCommand;
import me.emmy.tulip.feature.kit.command.impl.*;
import me.emmy.tulip.feature.shop.command.ShopCommand;
import me.emmy.tulip.feature.spawn.command.SetSpawnCommand;
import me.emmy.tulip.feature.spawn.command.SpawnCommand;
import me.emmy.tulip.game.command.admin.FFACommand;
import me.emmy.tulip.game.command.admin.impl.*;
import me.emmy.tulip.game.command.player.JoinCommand;
import me.emmy.tulip.game.command.player.LeaveCommand;
import me.emmy.tulip.game.command.player.PlayCommand;
import me.emmy.tulip.profile.command.BuildModeCommand;
import me.emmy.tulip.profile.data.coins.command.CoinsCommand;
import me.emmy.tulip.profile.data.coins.command.impl.CoinsDonateCommand;
import me.emmy.tulip.profile.data.coins.command.impl.CoinsRequestCommand;
import me.emmy.tulip.profile.data.coins.command.impl.CoinsSetCommand;
import me.emmy.tulip.profile.data.kitlayout.command.KitLayoutCommand;
import me.emmy.tulip.profile.data.settings.command.SettingsCommand;
import me.emmy.tulip.profile.data.settings.command.toggle.ToggleScoreboardCommand;
import me.emmy.tulip.profile.data.settings.command.toggle.ToggleTablistCommand;
import me.emmy.tulip.profile.data.stats.command.StatsCommand;
import me.emmy.tulip.profile.data.stats.command.admin.ResetStatsCommand;
import me.emmy.tulip.profile.data.stats.command.admin.add.AddDeathsCommand;
import me.emmy.tulip.profile.data.stats.command.admin.add.AddKillsCommand;
import me.emmy.tulip.profile.data.stats.command.admin.set.SetDeathsCommand;
import me.emmy.tulip.profile.data.stats.command.admin.set.SetKillsCommand;

/**
 * @author Emmy
 * @project FFA
 * @date 28/09/2024 - 09:05
 */
@UtilityClass
public class CommandUtility {
    /**
     * Register all commands based on their category.
     */
    public void registerCommands() {
        registerArenaCommands();
        registerAdminCommands();
        registerGlobalCommands();
        registerFFACommands();
        registerHotbarCommands();
        registerKitCommands();
        registerProfileCommands();
        registerOtherCommands();
        registerCoinCommands();
    }

    /**
     * Register the arena command and its subcommands.
     */
    private void registerArenaCommands() {
        new ArenaCommand();

        new ArenaCreateCommand();
        new ArenaDeleteCommand();
        new ArenaInfoCommand();
        new ArenaListCommand();
        new ArenaSetCenterCommand();
        new ArenaSetSafePosCommand();
        new ArenaSetSpawnCommand();
        new ArenaTeleportCommand();
    }

    /**
     * Register the admin commands.
     */
    private void registerAdminCommands() {
        new ReloadCommand();
    }

    /**
     * Register the global commands.
     */
    private void registerGlobalCommands() {
        new TulipCommand();
    }

    /**
     * Register the FFA command and its subcommands.
     */
    private void registerFFACommands() {
        //admin
        new FFACommand();

        new FFACreateCommand();
        new FFADeleteCommand();
        new FFAKickCommand();
        new FFAListCommand();
        new FFAListPlayersCommand();
        new FFASetMaxPlayersCommand();

        if (ConfigService.getInstance().getSettingsConfig().getBoolean("commands.ffa-join")) {
            new JoinCommand();
        }
        new LeaveCommand();
        new PlayCommand();
    }

    /**
     * Register the hotbar commands.
     */
    private void registerHotbarCommands() {
        new HotbarItemsCommand();
        new RemoveHotbarItemsCommand();
    }

    /**
     * Register the kit command and its subcommands.
     */
    private void registerKitCommands() {
        new KitCommand();

        new KitCreateCommand();
        new KitDeleteCommand();
        new KitGetInvCommand();
        new KitInfoCommand();
        new KitListCommand();
        new KitSetDescriptionCommand();
        new KitSetIconCommand();
        new KitSetInvCommand();
        new KitToggleCommand();
    }

    /**
     * Register the profile commands.
     */
    private void registerProfileCommands() {
        new BuildModeCommand();
        //admin
        new AddDeathsCommand();
        new AddKillsCommand();
        new SetDeathsCommand();
        new SetKillsCommand();
        new ResetStatsCommand();

        //player
        new KitLayoutCommand();
        new ToggleScoreboardCommand();
        new ToggleTablistCommand();
        new SettingsCommand();
        new StatsCommand();
    }

    /**
     * Register the other commands with no specific category.
     */
    private void registerOtherCommands() {
        new ShopCommand();
        new SpawnCommand();
        new SetSpawnCommand();
    }

    private void registerCoinCommands() {
        new CoinsCommand();
        new CoinsSetCommand();
        new CoinsRequestCommand();
        new CoinsDonateCommand();
    }
}