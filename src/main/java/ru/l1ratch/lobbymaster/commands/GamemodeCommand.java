package ru.l1ratch.lobbymaster.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import ru.l1ratch.lobbymaster.managers.CommandManager;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class GamemodeCommand extends CommandManager {

    private final GameMode mode;

    public GamemodeCommand(LobbyMaster plugin, GameMode mode) {
        super(plugin);
        this.mode = mode;
    }

    @Override
    public boolean handleCommand(Player player, Command command, String label, String[] args) {
        if (!player.hasPermission("lm.spcmd")) {
            player.sendMessage("У вас нет прав для выполнения этой команды.");
            return false;
        }

        // Логика смены режима игры
        player.setGameMode(mode);
        player.sendMessage("Ваш режим игры изменен на " + mode.name());
        return true;
    }
}
