package it.einjojo.akani.essentials.command.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("akaniplayers")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "akaniplayers")
public class AkaniPlayersCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public AkaniPlayersCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @Subcommand("list")
    @CommandCompletion("--redis")
    public void akaniPlayers(CommandSender sender, @Optional String arg) {
        boolean fromRedis = arg != null && arg.equals("--redis");
        List<AkaniPlayer> players = fromRedis ? plugin.core().playerStorage().onlinePlayers() : plugin.core().playerManager().onlinePlayers();
        sender.sendMessage("§7Online players: §a" + players.size() + "§7." + (fromRedis ? " (from Redis)" : ""));
        for (AkaniPlayer akaniPlayer : players) {
            sender.sendMessage("§8- §7" + akaniPlayer.name() + " §8(§a" + akaniPlayer.serverName() + "§8)");
        }
    }

    @Subcommand("reload")
    public void reload(Player player) {
        plugin.core().playerManager().loadOnlinePlayers();
        player.sendMessage("§aPlayers reloaded from redis.");
    }
}