package ru.l1ratch.lobbymaster.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import ru.l1ratch.lobbymaster.managers.CommandManager;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class LobbyMasterCommand extends CommandManager {

    public LobbyMasterCommand(LobbyMaster plugin) {
        super(plugin);
    }

    @Override
    public boolean handleCommand(Player player, Command command, String label, String[] args) {
        if (!player.hasPermission("lm.admin")) {
            player.sendMessage("У вас нет прав для выполнения этой команды.");
            return false;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("setspawn")) {
            Location loc = player.getLocation();
            plugin.getConfig().set("spawn.world", loc.getWorld().getName());
            plugin.getConfig().set("spawn.x", loc.getX());
            plugin.getConfig().set("spawn.y", loc.getY());
            plugin.getConfig().set("spawn.z", loc.getZ());
            plugin.saveConfig();
            player.sendMessage("Точка спавна установлена.");
            return true;
        }

        player.sendMessage("Использование: /lm setspawn");
        return true;
    }
}
