package io.github.aflaiderlirk.questboard;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class QuestMineListener implements Listener {

    private final QuestProgressManager progressManager;
    private final QuestSettings questSettings;
    public QuestMineListener(
        QuestProgressManager progressManager,
        QuestSettings questSettings
) {
    this.progressManager = progressManager;
    this.questSettings = questSettings;
}
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.STONE) {
            return;
        }

        Player miner = event.getPlayer();
        if (miner.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (progressManager.hasClaimedReward(
        miner.getUniqueId(),
        QuestType.STONE_MINER
)) {
    return;
}
        int previous = progressManager.getProgress(
                miner.getUniqueId(),
                QuestType.STONE_MINER
        );

        int target = questSettings.getTarget(QuestType.STONE_MINER);

        if (previous >= target) {
            return;
        }

        int progress = progressManager.incrementProgress(
                miner.getUniqueId(),
                QuestType.STONE_MINER
        );

        miner.sendMessage("Прогресс квеста: " + progress + "/" + target);

        if (progress == target) {

        miner.sendMessage("§aПоздравляем! Квест «Шахтёр» выполнен!");
new QuestRewardManager(questSettings).giveReward(
        miner,
        QuestType.STONE_MINER
);
progressManager.markRewardClaimed(
        miner.getUniqueId(),
        QuestType.STONE_MINER
);
    }
    }
}