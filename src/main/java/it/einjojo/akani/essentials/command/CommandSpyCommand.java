package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.cmdspy.CommandObserver;
import it.einjojo.akani.essentials.cmdspy.CommandObserverRegistry;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("commandspy|cmdspy")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "commandspy")
public class CommandSpyCommand extends BaseCommand {

    private final CommandObserverRegistry registry;
    private final AkaniEssentialsPlugin plugin;

    public CommandSpyCommand(AkaniEssentialsPlugin plugin, CommandObserverRegistry registry) {
        plugin.commandManager().registerCommand(this);
        this.plugin = plugin;
        this.registry = registry;
    }

    @Subcommand("all")
    public void commandSpy(Player sender) {
        if (registry.isSubscribed(sender.getUniqueId())) {
            commandSpyOff(sender);
        } else {
            registry.subscribeToAllPlayerCommands(sender.getUniqueId());
            plugin.sendMessage(sender, EssentialKey.of("cmdspy.all"));
        }

    }

    @Subcommand("target")
    public void commandSpyTarget(Player sender, OnlinePlayer target) {
        registry.observers().stream()
                .filter(observer -> {
                    if (observer.observer().equals(sender.getUniqueId())) return false;
                    if (observer instanceof CommandObserver.Target targetObserver) {
                        return targetObserver.target().equals(target.player.getUniqueId());
                    } else {
                        return false;
                    }
                }).findAny().ifPresentOrElse((present -> {
                    registry.observers().remove(present);
                    plugin.sendMessage(sender, EssentialKey.of("cmdspy.off"));
                }), () -> {
                    registry.subscribeToPlayerCommands(sender.getUniqueId(), target.player.getUniqueId());
                    plugin.sendMessage(sender, EssentialKey.of("cmdspy.target"), (s) -> s.replaceAll("%player%", target.player.getName()));
                });

    }

    @Subcommand("off")
    public void commandSpyOff(Player sender) {
        registry.unsubscribe(sender.getUniqueId());
        plugin.sendMessage(sender, EssentialKey.of("cmdspy.off"));
    }


}
