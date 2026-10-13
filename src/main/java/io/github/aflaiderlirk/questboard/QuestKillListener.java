package io.github.aflaiderlirk.questboard;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

public class QuestKillListener implements Listener {

    private final QuestProgressManager progressManager;
    private final QuestSettings questSettings;
    public QuestKillListener(
        QuestProgressManager progressManager,
        QuestSettings questSettings
) {
    this.progressManager = progressManager;
    this.questSettings = questSettings;
}
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        QuestType questType;

if (event.getEntityType() == EntityType.ZOMBIE) {
    questType = QuestType.ZOMBIE_HUNTER;
} else if (event.getEntityType() == EntityType.SKELETON) {
    questType = QuestType.SKELETON_HUNTER;
} else {
    return;
}
        Player player = event.getEntity().getKiller();
        if (player == null) {
            return;
        }
        if (player.getGameMode() != GameMode.SURVIVAL) {
    return;
}
if (progressManager.hasClaimedReward(
        player.getUniqueId(),
        questType
)) {
    return;
}
        int target = questSettings.getTarget(questType);
        int previous = progressManager.getProgress(
        player.getUniqueId(),
        questType
);
        
        if (previous >= target) {
    return;
}
        int progress = progressManager.incrementProgress(
        player.getUniqueId(),
        questType
);
        player.sendMessage("Прогресс квеста: " + progress + "/" + target);
        if (progress == target) {
            String questName = questType == QuestType.ZOMBIE_HUNTER
        ? "Охотник на зомби"
        : "Охотник на скелетов";

player.sendMessage("§aПоздравляем! Квест «" + questName + "» выполнен!");

            new QuestRewardManager(questSettings).giveReward(
        player,
        questType
);
progressManager.markRewardClaimed(
        player.getUniqueId(),
        questType
);
        }
    }
}
