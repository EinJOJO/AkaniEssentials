package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.scoreboard.ScoreboardProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("akani|essentials")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "admin")
public class AkaniAdminCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public AkaniAdminCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
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


    @Subcommand("reload")
    public void reload(Player player) {
        plugin.core().playerManager().loadOnlinePlayers();
        player.sendMessage("§aPlayers reloaded from redis.");
    }
}