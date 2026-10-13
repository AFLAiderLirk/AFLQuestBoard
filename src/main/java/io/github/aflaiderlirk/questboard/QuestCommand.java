
package io.github.aflaiderlirk.questboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {

        private final QuestProgressManager progressManager;
        private final QuestSettings questSettings;

        public QuestCommand(
                QuestProgressManager progressManager,
                QuestSettings questSettings
        ) {
        this.progressManager = progressManager;
        this.questSettings = questSettings;
        }

        @Override
        public boolean onCommand(
                CommandSender sender,
                Command command,
                String label,
                String[] args
        ) {
        if (!(sender instanceof Player player)) {
                sender.sendMessage("§cКоманда доступна только игрокам!");
                return true;
        }

        new QuestMenu(questSettings).openAuto(
                player,
                progressManager
        );

        return true;
        }
}
