package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class SpawnManager implements Listener {

    private LobbyMaster plugin;

    public SpawnManager(LobbyMaster plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); // Регистрация событий
    }

    private Location getSpawnLocation() {
        if (plugin.getConfig().getBoolean("spawn.enabled", false)) {
            World world = Bukkit.getWorld(plugin.getConfig().getString("spawn.location.world", "world"));
            if (world != null) {
                double x = plugin.getConfig().getDouble("spawn.location.x", 0);
                double y = plugin.getConfig().getDouble("spawn.location.y", 64);
                double z = plugin.getConfig().getDouble("spawn.location.z", 0);
                float yaw = (float) plugin.getConfig().getDouble("spawn.location.yaw", 0);
                float pitch = (float) plugin.getConfig().getDouble("spawn.location.pitch", 0);
                return new Location(world, x, y, z, yaw, pitch);
            } else {
                plugin.getLogger().warning("Мир для точки спавна не найден: " + plugin.getConfig().getString("spawn.location.world", "world"));
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("spawn.enabled", false)) {
            Location spawnLocation = getSpawnLocation();
            if (spawnLocation != null) {
                event.getPlayer().teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getConfig().getBoolean("spawn.enabled", false)) {
            Player player = event.getPlayer();
            Location spawnLocation = getSpawnLocation();
            if (spawnLocation != null) {
                World world = player.getWorld();
                if (world != null && !world.equals(spawnLocation.getWorld())) {
                    player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        // Проверяем, активирована ли функция возврата на спавн при падении
        if (plugin.getConfig().getBoolean("spawn.return-on-fall", false)) {
            // Проверяем, находится ли игрок в пустоте
            if (location.getY() < 0) {
                Location spawnLocation = getSpawnLocation();
                if (spawnLocation != null) {
                    player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
            }
        }
    }
}
