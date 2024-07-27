package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("fly")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "fly")
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
    public void toggleFly(Player sender, @Optional OnlinePlayer targetPlayer) {
        Player target = targetPlayer != null ? targetPlayer.getPlayer() : sender;
        boolean other = target != sender;
        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            if (other)
                plugin.sendMessage(sender, EssentialKey.FLY_DISABLED_OTHER, (s) -> s.replaceAll("%player%", target.getName()));
            plugin.sendMessage(target, EssentialKey.FLY_DISABLED);
        } else {
            target.setAllowFlight(true);
            if (other)
                plugin.sendMessage(sender, EssentialKey.FLY_ENABLED_OTHER, (s) -> s.replaceAll("%player%", target.getName()));
            plugin.sendMessage(target, EssentialKey.FLY_ENABLED);
        }
    }


}
