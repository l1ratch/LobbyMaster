package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class WorldManager {

    private LobbyMaster plugin;

    public WorldManager(LobbyMaster plugin) {
        this.plugin = plugin;
        // Запланировать выполнение настройки мира на следующем тикере
        new BukkitRunnable() {
            @Override
            public void run() {
                handleWorldSetup();
            }
        }.runTask(plugin);
    }

    private void handleWorldSetup() {
        // Получаем имя мира из конфигурации
        String worldName = plugin.getConfig().getString("world.name", "world");
        setupWorld(worldName);
    }

    private void setupWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            plugin.getLogger().warning("Мир с именем '" + worldName + "' не найден.");
            return;
        }

        // Заморозка времени
        int freezeTime = plugin.getConfig().getInt("world.freeze-time", -1);
        if (freezeTime != -1) {
            world.setTime(freezeTime);
            // Принудительно блокируем изменение времени
            world.setGameRuleValue("doDaylightCycle", "false");
        }

        // Заморозка погоды
        String freezeWeather = plugin.getConfig().getString("world.freeze-weather", null);
        if (freezeWeather != null) {
            switch (freezeWeather.toUpperCase()) {
                case "CLEAR":
                    world.setStorm(false);
                    world.setWeatherDuration(0);
                    world.setThundering(false);
                    break;
                case "RAIN":
                    world.setStorm(true);
                    world.setWeatherDuration(Integer.MAX_VALUE); // Длительность дождя
                    world.setThundering(false);
                    break;
                case "THUNDER":
                    world.setStorm(true);
                    world.setWeatherDuration(Integer.MAX_VALUE); // Длительность грозы
                    world.setThundering(true);
                    break;
                default:
                    plugin.getLogger().warning("Неизвестный тип погоды: " + freezeWeather);
                    world.setStorm(false);
                    world.setThundering(false);
            }
        } else {
            world.setStorm(false);
            world.setThundering(false);
        }
    }
}
