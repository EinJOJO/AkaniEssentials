package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.entity.Player;

@CommandAlias("invsee")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "invsee")
public class InvseeCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public InvseeCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@players")
    public void invsee(Player sender, OnlinePlayer target) {
        sender.openInventory(target.getPlayer().getInventory()); //TODO use husk-sync
    }

}
