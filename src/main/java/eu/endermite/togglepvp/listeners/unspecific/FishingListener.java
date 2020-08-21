package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FishingListener implements Listener {

    ConfigCache config = TogglePvP.getPlugin().getConfigCache();

    /**
     * Prevents hooking players with disabled pvp with fishing rod
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFish(org.bukkit.event.player.PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            Player damager = event.getPlayer();
            Player victim = (Player) event.getCaught();
            if (damager == victim) {
                return;
            }
            boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
            if (!damagerPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                return;
            }
            boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
            if (!victimPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
            }
        } else if (event.getCaught() instanceof Wolf) {
            Wolf victim = (Wolf) event.getCaught();
            if (victim.getOwner() == null) {
                return;
            }
            Player damager = event.getPlayer();
            boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
            if (!damagerPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                return;
            }
            boolean victimPvpEnabled = (boolean) SmartCache.getPlayerData(victim.getOwner().getUniqueId()).get("pvpenabled");
            if (!victimPvpEnabled) {
                event.setCancelled(true);
                PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
            }
        }
    }

}