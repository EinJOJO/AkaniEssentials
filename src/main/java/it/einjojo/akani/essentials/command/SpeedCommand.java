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
        plugin.commandManager().getCommandCompletions().registerStaticCompletion("speeds", new String[]{"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0"});
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "speed")
    @Syntax("<speed>")
    @CommandCompletion("@speeds")
    public void setSpeed(Player sender, @Default("0.2") String speed) {
        if (Float.parseFloat(speed) < 0.1 || Float.parseFloat(speed) > 1.0) {
            sender.sendMessage(plugin.miniMessage().deserialize("<red>Die Geschwindigkeit muss zwischen <yellow>0.1 <red>und <yellow>1.0 <red>liegen."));
            return;
        }
        if (getExecCommandLabel().equals("speed")) {
            setWalkingSpeed(sender, Float.parseFloat(speed));
        } else {
            setFlyingSpeed(sender, Float.parseFloat(speed));
        }
    }

    public void setWalkingSpeed(Player player, float speed) {
        player.setWalkSpeed(speed);
        player.sendMessage(plugin.miniMessage().deserialize("<green>Deine Laufgeschwindigkeit wurde auf <yellow>%s <green>gesetzt.".formatted(speed)));
    }

    public void setFlyingSpeed(Player player, float speed) {
        player.setFlySpeed(speed);
        player.sendMessage(plugin.miniMessage().deserialize("<green>Deine Fluggeschwindigkeit wurde auf <yellow>%s <green>gesetzt.".formatted(speed)));
    }


}
