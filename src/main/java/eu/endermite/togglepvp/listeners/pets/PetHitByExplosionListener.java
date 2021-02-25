package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetHitByExplosionListener implements Listener {

    /**
     * Cancels explosion damage for pets with pvp off that is caused by players
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitByExplosion(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Tameable))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
            return;

        Tameable victim = (Tameable) event.getEntity();
        if (victim.getOwner() == null)
            return;

        try {
            UUID damageruuid = UUID.fromString(event.getDamager().getMetadata("PLAYEREXPLODED").get(0).asString());
            if (victim.getOwner().getUniqueId() == damageruuid) {
                return;
            }
            ConfigCache config = TogglePvp.getPlugin().getConfigCache();
            SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();
            if (!smartCache.getPlayerData(damageruuid).isPvpEnabled()) {
                PluginMessages.sendActionBar(damageruuid, config.getCannot_attack_pets_attacker());
                event.setCancelled(true);
                return;
            }
            if (!smartCache.getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                PluginMessages.sendActionBar(damageruuid, config.getCannot_attack_pets_victim());
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damageruuid, victim.getOwner().getUniqueId());
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {}

    }
}