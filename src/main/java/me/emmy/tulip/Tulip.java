package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.api.assemble.Assemble;
import me.emmy.tulip.api.assemble.AssembleStyle;
import me.emmy.tulip.api.command.CommandFramework;
import me.emmy.tulip.feature.arena.ArenaRepository;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.feature.cooldown.CooldownRepository;
import me.emmy.tulip.database.MongoService;
import me.emmy.tulip.game.GameRepository;
import me.emmy.tulip.game.spawn.FFASpawnHandler;
import me.emmy.tulip.game.spawn.task.FFASpawnTask;
import me.emmy.tulip.feature.kit.KitRepository;
import me.emmy.tulip.feature.product.ProductRepository;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.feature.spawn.SpawnHandler;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.CommandUtility;
import me.emmy.tulip.util.PluginUtil;
import me.emmy.tulip.util.ServerUtil;
import me.emmy.tulip.visual.scoreboard.ScoreboardVisualizer;
import me.emmy.tulip.visual.tablist.task.TablistUpdateTask;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 21:12
 */
@Getter
@Setter
public class Tulip extends JavaPlugin {

    @Getter
    private static Tulip instance;

    private ConfigService configService;
    private CommandFramework commandFramework;
    private SpawnHandler spawnHandler;
    private ArenaRepository arenaRepository;
    private KitRepository kitRepository;
    private MongoService mongoService;
    private ProfileRepository profileRepository;
    private CooldownRepository cooldownRepository;
    private GameRepository gameRepository;
    private FFASpawnHandler ffaSpawnHandler;
    private ProductRepository productRepository;

    @Override
    public void onEnable() {
        instance = this;

        configService = new ConfigService();

        commandFramework = new CommandFramework();
        //commandFramework.registerCommandsInPackage("me.emmy.tulip");
        CommandUtility.registerCommands();

        spawnHandler = new SpawnHandler();
        spawnHandler.loadSpawn();

        arenaRepository = new ArenaRepository();
        arenaRepository.loadArenas();

        kitRepository = new KitRepository();
        kitRepository.loadKits();

        gameRepository = new GameRepository();
        gameRepository.loadFFAMatches();

        ffaSpawnHandler = new FFASpawnHandler();
        ffaSpawnHandler.loadFFASpawn();

        mongoService = new MongoService();
        mongoService.startMongo();

        profileRepository = new ProfileRepository();
        profileRepository.initializeEveryProfile();

        cooldownRepository = new CooldownRepository();

        productRepository = new ProductRepository();
        productRepository.loadProducts();

        PluginUtil.registerListenersInPackage(
                "me.emmy.tulip",
                "me.emmy.tulip.util", "me.emmy.tulip.api.assemble"
        );

        ServerUtil.setupWorld();

        runTasks();
        loadScoreboard();
        CC.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        this.profileRepository.getProfiles().forEach((uuid, profile) -> profile.saveProfile());

        ServerUtil.disconnectPlayers();

        this.arenaRepository.saveArenas();
        this.kitRepository.saveKits();
        this.gameRepository.saveFFAMatches();

        ServerUtil.stopTasks();

        CC.sendShutdownMessage();
    }

    /**
     * Load the scoreboard
     */
    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    /**
     * Run the tasks
     */
    private void runTasks() {
        new FFASpawnTask(this.ffaSpawnHandler.getCuboid(), this).runTaskTimer(this, 0, 20);
        if (configService.getTablistConfig().getBoolean("tablist.enabled")) {
            new TablistUpdateTask().runTaskTimer(this, 0L, 20L);
        }
    }
}