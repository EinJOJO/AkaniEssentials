package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;


@CommandAlias("ptime|time")
public class TimeCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public TimeCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }


    public void sendTimeMessage(Player sender, String time) {
        plugin.sendMessage(sender, EssentialKey.of("time.set"), (s) -> s.replaceAll("%time%", time));
    }

    @Subcommand("set")
    public void setTime(Player sender, String time) {
        try {
            long timeLong = Long.parseLong(time);
            boolean playerTime = getExecCommandLabel().equals("ptime");
            if (playerTime) {
                sender.setPlayerTime(timeLong, false);
            } else {
                sender.getWorld().setTime(timeLong);
            }
            sendTimeMessage(sender, time);
        } catch (NumberFormatException e) {
            plugin.sendMessage(sender, EssentialKey.of("time.invalid"));
        }
    }

    @CommandAlias("day|night")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "time")
    @Description("Set the time of the world")
    public void time(Player sender) {
        time(sender, getExecCommandLabel().toLowerCase());
    }

    @CommandCompletion("day|night")
    @Syntax("<day|night>")
    @Default
    public void time(Player sender, @Optional String time) {
        boolean playerTime = getExecCommandLabel().equals("ptime");
        if (time != null) {
            switch (time.toLowerCase()) {
                case "day":
                    if (playerTime) {
                        sender.setPlayerTime(6000, false);
                    } else {
                        sender.getWorld().setTime(6000);
                    }
                    sendTimeMessage(sender, "DAY");
                    break;
                case "night":
                    if (playerTime) {
                        sender.setPlayerTime(18000, false);
                    } else {
                        sender.getWorld().setTime(18000);
                    }
                    break;
                default:
                    plugin.sendMessage(sender, EssentialKey.of("time.invalid"));
            }
        }
    }

}
