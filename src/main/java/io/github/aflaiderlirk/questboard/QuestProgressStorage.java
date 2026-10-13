package io.github.aflaiderlirk.questboard;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class QuestProgressStorage {
    private boolean loadSuccessful = false;
    private final JavaPlugin plugin;
    private final File file;
    public QuestProgressStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "progress.yml");
    }
    public void save(QuestProgressManager progressManager) {
        if (!loadSuccessful) {
            plugin.getLogger().severe("Сохранение отменено: прогресс не был успешно загружен!");
            return;
        }
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<UUID, Map<QuestType, Integer>> playerEntry
        : progressManager.getAllQuestProgress().entrySet()) {

    UUID playerId = playerEntry.getKey();

    for (Map.Entry<QuestType, Integer> questEntry
            : playerEntry.getValue().entrySet()) {

        config.set(
                "quest-progress." + playerId + "." + questEntry.getKey().name(),
                questEntry.getValue()
        );
    }
}
for (Map.Entry<UUID, Set<QuestType>> playerEntry
        : progressManager.getAllClaimedRewards().entrySet()) {

    List<String> questNames = new ArrayList<>();

    for (QuestType questType : playerEntry.getValue()) {
        questNames.add(questType.name());
    }

    config.set(
            "claimed-rewards." + playerEntry.getKey(),
            questNames
    );
}
        try {
            Files.createDirectories(file.getParentFile().toPath());
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Не удалось сохранить прогресс квестов!", e);
        }
    }   
    public void load(QuestProgressManager progressManager) {
        loadSuccessful = false;
        if (!file.exists()) {
            loadSuccessful = true;
            return;
        }
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Не удалось загрузить прогресс квестов!", e);
            return;
        }
        ConfigurationSection questsSection =
        config.getConfigurationSection("quest-progress");

        if (questsSection == null && config.contains("quest-progress")) {
            plugin.getLogger().severe("Неверный формат раздела quest-progress!");
            return;
        }
        if (questsSection != null) {
            for (String playerKey : questsSection.getKeys(false)) {
                UUID playerId;

                try {
                    playerId = UUID.fromString(playerKey);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Некорректный UUID в quest-progress: " + playerKey);
                    return;
                }

                ConfigurationSection playerSection =
                        questsSection.getConfigurationSection(playerKey);

                if (playerSection == null) {
                    plugin.getLogger().severe("Неверный формат прогресса игрока: " + playerKey);
                    return;
                }
                for (String questKey : playerSection.getKeys(false)) {
                    QuestType questType;

                    try {
                        questType = QuestType.valueOf(questKey);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Неизвестный тип квеста: " + questKey);
                        return;
                    }

                    if (!playerSection.isInt(questKey)) {
                        plugin.getLogger().severe("Некорректный прогресс квеста: " + questKey);
                        return;
                    }

                    int progress = playerSection.getInt(questKey);

                    progressManager.setProgress(playerId, questType, progress);
                }
            }
        }
        ConfigurationSection claimedSection =
        config.getConfigurationSection("claimed-rewards");

if (claimedSection == null && config.contains("claimed-rewards")) {
    plugin.getLogger().severe("Неверный формат раздела claimed-rewards!");
    return;
}
if (claimedSection != null) {
    for (String playerKey : claimedSection.getKeys(false)) {
        UUID playerId;

        try {
            playerId = UUID.fromString(playerKey);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Некорректный UUID в claimed-rewards: " + playerKey);
            return;
        }

        if (!claimedSection.isList(playerKey)) {
            plugin.getLogger().severe("Неверный список наград игрока: " + playerKey);
            return;
        }

        List<?> questNames = claimedSection.getList(playerKey);

for (Object value : questNames) {
    if (!(value instanceof String)) {
        plugin.getLogger().severe("Некорректное название квеста: " + value);
        return;
    }

    String questKey = (String) value;

    QuestType questType;

    try {
        questType = QuestType.valueOf(questKey);
    } catch (IllegalArgumentException e) {
        plugin.getLogger().warning("Неизвестный квест в claimed-rewards: " + questKey);
        return;
    }

    progressManager.markRewardClaimed(playerId, questType);
}
    }
}
if (claimedSection == null && questsSection != null) {
    for (Map.Entry<UUID, Map<QuestType, Integer>> playerEntry
            : progressManager.getAllQuestProgress().entrySet()) {

        UUID playerId = playerEntry.getKey();

        for (Map.Entry<QuestType, Integer> questEntry
                : playerEntry.getValue().entrySet()) {

            QuestType questType = questEntry.getKey();
            int progress = questEntry.getValue();

            if (progress >= questType.getTarget()) {
                progressManager.markRewardClaimed(playerId, questType);
            }
        }
    }
}
        ConfigurationSection section = config.getConfigurationSection("zombie-kills");

        if (section == null) {
            if (config.contains("zombie-kills")) {
                plugin.getLogger().severe("Неверный формат раздела zombie-kills!");
                return;
            }

            loadSuccessful = true;
            return;
        }
        for (String key : section.getKeys(false)) {
            try {
                UUID playerId = UUID.fromString(key);
                if (!section.isInt(key)) {
                    plugin.getLogger().severe("Некорректное значение прогресса для UUID: " + key);
                    return;
                }
                int kills = section.getInt(key);
                if (questsSection == null || !questsSection.contains(playerId + ".ZOMBIE_HUNTER")) {
                    progressManager.setProgress(playerId, QuestType.ZOMBIE_HUNTER, kills);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Некорректный UUID в progress.yml: " + key);
                return;
            }
        }
        loadSuccessful = true;
    }
    
}
