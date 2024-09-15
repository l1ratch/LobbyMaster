package ru.l1ratch.lobbymaster.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommandManager implements CommandExecutor, TabCompleter {

    protected JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команды могут использовать только игроки!");
            return false;
        }
        Player player = (Player) sender;
        return handleCommand(player, command, label, args);
    }

    // Абстрактный метод для обработки команд, реализуется в наследниках
    public abstract boolean handleCommand(Player player, Command command, String label, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return handleTabComplete(sender, command, alias, args);
    }

    // Метод для обработки автозаполнения
    public List<String> handleTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
