package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerHitByProjectileListener implements Listener {

    /**
     * Cancels damage done by projectiles to player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player damager = (Player) projectile.getShooter();
                Player victim = (Player) event.getEntity();

                // Ender pearls and other self-damage
                if (damager == victim) {
                    return;
                }

                ConfigCache config = TogglePvP.getPlugin().getConfigCache();

                boolean damagerPvpEnabled = (boolean) TogglePvP.getPlugin().getPlayerManager().getPlayer(damager).get("pvpenabled");
                boolean victimPvpEnabled = (boolean) TogglePvP.getPlugin().getPlayerManager().getPlayer(victim).get("pvpenabled");

                if (!damagerPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                    return;
                }
                if (!victimPvpEnabled) {
                    event.setCancelled(true);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                }

            }
        }

    }

}