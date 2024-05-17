package it.einjojo.akani.essentials.booster.impl;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.booster.Booster;
import it.einjojo.akani.essentials.booster.ExpiryTracker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Break3x3Booster implements Listener, Booster {
    private static final Duration DURATION = Duration.ofMinutes(5);
    private final Set<UUID> activePlayers = new HashSet<>();
    private final ExpiryTracker expiryTracker = new ExpiryTracker(activePlayers::remove);

    public Break3x3Booster(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String name() {
        return "3x3-Breaker";
    }

    @Override
    public String description() {
        return "Zerstört 3x3 Blöcke auf einmal.";
    }

    @Override
    public void enable(Player player) {
        activePlayers.add(player.getUniqueId());
        expiryTracker.track(player.getUniqueId(), DURATION);
    }

    @Override
    public void disable(Player player) {
        activePlayers.remove(player.getUniqueId());
        expiryTracker.remove(player.getUniqueId());
    }

    @Override
    public boolean hasEnabled(UUID playerUuid) {
        return activePlayers.contains(playerUuid);
    }

    @Override
    public boolean canEnable(Player player) {
        return true;
    }

    @Override
    public boolean hasUnlocked(Player player) {
        return player.hasPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "booster.break3x3");
    }

    @Override
    public void tick() {
        expiryTracker.runCheck();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (hasEnabled(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Block block = event.getBlock();
            Location location = block.getLocation();
            World world = location.getWorld();
            ItemStack tool = player.getInventory().getItemInMainHand();
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Block relative = world.getBlockAt(location.clone().add(x, y, z));
                        if (relative.getType() != Material.AIR && block.isPreferredTool(tool)) {
                            relative.breakNaturally();
                        }
                    }
                }
            }
        }
    }
}
