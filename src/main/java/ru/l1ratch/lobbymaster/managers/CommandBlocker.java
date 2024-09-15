package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.l1ratch.lobbymaster.LobbyMaster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandBlocker implements Listener {

    private LobbyMaster plugin;
    private List<String> commandList;
    private String mode;
    private static final String BYPASS_PERMISSION = "lm.command.bypass"; // Постоянное право обхода

    // Используем множество для отслеживания уже отправленных сообщений
    private Set<String> sentMessages = new HashSet<>();

    public CommandBlocker(LobbyMaster plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); // Регистрация событий

        // Загрузка настроек из конфигурации
        this.mode = plugin.getConfig().getString("command-blocker.mode", "off");
        this.commandList = plugin.getConfig().getStringList("command-blocker.commands");
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1); // Получаем команду без "/"
        String playerName = event.getPlayer().getName();

        // Проверяем, имеет ли игрок права на обход
        if (!event.getPlayer().hasPermission(BYPASS_PERMISSION)) {
            if ("black".equalsIgnoreCase(mode)) {
                // Блокируем команды из черного списка
                if (commandList.contains(command)) {
                    sendBlockMessage(event.getPlayer(), "Выполнение этой команды заблокировано.");
                    event.setCancelled(true); // Блокировка команды
                }
            } else if ("white".equalsIgnoreCase(mode)) {
                // Блокируем все команды, кроме белого списка
                if (!commandList.contains(command)) {
                    sendBlockMessage(event.getPlayer(), "Выполнение этой команды заблокировано.");
                    event.setCancelled(true); // Блокировка команды
                }
            }
            // Если mode равен "off", блокировка команд полностью отключена
        }
    }

    // Метод для отправки сообщения и предотвращения дублирования
    private void sendBlockMessage(org.bukkit.entity.Player player, String message) {
        String messageKey = player.getName() + message;
        if (!sentMessages.contains(messageKey)) {
            player.sendMessage(message);
            sentMessages.add(messageKey);
        }
    }
}
