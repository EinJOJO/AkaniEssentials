package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.entity.Player;


@CommandAlias("enderchest|ec")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "enderchest")
public class EnderChestCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public EnderChestCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }


    @Default
    public void onEnderchest(Player sender) {
        sender.openInventory(sender.getEnderChest());
    }

}
