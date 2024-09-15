package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.l1ratch.lobbymaster.LobbyMaster;

import java.util.List;

public class NotificationManager implements Listener {

    private LobbyMaster plugin;
    private boolean welcomeMessageEnabled;
    private boolean spamMessageEnabled;
    private int spamMessageInterval;
    private int spamMessageIndex = 0;

    public NotificationManager(LobbyMaster plugin) {
        this.plugin = plugin;

        // Инициализация из конфигурации
        welcomeMessageEnabled = plugin.getConfig().getBoolean("notifications.welcome-message.enabled", false);
        spamMessageEnabled = plugin.getConfig().getBoolean("notifications.spam-message.enabled", false);
        spamMessageInterval = plugin.getConfig().getInt("notifications.spam-message.interval", 600); // Интервал в тиках

        // Если спам-оповещение включено, запускаем периодическую задачу
        if (spamMessageEnabled) {
            startSpamMessageTask();
        }
    }

    // Обработчик события входа игрока
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String joinMessage = plugin.getConfig().getString("notifications.join-message");

        if (joinMessage == null) {
            event.setJoinMessage(null); // Скрыть сообщение
        } else if (!"off".equalsIgnoreCase(joinMessage)) {
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', joinMessage.replace("{player}", player.getName())));
        }

        // Приветственное сообщение
        if (welcomeMessageEnabled) {
            sendWelcomeMessage(player);
        }
    }

    // Обработчик события выхода игрока
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = plugin.getConfig().getString("notifications.quit-message");

        if (quitMessage == null) {
            event.setQuitMessage(null); // Скрыть сообщение
        } else if (!"off".equalsIgnoreCase(quitMessage)) {
            event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', quitMessage.replace("{player}", player.getName())));
        }
    }

    // Обработчик события смерти игрока
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String deathMessage = plugin.getConfig().getString("notifications.death-message");

        if (deathMessage == null) {
            event.setDeathMessage(null); // Скрыть сообщение
        } else if (!"off".equalsIgnoreCase(deathMessage)) {
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', deathMessage.replace("{player}", player.getName())));
        }
    }

    // Приветственное сообщение игроку
    private void sendWelcomeMessage(Player player) {
        List<String> welcomeMessages = plugin.getConfig().getStringList("notifications.welcome-message.messages");
        for (String message : welcomeMessages) {
            // Заменяем {player} на имя игрока
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{player}", player.getName())));
        }
    }

    // Периодическое спам-оповещение
    private void startSpamMessageTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> spamMessages = plugin.getConfig().getStringList("notifications.spam-message.messages");
                if (!spamMessages.isEmpty()) {
                    // Выбираем сообщение по циклическому индексу
                    String message = spamMessages.get(spamMessageIndex);

                    // Выводим сообщение всем игрокам
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));

                    // Увеличиваем индекс, сбрасываем в начало, если он больше размера списка
                    spamMessageIndex = (spamMessageIndex + 1) % spamMessages.size();
                }
            }
        }.runTaskTimer(plugin, spamMessageInterval, spamMessageInterval); // Запуск задачи с периодичностью
    }
}
