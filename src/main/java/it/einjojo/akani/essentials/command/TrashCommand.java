package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("trash")
public class TrashCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public TrashCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void openTrash(Player sender) {
        Inventory trash = Bukkit.createInventory(null, 9 * 5);
        sender.openInventory(trash);
    }

}
