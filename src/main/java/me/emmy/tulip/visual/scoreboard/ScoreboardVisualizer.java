package me.emmy.tulip.visual.scoreboard;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.BukkitReflection;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.assemble.AssembleAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 17:42
 */
public class ScoreboardVisualizer implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return CC.translate(ConfigService.getInstance().getScoreboardConfig().getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> list = new ArrayList<>();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if (profile == null) {
            for (String line : ConfigService.getInstance().getScoreboardConfig().getStringList("scoreboard.failed")) {
                list.add(CC.translate(line).replace("{sidebar}", "&7&m------------------"));
            }
            return list;
        }

        if (profile.getSettingsData().isShowScoreboard()) {
            if (profile.getState() == EnumProfileState.FFA) {
                for (String line : ConfigService.getInstance().getScoreboardConfig().getStringList("scoreboard.lines.in-game")) {
                    list.add(CC.translate(line)
                            .replace("{sidebar}", "&7&m------------------")
                            .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replace("{kit}", String.valueOf(Tulip.getInstance().getGameRepository().getFFAMatch(player).getKit().getName()))
                            .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                            .replace("{players}", String.valueOf(profile.getGame().getPlayers().size()))
                            .replace("{ks}", String.valueOf(profile.getStatsData().getCurrentKillstreak().get(profile.getGame().getKit())))
                            .replace("{kills}", String.valueOf(profile.getStatsData().getKitKills(profile.getGame().getKit())))
                            .replace("{deaths}", String.valueOf(profile.getStatsData().getKitDeaths(profile.getGame().getKit())))
                            .replace("{max-online}", String.valueOf(Bukkit.getMaxPlayers()))
                    );
                }
            } else {
                for (String line : ConfigService.getInstance().getScoreboardConfig().getStringList("scoreboard.lines.spawn")) {
                    list.add(CC.translate(line)
                            .replace("{sidebar}", "&7&m------------------")
                            .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replace("{kills}", String.valueOf(profile.getStatsData().getTotalKills()))
                            .replace("{deaths}", String.valueOf(profile.getStatsData().getTotalDeaths()))
                            .replace("{KDR}", decimalFormat.format(profile.getStatsData().getKDR()))
                            .replace("{max-online}", String.valueOf(Bukkit.getMaxPlayers()))
                    );
                }
            }
        }
        return list;
    }
}
