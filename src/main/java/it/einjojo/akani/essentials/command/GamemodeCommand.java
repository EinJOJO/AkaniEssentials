package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@CommandAlias("gamemode|gm")
public class GamemodeCommand extends BaseCommand {
    private final static Map<String, GameMode> GAMEMODE_MAP = Map.ofEntries(
            Map.entry("survival", GameMode.SURVIVAL),
            Map.entry("creative", GameMode.CREATIVE),
            Map.entry("adventure", GameMode.ADVENTURE),
            Map.entry("spectator", GameMode.SPECTATOR),
            Map.entry("0", GameMode.SURVIVAL),
            Map.entry("1", GameMode.CREATIVE),
            Map.entry("2", GameMode.ADVENTURE),
            Map.entry("3", GameMode.SPECTATOR)
    );
    private final AkaniEssentialsPlugin plugin;


    public GamemodeCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        PaperCommandManager manager = plugin.commandManager();
        manager.getCommandCompletions().registerStaticCompletion("gamemodes", GAMEMODE_MAP.keySet());
        manager.registerCommand(this);
    }

    @Default
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "gamemode")
    @CommandCompletion("@gamemodes @akaniplayers")
    @Syntax("<gamemode> [player]")
    public void changeGameMode(CommandSender sender, String gameMode, @Optional AkaniPlayer player) {
        GameMode mode = GAMEMODE_MAP.get(gameMode);
        if (mode == null) {
            plugin.sendMessage(sender, EssentialKey.of("gamemode.invalid"));
            return;
        }
        if (sender instanceof Player senderPlayer) {
            // command executed by player
            if (player == null) {
                senderPlayer.setGameMode(mode);
                plugin.sendMessage(senderPlayer, EssentialKey.of("gamemode.success"));
                return;
            } else {
                player.server().runCommand("gamemode " + gameMode + " " + player.name());
                plugin.sendMessage(senderPlayer, EssentialKey.of("gamemode.success.other"), (s) -> s.replaceAll("%player%", player.name()));
            }
        } else {
            // Sender is console:
            if (player == null) {
                plugin.sendMessage(sender, EssentialKey.SPECIFY_PLAYER);
                return;
            }
            AkaniBukkitAdapter.bukkitPlayer(player.uuid()).ifPresentOrElse((bukkitPlayer) -> {
                bukkitPlayer.setGameMode(mode);
                plugin.sendMessage(sender, EssentialKey.of("gamemode.success.other"), (s) -> s.replaceAll("%player%", player.name()));
                plugin.sendMessage(bukkitPlayer, EssentialKey.of("gamemode.success"));
            }, () -> {
                player.server().runCommand("gamemode " + gameMode + " " + player.name());
            });
        }

    }


}
