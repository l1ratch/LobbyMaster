package ru.l1ratch.lobbymaster.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.l1ratch.lobbymaster.LobbyMaster;

import java.util.List;

public class EffectManager implements Listener {

    private final LobbyMaster plugin; // Объявлено как final

    public EffectManager(LobbyMaster plugin) {
        this.plugin = plugin;
        // Регистрация слушателя
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // Применение эффектов ко всем игрокам при запуске плагина
        applyEffectsToAllPlayers();
    }

    // Применение эффектов ко всем игрокам
    public void applyEffectsToAllPlayers() {
        if (plugin.getConfig().getBoolean("effects.enabled", true)) {
            List<String> effects = plugin.getConfig().getStringList("effects.list");
            for (Player player : Bukkit.getOnlinePlayers()) {
                applyEffectsToPlayer(player, effects);
            }
        }
    }

    // Применение эффектов к игроку
    private void applyEffectsToPlayer(Player player, List<String> effects) {
        for (String effectConfig : effects) {
            String[] parts = effectConfig.split(":");
            if (parts.length == 3) {
                try {
                    PotionEffectType effectType = PotionEffectType.getByName(parts[0].toUpperCase());
                    int level = Integer.parseInt(parts[1]);
                    String durationString = parts[2];

                    int duration;
                    if (durationString.equals("0")) {
                        duration = Integer.MAX_VALUE; // Вечный эффект
                    } else {
                        duration = Integer.parseInt(durationString) * 20; // Время в секундах, переводим в тики
                    }

                    if (effectType != null) {
                        player.addPotionEffect(new PotionEffect(effectType, duration, level - 1));
                    }
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Неверный формат эффекта: " + effectConfig);
                }
            } else {
                plugin.getLogger().warning("Неверный формат эффекта: " + effectConfig);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("effects.enabled", true)) {
            List<String> effects = plugin.getConfig().getStringList("effects.list");
            applyEffectsToPlayer(player, effects);
        }
    }
}
