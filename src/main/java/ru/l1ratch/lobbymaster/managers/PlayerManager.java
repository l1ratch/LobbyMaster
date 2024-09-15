package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.entity.Player;
import ru.l1ratch.lobbymaster.LobbyMaster;

public class PlayerManager implements Listener {

    private LobbyMaster plugin;

    public PlayerManager(LobbyMaster plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); // Убедитесь, что это сделано
    }

    // Принудительная смена игрового режима при входе
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String gameModeString = plugin.getConfig().getString("player.gamemode", "ADVENTURE");
        player.setGameMode(GameMode.valueOf(gameModeString.toUpperCase()));
    }

    // Запрет получения урона
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.hasPermission("lm.ignore-damage") && plugin.getConfig().getBoolean("player.damage-disabled", true)) {
                event.setCancelled(true);
            }
        }
    }

    // Запрет голода
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.hasPermission("lm.ignore-feed") && plugin.getConfig().getBoolean("player.hunger-disabled", true)) {
                event.setCancelled(true);
            }
        }
    }

    // Запрет ломания блоков
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("lm.ignore-block") && plugin.getConfig().getBoolean("player.block-break-disabled", true)) {
            event.setCancelled(true);
        }
    }

    // Запрет установки блоков
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("lm.ignore-block") && plugin.getConfig().getBoolean("player.block-place-disabled", true)) {
            event.setCancelled(true);
        }
    }

    // Запрет взрывов (взрывной урон)
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (plugin.getConfig().getBoolean("player.explosion-disabled", true)) {
            event.setCancelled(true);
        }
    }

    // Запрет распространения огня и сгорания блоков
    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (plugin.getConfig().getBoolean("player.fire-spread-disabled", true)) {
            event.setCancelled(true);
        }
    }

    // Запрет таяния льда
    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.getBlock().getType() == Material.ICE || event.getBlock().getType() == Material.PACKED_ICE || event.getBlock().getType() == Material.SNOW) {
            if (plugin.getConfig().getBoolean("player.ice-melt-disabled", true)) {
                event.setCancelled(true);
            }
        }
    }

    // Запрет физики блоков
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (plugin.getConfig().getBoolean("player.block-physics-disabled", true)) {
            event.setCancelled(true);
        }
    }
}
