package ru.l1ratch.lobbymaster.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import ru.l1ratch.lobbymaster.managers.CommandManager;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class FlyCommand extends CommandManager {

    public FlyCommand(LobbyMaster plugin) {
        super(plugin);
    }

    @Override
    public boolean handleCommand(Player player, Command command, String label, String[] args) {
        if (!player.hasPermission("lm.spcmd")) {
            player.sendMessage("У вас нет прав для выполнения этой команды.");
            return false;
        }
        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage("Режим полета " + (player.getAllowFlight() ? "включен" : "выключен") + ".");
        return true;
    }
}
