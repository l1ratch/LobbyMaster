package ru.l1ratch.lobbymaster;

import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;
import ru.l1ratch.lobbymaster.commands.*;
import ru.l1ratch.lobbymaster.managers.*;

public class LobbyMaster extends JavaPlugin {

    private EffectManager effectManager;
    private PlayerManager playerManager;
    private WorldManager worldManager;
    private ChatManager chatManager;
    private CommandBlocker commandBlocker;
    private SpawnManager spawnManager;
    private NotificationManager notificationManager;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Сохраняем конфигурацию по умолчанию
        effectManager = new EffectManager(this);
        playerManager = new PlayerManager(this);
        worldManager = new WorldManager(this);
        chatManager = new ChatManager(this);
        commandBlocker = new CommandBlocker(this);
        spawnManager = new SpawnManager(this);
        notificationManager = new NotificationManager(this);

        // Регистрация менеджеров команд
        getCommand("lm").setExecutor(new LobbyMasterCommand(this));
        getCommand("gmc").setExecutor(new GamemodeCommand(this, GameMode.CREATIVE));
        getCommand("gms").setExecutor(new GamemodeCommand(this, GameMode.SURVIVAL));
        getCommand("gma").setExecutor(new GamemodeCommand(this, GameMode.ADVENTURE));
        getCommand("gmsp").setExecutor(new GamemodeCommand(this, GameMode.SPECTATOR));
        getCommand("fly").setExecutor(new FlyCommand(this));

        // Регистрируем обработку событий
        getServer().getPluginManager().registerEvents(playerManager, this);
        getServer().getPluginManager().registerEvents(chatManager, this);
        getServer().getPluginManager().registerEvents(commandBlocker, this);
        getServer().getPluginManager().registerEvents(spawnManager, this);
        getServer().getPluginManager().registerEvents(notificationManager, this);

        getLogger().info("LobbyMaster успешно загружен!");
    }

    @Override
    public void onDisable() {
        // Логика отключения плагина, если необходимо
    }
}
