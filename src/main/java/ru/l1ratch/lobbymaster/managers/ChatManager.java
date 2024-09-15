package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class ChatManager implements Listener {

    private LobbyMaster plugin;
    private static final String BYPASS_PERMISSION = "lm.chat.bypass"; // Постоянное право обхода

    public ChatManager(LobbyMaster plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); // Регистрация событий
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Проверяем, включена ли функция блокировки чата
        if (plugin.getConfig().getBoolean("chat.enabled", false)) {
            // Проверяем, имеет ли игрок права на обход
            if (!event.getPlayer().hasPermission(BYPASS_PERMISSION)) {
                // Отправляем сообщение только один раз
                if (!event.isCancelled()) {
                    event.getPlayer().sendMessage("Чат временно заблокирован.");
                    event.setCancelled(true); // Блокировка сообщения
                }
            }
        }
    }
}
