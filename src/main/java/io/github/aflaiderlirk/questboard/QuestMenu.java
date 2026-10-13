
package io.github.aflaiderlirk.questboard;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;

public class QuestMenu {

    private final QuestSettings questSettings;

    public QuestMenu(QuestSettings questSettings) {
        this.questSettings = questSettings;
    }

    public void openAuto(
            Player player,
            QuestProgressManager progressManager
    ) {

        QuestMenuHolder holder = new QuestMenuHolder();

Inventory menu = Bukkit.createInventory(
        holder,
        27,
        "Доска заданий"
);

holder.setInventory(menu);

        QuestType[] quests = QuestType.values();

        int count = Math.min(quests.length, 7);

        int[] slots;

        if (count <= 3) {
            slots = new int[]{11, 13, 15};
        } else {
            slots = new int[]{10, 11, 12, 13, 14, 15, 16};
        }

        for (int i = 0; i < count; i++) {

            QuestType questType = quests[i];

            int progress = progressManager.getProgress(
                    player.getUniqueId(),
                    questType
            );

            boolean claimed = progressManager.hasClaimedReward(
                    player.getUniqueId(),
                    questType
            );

            ItemStack questItem = createQuestItem(
                    questType,
                    progress,
                    claimed
            );

            menu.setItem(slots[i], questItem);
        }

        player.openInventory(menu);
    }

    private ItemStack createQuestItem(
            QuestType questType,
            int progress,
            boolean claimed
    ) {

        int target = questSettings.getTarget(questType);

        Material icon = switch (questType) {
            case ZOMBIE_HUNTER -> Material.IRON_SWORD;
            case STONE_MINER -> Material.IRON_PICKAXE;
            case SKELETON_HUNTER -> Material.BOW;
        };

        ItemStack item = new ItemStack(icon);

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item;
        }

        String questName = questType.getDisplayName();

        boolean completed = claimed || progress >= target;

        if (completed) {
            meta.setDisplayName(
                    "§a✔ " + questName + " [ВЫПОЛНЕНО]"
            );
        } else {
            meta.setDisplayName("§e" + questName);
        }

        String description = switch (questType) {
            case ZOMBIE_HUNTER ->
                    "Убей " + target + " зомби";

            case STONE_MINER ->
                    "Добудь " + target + " блоков камня";

            case SKELETON_HUNTER ->
                    "Убей " + target + " скелетов";
        };

        int rewardAmount = questSettings.getRewardAmount(
                questType
        );

        Material rewardMaterial = questSettings.getRewardMaterial(
                questType
        );

        meta.setLore(List.of(
                "§7" + description,
                "",
                "§7Прогресс: §f" + progress + "/" + target,
                "",
                "§eНаграда: " + rewardAmount + " × "
                        + getRewardName(rewardMaterial),
                "",
                completed
                        ? "§aСтатус: ВЫПОЛНЕНО"
                        : "§eСтатус: В ПРОЦЕССЕ"
        ));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

    // Русские названия предметов-наград
    private String getRewardName(Material material) {

        return switch (material) {
            case DIAMOND -> "алмаз";
            case EMERALD -> "изумруд";
            case IRON_INGOT -> "железный слиток";
            case GOLD_INGOT -> "золотой слиток";
            case NETHERITE_INGOT -> "незеритовый слиток";

            default -> material.name()
                    .toLowerCase(Locale.ROOT)
                    .replace('_', ' ');
        };
    }
}
