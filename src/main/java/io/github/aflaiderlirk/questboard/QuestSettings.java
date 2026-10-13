
package io.github.aflaiderlirk.questboard;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestSettings {

    private final JavaPlugin plugin;

    public QuestSettings(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int getTarget(QuestType questType) {
        String path = "quests." + questType.name() + ".target";

        int target = plugin.getConfig().getInt(
                path,
                questType.getTarget()
        );

        if (target <= 0) {
            return questType.getTarget();
        }

        return target;
    }

    public int getRewardAmount(QuestType questType) {
        String path = "quests." + questType.name() + ".reward.amount";

        int amount = plugin.getConfig().getInt(
                path,
                questType.getRewardAmount()
        );

        if (amount <= 0) {
            return questType.getRewardAmount();
        }

        return amount;
    }

    public Material getRewardMaterial(QuestType questType) {
        String path = "quests." + questType.name() + ".reward.material";

        String materialName = plugin.getConfig().getString(
                path,
                questType.getRewardMaterial().name()
        );

        if (materialName == null) {
            return questType.getRewardMaterial();
        }

        Material material = Material.matchMaterial(materialName);

        if (material == null || !material.isItem()) {
            return questType.getRewardMaterial();
        }

        return material;
    }
}
