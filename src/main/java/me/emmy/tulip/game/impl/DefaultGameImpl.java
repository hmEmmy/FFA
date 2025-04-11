package me.emmy.tulip.game.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.feature.cooldown.Cooldown;
import me.emmy.tulip.feature.cooldown.CooldownRepository;
import me.emmy.tulip.feature.hotbar.HotbarUtility;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.game.AbstractGame;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
public class DefaultGameImpl extends AbstractGame {

    /**
     * Constructor for the DefaultGameImpl class
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public DefaultGameImpl(String name, Arena arena, Kit kit, int maxPlayers) {
        super(name, arena, kit, maxPlayers);
    }

    /**
     * Join a player to the FFA match
     *
     * @param player The player
     */
    @Override
    public void join(Player player) {
        if (getPlayers().size() >= getMaxPlayers()) {
            player.sendMessage(CC.translate(ConfigService.getInstance().getLocaleConfig().getString("game.is-full").replace("{max-players}", String.valueOf(getMaxPlayers()))));
            return;
        }

        for (String welcomer : ConfigService.getInstance().getLocaleConfig().getStringList("game.join-message")) {
            player.sendMessage(CC.translate(welcomer)
                    .replace("{kit}", getKit().getName())
                    .replace("{arena}", getArena().getName())
            );
        }

        getPlayers().add(player);
        setupPlayer(player);
    }

    /**
     * Leave a player from the FFA match
     *
     * @param player The player
     */
    @Override
    public void leave(Player player) {
        getPlayers().remove(player);

        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

        player.sendMessage(CC.translate(ConfigService.getInstance().getLocaleConfig().getString("game.left")));

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPAWN);
        profile.setGame(null);

        CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));
        if (optionalCooldown.isPresent()) {
            Cooldown cooldown = optionalCooldown.get();
            cooldown.cancelCooldown();
        }

        profile.getStatsData().resetStreakAndSaveIfPresent(getKit(), player.getUniqueId());

        PlayerUtil.reset(player);
        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        HotbarUtility.applyHotbarItems(player);
        player.getInventory().setHeldItemSlot(0);
    }

    /**
     * Setup a player for the FFA match
     *
     * @param player The player
     */
    @Override
    public void setupPlayer(Player player) {
        if (player.isFlying()) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setGame(this);

        Arena arena = getArena();
        player.getInventory().setHeldItemSlot(0);
        player.teleport(arena.getSpawn());

        Kit kit = getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(profile.getKitLayoutData().getLayout(kit.getName()) == null ? kit.getItems() : profile.getKitLayoutData().getLayout(kit.getName()));
    }

    /**
     * Handle the respawn of a player
     *
     * @param player The player
     */
    public void handleRespawn(Player player) {
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setGame(this);

        Arena arena = getArena();

        Bukkit.getScheduler().runTaskLater(Tulip.getInstance(), () -> {
            player.teleport(arena.getSpawn());

            Kit kit = getKit();
            player.getInventory().clear();
            player.getInventory().setHeldItemSlot(0);
            player.getInventory().setContents(profile.getKitLayoutData().getLayout(kit.getName()) == null ? kit.getItems() : profile.getKitLayoutData().getLayout(kit.getName()));
            player.getInventory().setArmorContents(kit.getArmor());
            player.updateInventory();
        }, 1L);
    }

    /**
     * Handle the death of a player
     *
     * @param player The player
     * @param killer The killer
     */
    @Override
    public void handleDeath(Player player, Player killer) {
        if (killer == null) {
            Profile playerProfile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            playerProfile.getStatsData().incrementKitDeaths(getKit());

            this.getPlayers().forEach(online -> online.sendMessage(CC.translate(ConfigService.getInstance().getLocaleConfig().getString("game.killed.no-killer").replace("{player}", player.getName()))));
            this.handleRespawn(player);
            return;
        }

        CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));
        if (optionalCooldown.isPresent()) {
            Cooldown cooldown = optionalCooldown.get();
            cooldown.cancelCooldown();
        }

        Profile killerProfile = Tulip.getInstance().getProfileRepository().getProfile(killer.getUniqueId());
        killerProfile.getStatsData().incrementKitKills(this.getKit());

        this.giveCoins(player, killer, killerProfile);

        killerProfile.getStatsData().incrementKillstreak(getKit());
        this.broadcastKillStreak(killer);

        Profile playerProfile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        playerProfile.getStatsData().resetStreakAndSaveIfPresent(getKit(), player.getUniqueId());

        this.getPlayers().forEach(online -> online.sendMessage(CC.translate(ConfigService.getInstance().getLocaleConfig().getString("game.killed.with-killer").replace("{player}", player.getName()).replace("{killer}", killer.getName()))));
        this.handleRespawn(player);
    }

    /**
     * Give coins to the killer
     *
     * @param player The player who died
     * @param killer The player who killed the other player
     * @param killerProfile The profile of the killer
     */
    private void giveCoins(Player player, Player killer, Profile killerProfile) {
        int victimCoins = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getCoinsData().getCoins();

        int minReward = 10;
        int maxReward = 100;

        if (victimCoins >= 1000) {
            minReward = 100;
            maxReward = 200;
        } else if (victimCoins >= 500) {
            minReward = 50;
            maxReward = 150;
        } else if (victimCoins >= 250) {
            minReward = 25;
        }

        int reward = ThreadLocalRandom.current().nextInt(minReward, maxReward + 1);
        killerProfile.getCoinsData().addCoins(reward);
        killer.sendMessage(CC.translate("&eYou earned &6" + reward + " coins &efor killing &c" + player.getName() + "&e."));
    }

    /**
     * Alert every five kills
     *
     * @param killer The killer
     */
    private void broadcastKillStreak(Player killer) {
        Profile killerProfile = Tulip.getInstance().getProfileRepository().getProfile(killer.getUniqueId());

        if (killerProfile.getStatsData().getCurrentKillstreak().get(getKit()) % ConfigService.getInstance().getLocaleConfig().getInt("game.killstreak.interval") == 0) {
            for (String message : ConfigService.getInstance().getLocaleConfig().getStringList("game.killstreak.broadcast")) {
                this.getPlayers().forEach(pl -> pl.sendMessage(CC.translate(message).replace("{player}", killer.getName()).replace("{streak}", String.valueOf(killerProfile.getStatsData().getCurrentKillstreak().get(getKit())))));
            }
        }

        //if (KillStreakData.getCurrentStreak(killer) % ConfigHandler.getInstance().getLocaleConfig().getInt("game.killstreak.interval") == 0) {
        //            for (String message : ConfigHandler.getInstance().getLocaleConfig().getStringList("game.killstreak.broadcast")) {
        //                getPlayers().forEach(pl -> pl.sendMessage(CC.translate(message).replace("{player}", killer.getName()).replace("{streak}", String.valueOf(KillStreakData.getCurrentStreak(killer)))));
        //            }
        //        }
    }
}