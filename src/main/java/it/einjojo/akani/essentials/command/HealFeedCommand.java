package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public void feed(Player sender, @Optional Player target) {
        if (target == null) {
            sender.setFoodLevel(20);
            sender.setSaturation(20);
            plugin.sendMessage(sender, MessageKey.FEED_SELF);
        } else {
            target.setFoodLevel(20);
            target.setSaturation(20);
            plugin.sendMessage(sender, MessageKey.FEED_OTHER, s -> s.replaceAll("%player%", target.getName()));
        }
    }

    @CommandAlias("heal")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "heal")
    @Description("Heal yourself or another player")
    @CommandCompletion("@players")
    public void heal(CommandSender sender, @Optional Player target) {
        if (target == null) {
            if (!(sender instanceof Player senderPlayer)) return;
            senderPlayer.setHealth(20);
            plugin.sendMessage(sender, MessageKey.HEAL_SELF);
        } else {
            target.setHealth(20);
            plugin.sendMessage(sender, MessageKey.HEAL_OTHER, s -> s.replaceAll("%player%", target.getName()));
        }
    }
}
