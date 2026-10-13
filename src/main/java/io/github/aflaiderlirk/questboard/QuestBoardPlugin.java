package io.github.aflaiderlirk.questboard;

import org.bukkit.plugin.java.JavaPlugin;
public class QuestBoardPlugin extends JavaPlugin {
    private QuestProgressStorage progressStorage;
    private QuestProgressManager progressManager;
    private QuestSettings questSettings;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        questSettings = new QuestSettings(this);
        int zombieTarget = getConfig().getInt("quests.ZOMBIE_HUNTER.target", 10);
        getLogger().info("Цель квеста ZOMBIE_HUNTER: " + zombieTarget);
        int stoneTarget = getConfig().getInt("quests.STONE_MINER.target", 32);
        getLogger().info("Цель квеста STONE_MINER: " + stoneTarget);
        progressManager = new QuestProgressManager(questSettings);
        progressStorage = new QuestProgressStorage(this);
        progressStorage.load(progressManager);
        getServer().getScheduler().runTaskTimer(
        this,
        () -> progressStorage.save(progressManager),
        20L * 30 * 5,
        20L * 30 * 5
);
        getServer().getPluginManager().registerEvents(
        new QuestKillListener(progressManager, questSettings),
        this
);
        getServer().getPluginManager().registerEvents(
        new QuestMineListener(progressManager, questSettings),
        this
);
        getCommand("quests").setExecutor(
        new QuestCommand(progressManager, questSettings)
);
        getLogger().info("AFLQuestBoard включён!");
        getServer().getPluginManager().registerEvents(new QuestMenuListener(), this);
    }


    @Override
    public void onDisable() {
        if (progressStorage != null && progressManager != null) {
            progressStorage.save(progressManager);
        }
        getLogger().info("AFLQuestBoard выключен!");
    }
    
}
