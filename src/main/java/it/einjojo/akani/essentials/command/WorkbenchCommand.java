package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.entity.Player;


@CommandAlias("wb|workbench")
public class WorkbenchCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public WorkbenchCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "workbench")
    @Description("Open the workbench")
    public void openWorkbench(Player player) {
        player.openWorkbench(player.getLocation(), true);
    }

}
