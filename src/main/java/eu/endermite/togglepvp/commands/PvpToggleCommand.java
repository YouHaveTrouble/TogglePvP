package eu.endermite.togglepvp.commands;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand {

    public static void toggle(CommandSender sender, String[] args) {

        Bukkit.getScheduler().runTaskAsynchronously(TogglePvp.getPlugin(), () -> {

            if (!sender.hasPermission("togglepvp.command.toggle")) {
                String message = ChatColor.translateAlternateColorCodes('&', TogglePvp.getPlugin().getConfigCache().getNo_permission());
                sender.sendMessage(message);
                return;
            }

            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    if (CombatTimer.isInCombat(player.getUniqueId())) {
                        sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                        return;
                    }

                    boolean currentState = TogglePvp.getPlugin().getPlayerManager().togglePlayerPvpState(player.getUniqueId());

                    String message = "";
                    if (currentState) {
                        message = PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getPvp_enabled());
                    } else {
                        message = PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getPvp_disabled());
                    }
                    player.sendMessage(message);
                } else {
                    sender.sendMessage("Try /pvp toggle <player>");
                }
            } else if (args.length == 2) {
                if (!sender.hasPermission("togglepvp.command.toggle.others")) {
                    String message = ChatColor.translateAlternateColorCodes('&', TogglePvp.getPlugin().getConfigCache().getNo_permission());
                    sender.sendMessage(message);
                    return;
                }

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (CombatTimer.isInCombat(player.getUniqueId())) {
                        sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                        return;
                    }
                }
                try {
                    Player player = Bukkit.getPlayer(args[1]);
                    boolean currentState = TogglePvp.getPlugin().getPlayerManager().togglePlayerPvpState(player.getUniqueId());
                    String message;
                    if (currentState) {
                        message = TogglePvp.getPlugin().getConfigCache().getPvp_enabled_other();
                    } else {
                        message = TogglePvp.getPlugin().getConfigCache().getPvp_disabled_other();
                    }
                    sender.sendMessage(PluginMessages.parsePlayerName(player, message));

                } catch (NullPointerException e) {
                    sender.sendMessage(PluginMessages.parseMessage("&cPlayer offline."));
                }
            } else {
                if (sender.hasPermission("togglepvp.command.toggle.others")) {
                    sender.sendMessage("Try /pvp toggle <player>");
                } else {
                    sender.sendMessage("Try /pvp toggle");
                }
            }
        });
    }

    public static void enable(CommandSender sender, String[] args) {
        if (!sender.hasPermission("togglepvp.command.toggle")) {
            String message = ChatColor.translateAlternateColorCodes('&', TogglePvp.getPlugin().getConfigCache().getNo_permission());
            sender.sendMessage(message);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
                TogglePvp.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
                String message = PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getPvp_enabled());
                player.sendMessage(message);
            } else {
                sender.sendMessage("Try /pvp enable <player>");
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission("togglepvp.command.toggle.others")) {
                String message = ChatColor.translateAlternateColorCodes('&', TogglePvp.getPlugin().getConfigCache().getNo_permission());
                sender.sendMessage(message);
                return;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
            }
            try {
                Player player = Bukkit.getPlayer(args[1]);
                String message = TogglePvp.getPlugin().getConfigCache().getPvp_enabled_other();
                sender.sendMessage(PluginMessages.parsePlayerName(player, message));
                TogglePvp.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
            } catch (NullPointerException e) {
                sender.sendMessage(PluginMessages.parseMessage("&cPlayer offline."));
            }
        } else {
            if (sender.hasPermission("togglepvp.command.toggle.others")) {
                sender.sendMessage("Try /pvp enable <player>");
            } else {
                sender.sendMessage("Try /pvp enable");
            }
        }
    }

    public static void disable(CommandSender sender, String[] args) {
        if (!sender.hasPermission("togglepvp.command.toggle")) {
            String message = ChatColor.translateAlternateColorCodes('&', TogglePvp.getPlugin().getConfigCache().getNo_permission());
            sender.sendMessage(message);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
                TogglePvp.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), false);
                String message = PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getPvp_disabled());
                player.sendMessage(message);
            } else {
                sender.sendMessage("Try /pvp disable <player>");
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission("togglepvp.command.toggle.others")) {
                String message = TogglePvp.getPlugin().getConfigCache().getNo_permission();
                sender.sendMessage(message);
                return;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(TogglePvp.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
            }
            try {
                Player player = Bukkit.getPlayer(args[1]);
                String message = TogglePvp.getPlugin().getConfigCache().getPvp_disabled_other();
                sender.sendMessage(PluginMessages.parsePlayerName(player, message));
                TogglePvp.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
            } catch (NullPointerException e) {
                sender.sendMessage(PluginMessages.parseMessage("&cPlayer offline."));
            }
        } else {
            if (sender.hasPermission("togglepvp.command.toggle.others")) {
                sender.sendMessage("Try /pvp disable <player>");
            } else {
                sender.sendMessage("Try /pvp disable");
            }
        }
    }

}
