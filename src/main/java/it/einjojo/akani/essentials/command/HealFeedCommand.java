package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.paper.player.PaperAkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//TODO Cooldown
public class HealFeedCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;


    public HealFeedCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        PaperCommandManager manager = plugin.commandManager();
        //Register the command
        manager.registerCommand(this);
    }

    @CommandAlias("feed")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "feed")
    @Description("Feed yourself or another player")
    @CommandCompletion("@akaniplayers:includeSender")
    public void feed(CommandSender sender, @Optional PaperAkaniPlayer targetAkaniPlayer) {
        if (targetAkaniPlayer == null) {
            if (sender instanceof Player player) {
                player.setFoodLevel(20);
                plugin.sendMessage(sender, EssentialKey.FEED_SELF);
            } else {
                plugin.sendMessage(sender, EssentialKey.GENERIC_ERROR);
            }
        } else {
            java.util.Optional<Player> target = targetAkaniPlayer.bukkitPlayer();
            target.ifPresentOrElse(targetPlayer -> {
                targetPlayer.setFoodLevel(20);
                plugin.sendMessage(sender, EssentialKey.FEED_OTHER, s -> s.replaceAll("%player%", targetPlayer.getName()));
            }, () -> {
                plugin.sendMessage(sender, EssentialKey.PLAYER_NOT_FOUND);
            });
        }
    }

    @CommandAlias("heal")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "heal")
    @Description("Heal yourself or another player")
    @CommandCompletion("@akaniplayers:includeSender")
    public void heal(CommandSender sender, @Optional PaperAkaniPlayer targetAkaniPlayer) {
        if (targetAkaniPlayer == null) {
            if (sender instanceof Player player) {
                player.setHealth(player.getMaxHealth());
                plugin.sendMessage(sender, EssentialKey.HEAL_SELF);
            } else {
                plugin.sendMessage(sender, EssentialKey.GENERIC_ERROR);
            }
        } else {
            java.util.Optional<Player> target = targetAkaniPlayer.bukkitPlayer();
            target.ifPresentOrElse(targetPlayer -> {
                targetPlayer.setHealth(targetPlayer.getMaxHealth());
                plugin.sendMessage(sender, EssentialKey.HEAL_OTHER, s -> s.replaceAll("%player%", targetPlayer.getName()));
            }, () -> {
                plugin.sendMessage(sender, EssentialKey.PLAYER_NOT_FOUND);
            });
        }
    }
}
