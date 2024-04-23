package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.MessageKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("fly")
public class FlyCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public FlyCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }


    @Default
    @Description("Schalte den Flugmodus ein oder aus")
    @CommandCompletion("@players")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "fly")
    public void toggleFly(Player sender, @Optional Player target) {
        if (target == null) {
            if (sender.getAllowFlight()) {
                sender.setAllowFlight(false);
                plugin.sendMessage(sender, MessageKey.FLY_DISABLED);
            } else {
                sender.setAllowFlight(true);
                plugin.sendMessage(sender, MessageKey.FLY_ENABLED);
            }
        } else {
            if (target.isEmpty()) {
                plugin.sendMessage(sender, MessageKey.PLAYER_NOT_ONLINE);
                return;
            }
            if (target.getAllowFlight()) {
                target.setAllowFlight(false);
                plugin.sendMessage(sender, MessageKey.FLY_DISABLED_OTHER, (s) -> s.replaceAll("%player%", target.getName()));
                plugin.sendMessage(target, MessageKey.FLY_DISABLED);
            } else {
                target.setAllowFlight(true);
                plugin.sendMessage(sender, MessageKey.FLY_ENABLED_OTHER, (s) -> s.replaceAll("%player%", target.getName()));
                plugin.sendMessage(target, MessageKey.FLY_ENABLED);
            }
        }
    }


}
