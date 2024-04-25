package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.entity.Player;


@CommandAlias("speed|flyspeed")
public class SpeedCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public SpeedCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandCompletions().registerStaticCompletion("speed", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "speed")
    @Syntax("[speed]")
    @CommandCompletion("@speed")
    public void setSpeed(Player sender, @Default("2") @Single String speed) {
        try {
            int speedInt = Integer.parseInt(speed);
            if (speedInt < 1 || speedInt > 10) {
                sender.sendMessage(plugin.miniMessage().deserialize("<red>Die Geschwindigkeit muss zwischen <yellow>1 <red>und <yellow>1 <red>liegen."));
                return;
            }
            float speedFloat = (float) speedInt / 10;
            if (getExecCommandLabel().equals("speed")) {
                sender.setWalkSpeed(speedFloat);
                sender.sendMessage(plugin.miniMessage().deserialize("<green>Deine Laufgeschwindigkeit wurde auf <yellow>%s <green>gesetzt.".formatted(speedInt)));
            } else {
                sender.setFlySpeed(speedFloat);
                sender.sendMessage(plugin.miniMessage().deserialize("<green>Deine Fluggeschwindigkeit wurde auf <yellow>%s <green>gesetzt.".formatted(speedInt)));
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.miniMessage().deserialize("<red>Die Geschwindigkeit muss eine Zahl sein."));
        }
    }

    @CatchUnknown
    @HelpCommand
    public void unknownCommand(Player sender) {
        sender.sendMessage(plugin.miniMessage().deserialize("<red>Unbekannter Befehl. Verwende <yellow>/speed <dark_gray>[<yellow>speed<dark_gray>] <red>oder <yellow>/flyspeed <dark_gray>[<yellow>speed<dark_gray>]."));
    }
}
