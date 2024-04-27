package it.einjojo.akani.essentials.listener;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public record BlockThrower(AkaniEssentialsPlugin plugin) implements Listener {

    public BlockThrower {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().getType().isBlock()) return;
        Player player = event.getPlayer();
        int max = Integer.min(16, event.getItem().getAmount());
        for (int i = 0; i < max; i++) {
            Bukkit.getScheduler().runTaskLater(plugin(), () -> {
                if (event.getItem().getType().equals(Material.TNT)) {
                    var entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                    entity.setVelocity(player.getLocation().getDirection().multiply(2));
                } else {
                    FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 1, 0), event.getItem().getType().createBlockData());
                    block.setVelocity(player.getLocation().getDirection().multiply(2));
                    block.shouldAutoExpire(true);
                    block.setDropItem(false);
                }

            }, i);
        }

    }

}
