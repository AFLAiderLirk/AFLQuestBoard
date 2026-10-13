
package io.github.aflaiderlirk.questboard;

import org.bukkit.Material;

public enum QuestType {
    ZOMBIE_HUNTER(10, Material.DIAMOND, 5, "Охотник на зомби"),
    STONE_MINER(32, Material.IRON_INGOT, 8, "Шахтёр"),
    SKELETON_HUNTER(8, Material.GOLD_INGOT, 4, "Охотник на скелетов");

    private final int target;
    private final Material rewardMaterial;
    private final int rewardAmount;
    private final String displayName;

    QuestType(int target, Material rewardMaterial, int rewardAmount, String displayName) {
        this.target = target;
        this.rewardMaterial = rewardMaterial;
        this.rewardAmount = rewardAmount;
        this.displayName = displayName;
    }

    public int getTarget() {
        return target;
    }

    public Material getRewardMaterial() {
        return rewardMaterial;
    }

    public int getRewardAmount() {
        return rewardAmount;
    }

    public String getDisplayName() {
        return displayName;
    }
}
