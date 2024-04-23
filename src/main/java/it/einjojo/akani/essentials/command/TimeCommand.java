package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;


@CommandAlias("day|night")
public class TimeCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public TimeCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "time")
    @Description("Set the time of the world")
    public void time(Player sender) {
        switch (getExecCommandLabel().toLowerCase()) {
            case "day":
                sender.getWorld().setTime(6000);
                sender.sendMessage(Component.text("Time set to day"));
                break;
            case "night":
                sender.getWorld().setTime(13000);
                sender.sendMessage(Component.text("Time set to night"));
                break;
            default:
                sender.sendMessage(Component.text("Invalid time"));
        }
    }

}
