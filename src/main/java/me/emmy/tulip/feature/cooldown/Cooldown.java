package me.emmy.tulip.feature.cooldown;

import lombok.Getter;
import me.emmy.tulip.util.TaskUtil;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
@Getter
public class Cooldown {
    private final long cooldownDuration;
    private final Runnable actionToRun;
    private long startTime;
    private BukkitTask cooldownTask;

    /**
     * Instantiates a new Cooldown.
     *
     * @param cooldownDuration the cooldown duration
     * @param actionToRun      the action to run
     */
    public Cooldown(long cooldownDuration, Runnable actionToRun) {
        this.cooldownDuration = cooldownDuration;
        this.actionToRun = actionToRun;
        this.startTime = 0L;
    }

    public long calculateEndTime() {
        return startTime + cooldownDuration;
    }

    public boolean isActive() {
        return calculateEndTime() > System.currentTimeMillis() && cooldownTask != null;
    }

    public Cooldown resetCooldown() {
        startTime = System.currentTimeMillis();
        cancelExistingTask();
        startNewCooldownTask();
        return this;
    }

    public void cancelCooldown() {
        startTime = 0L;
        cancelExistingTask();
    }

    public int remainingTime() {
        return (int) ((calculateEndTime() - System.currentTimeMillis()) / 1000);
    }

    private void cancelExistingTask() {
        if (cooldownTask != null) {
            cooldownTask.cancel();
            cooldownTask = null;
        }
    }

    private void startNewCooldownTask() {
        cooldownTask = TaskUtil.runLaterAsync(() -> {
            actionToRun.run();
            cancelExistingTask();
        }, cooldownDuration / 50L);
    }
}