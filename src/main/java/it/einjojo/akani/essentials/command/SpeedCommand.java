package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;


@CommandAlias("speed|flyspeed")
@Description("Ändert die Lauf- oder Fluggeschwindigkeit.")
public class SpeedCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public SpeedCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "speed")
    @Syntax("[speed]")
    @CommandCompletion("@range:1-10")
    public void setSpeed(Player sender, @Default("2") @Single String speed) {
        //Check if speed is number
        try {
            int speedInt = Integer.parseInt(speed);
            if (speedInt < 1 || speedInt > 10) {
                plugin.sendMessage(sender, EssentialKey.of("speed.invalid"));
                return;
            }
            float speedFloat = (float) speedInt / 10;
            if (getExecCommandLabel().equals("speed")) {
                if (sender.isFlying()) {
                    setFlightSpeed(sender, speedFloat);
                } else {
                    sender.setWalkSpeed(speedFloat);
                    plugin.sendMessage(sender, EssentialKey.of("speed.walk-changed"));
                }
            } else {
                setFlightSpeed(sender, speedFloat);
            }
        } catch (NumberFormatException e) {
            plugin.sendMessage(sender, EssentialKey.of("speed.invalid"));
        }
    }

    public void setFlightSpeed(Player sender, float speedFloat) {
        sender.setFlySpeed(speedFloat);
        plugin.sendMessage(sender, EssentialKey.of("speed.fly-changed"));
    }
}
