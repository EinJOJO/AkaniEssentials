package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HealFeedCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;


    public HealFeedCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        PaperCommandManager manager = plugin.commandManager();
        //Register online players as completions
        manager.getCommandCompletions().registerAsyncCompletion("aplayer",
                c -> plugin.core().playerManager().onlinePlayers().stream().map(AkaniPlayer::name).toList());
        //Register the command
        manager.registerCommand(this);
    }

    @CommandAlias("feed")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "feed")
    @Description("Feed yourself or another player")
    @CommandCompletion("@aplayer")
    public void feed(CommandSender sender, @Optional String target) {
        if (Objects.equals(getExecCommandLabel(), "feed")) {
            if (target == null) {
                if (sender instanceof Player player) {
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    plugin.sendMessage(sender, MessageKey.FEED_SELF);
                } else {
                    plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
                }
            } else {
                Player player = Bukkit.getPlayer(target);
                if (player != null) {
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    plugin.sendMessage(sender, MessageKey.FEED_OTHER, s -> s.replaceAll("%player%", player.getName()));
                } else {
                    plugin.sendMessage(sender, MessageKey.PLAYER_NOT_FOUND);
                }
            }
        }
    }

    @CommandAlias("heal")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "heal")
    @Description("Heal yourself or another player")
    @CommandCompletion("@aplayer")
    public void heal(CommandSender sender, @Optional String target) {
        if (Objects.equals(getExecCommandLabel(), "heal")) {
            if (target == null) {
                if (sender instanceof Player player) {
                    player.setHealth(20);
                    plugin.sendMessage(sender, MessageKey.HEAL_SELF);
                } else {
                    plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
                }
            } else {
                Player player = Bukkit.getPlayer(target);
                if (player != null) {
                    player.setHealth(20);
                    plugin.sendMessage(sender, MessageKey.HEAL_OTHER, s -> s.replaceAll("%player%", player.getName()));
                } else {
                    plugin.sendMessage(sender, MessageKey.PLAYER_NOT_FOUND);
                }
            }
        }
    }
}
