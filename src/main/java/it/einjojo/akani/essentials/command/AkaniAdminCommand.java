package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardProvider;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import redis.clients.jedis.JedisPool;

import java.util.List;

@CommandAlias("akani|essentials")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "admin")
public class AkaniAdminCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public AkaniAdminCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Subcommand("reload-messages")
    public void reloadMessages(CommandSender sender) {
        plugin.core().messageManager().load();
        sender.sendMessage("§aMessages reloaded.");
    }

    @Subcommand("redis")
    public void showRedisStatus(CommandSender sender) {
        JedisPool pool = plugin.core().jedisPool();
        Component message = Component.newline().append(Component.text("§cRedis Pool §7status: " + (!pool.isClosed() ? "§aConnected" : "§cDisconnected")).appendNewline()
                .append(Component.text("§7Active: §a" + pool.getNumActive() + "§7, Idle: §a" + pool.getNumIdle() + "§7, Waiters: §a" + pool.getNumWaiters() + "§7.")).appendNewline()
                .append(Component.text("§7Max Active: §a" + pool.getMaxTotal() + "§7, Max Idle: §a" + pool.getMaxIdle() + "§7.")).appendNewline()
                .append(Component.text("§7Min Evictable Idle Time: §a" + pool.getMinEvictableIdleTimeMillis() + "§7, Time Between Eviction Runs: §a" + pool.getTimeBetweenEvictionRunsMillis() + "§7.")).appendNewline());
        sender.sendMessage(message);
    }

    @Subcommand("redis subscribe")
    public void subscribe(CommandSender sender, String channel) {
        plugin.core().brokerService().subscribe(channel);
        sender.sendMessage(Component.text("§7Subscribed to channel §e" + channel + "§7."));
    }

    @Subcommand("online-players")
    @CommandCompletion("--redis")
    public void akaniPlayers(CommandSender sender, @Optional String arg) {
        boolean fromRedis = arg != null && arg.equals("--redis");
        List<AkaniPlayer> players = fromRedis ? plugin.core().playerStorage().onlinePlayers() : plugin.core().playerManager().onlinePlayers();
        sender.sendMessage("§7Online players: §a" + players.size() + "§7." + (fromRedis ? " (from Redis)" : ""));
        for (AkaniPlayer akaniPlayer : players) {
            sender.sendMessage("§8- §7" + akaniPlayer.name() + " §8(§a" + akaniPlayer.serverName() + "§8)");
        }
    }

    @Subcommand("scoreboard-providers")
    @CommandCompletion("@players")
    public void scoreboardProviders(CommandSender sender, @Optional OnlinePlayer optionalTarget) {
        Player target = optionalTarget != null ? optionalTarget.getPlayer() : (Player) sender;
        sender.sendMessage("§eScoreboard providers: §a" + plugin.scoreboardManager().providers().size() + "§7. Tested on " + target.getName() + "§7.");
        for (ScoreboardProvider provider : plugin.scoreboardManager().providers()) {
            sender.sendMessage("§8- §7" + provider.getClass().getSimpleName() + " §8(" + (provider.shouldProvide(target) ? "§a" : "§c") + provider.priority() + "§8)");
        }
    }

    @Subcommand("tnt-minecart")
    public void tntMinecart(Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            player.getWorld().spawn(player.getLocation(), ExplosiveMinecart.class);
        }

    }


    @Subcommand("reload")
    public void reload(Player player) {
        plugin.core().playerManager().loadOnlinePlayers();
        player.sendMessage("§aPlayers reloaded from redis.");
    }
}