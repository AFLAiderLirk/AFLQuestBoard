package io.github.aflaiderlirk.questboard;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class QuestProgressManager {
    private final Map<UUID, Map<QuestType, Integer>> questProgress = new HashMap<>();
    private final Map<UUID, Set<QuestType>> claimedRewards = new HashMap<>();
    private final QuestSettings questSettings;

public QuestProgressManager(QuestSettings questSettings) {
    this.questSettings = questSettings;
}
    public Map<UUID, Map<QuestType, Integer>> getAllQuestProgress() {
        Map<UUID, Map<QuestType, Integer>> snapshot = new HashMap<>();

        for (Map.Entry<UUID, Map<QuestType, Integer>> entry : questProgress.entrySet()) {
            snapshot.put(entry.getKey(), Map.copyOf(entry.getValue()));
        }

        return Map.copyOf(snapshot);
    }
    public int getProgress(UUID playerId, QuestType questType) {
        Map<QuestType, Integer> playerProgress = questProgress.get(playerId);

        if (playerProgress == null) {
            return 0;
        }

        return playerProgress.getOrDefault(questType, 0);
    }
    public boolean hasClaimedReward(UUID playerId, QuestType questType) {
    Set<QuestType> claimed = claimedRewards.get(playerId);

    return claimed != null && claimed.contains(questType);
}
public void markRewardClaimed(UUID playerId, QuestType questType) {
    Set<QuestType> claimed = claimedRewards.computeIfAbsent(
            playerId,
            id -> new HashSet<>()
    );

    claimed.add(questType);
}
public Map<UUID, Set<QuestType>> getAllClaimedRewards() {
    Map<UUID, Set<QuestType>> snapshot = new HashMap<>();

    for (Map.Entry<UUID, Set<QuestType>> entry : claimedRewards.entrySet()) {
        snapshot.put(entry.getKey(), Set.copyOf(entry.getValue()));
    }

    return Map.copyOf(snapshot);
}
    public int incrementProgress(UUID playerId, QuestType questType) {
        Map<QuestType, Integer> playerProgress = questProgress.computeIfAbsent(playerId, id -> new HashMap<>());
        int current = playerProgress.getOrDefault(questType, 0);
        int target = questSettings.getTarget(questType);
int updated = Math.min(current + 1, target);
        playerProgress.put(questType, updated);
        return updated;
    }
    public void setProgress(UUID playerId, QuestType questType, int value) {
    Map<QuestType, Integer> playerProgress =
            questProgress.computeIfAbsent(playerId, id -> new HashMap<>());

        int target = questSettings.getTarget(questType);
int safeValue = Math.max(0, Math.min(value, target));

        playerProgress.put(questType, safeValue);
    }
}
