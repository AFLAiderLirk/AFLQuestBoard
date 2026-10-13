package io.github.aflaiderlirk.questboard;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Map;

public class QuestRewardManager {
    private final QuestSettings questSettings;

public QuestRewardManager(QuestSettings questSettings) {
    this.questSettings = questSettings;
}
    public void giveReward(Player player, QuestType questType) {

        ItemStack reward = new ItemStack(
        questSettings.getRewardMaterial(questType),
        questSettings.getRewardAmount(questType)
);
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(reward);

        for (ItemStack item : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }
}