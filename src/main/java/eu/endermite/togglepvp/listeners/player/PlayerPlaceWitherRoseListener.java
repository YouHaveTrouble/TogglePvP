package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.BoundingBoxUtil;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

public class PlayerPlaceWitherRoseListener implements Listener {

    private ConfigCache config = TogglePvP.getPlugin().getConfigCache();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerWitherRosePlace(org.bukkit.event.block.BlockPlaceEvent event) {

        if (!TogglePvP.getPlugin().getConfigCache().isLava_and_fire_stopper_enabled())
            return;

        if(event.getBlock().getType().equals(Material.WITHER_ROSE)) {

            Location location = event.getBlockPlaced().getLocation();
            double radius = config.getLava_and_fire_stopper_radius();

            BoundingBox boundingBox = BoundingBoxUtil.getBoundingBox(location, radius);
            for (Entity entity : location.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof Player) {
                    Player damager = event.getPlayer();
                    Player victim = (Player) entity;
                    if (victim != damager) {
                        boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager);
                        if (!damagerPvpEnabled) {
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                            event.setCancelled(true);
                            return;
                        }
                        boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim);
                        if (!victimPvpEnabled) {
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}