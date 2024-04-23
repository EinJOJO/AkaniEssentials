package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

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
    @CommandCompletion("@players")
    public void feed(CommandSender sender, @Optional Player target) {
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
                if (!target.isEmpty()) {
                    target.setFoodLevel(20);
                    target.setSaturation(20);
                    plugin.sendMessage(sender, MessageKey.FEED_OTHER, s -> s.replaceAll("%player%", target.getName()));
                } else {
                    plugin.sendMessage(sender, MessageKey.PLAYER_NOT_FOUND);
                }
            }
        }
    }

    @CommandAlias("heal")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "heal")
    @Description("Heal yourself or another player")
    @CommandCompletion("@players")
    public void heal(CommandSender sender, @Optional Player target) {
        if (Objects.equals(getExecCommandLabel(), "heal")) {
            if (target == null) {
                if (sender instanceof Player player) {
                    player.setHealth(20);
                    plugin.sendMessage(sender, MessageKey.HEAL_SELF);
                } else {
                    plugin.sendMessage(sender, MessageKey.GENERIC_ERROR);
                }
            } else {
                if (!target.isEmpty()) {
                    target.setHealth(20);
                    plugin.sendMessage(sender, MessageKey.HEAL_OTHER, s -> s.replaceAll("%player%", target.getName()));
                } else {
                    plugin.sendMessage(sender, MessageKey.PLAYER_NOT_FOUND);
                }
            }
        }
    }
}
