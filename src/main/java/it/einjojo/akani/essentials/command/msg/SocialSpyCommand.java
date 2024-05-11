package it.einjojo.akani.essentials.command.msg;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.command.TargetNotFoundException;
import it.einjojo.akani.essentials.service.MessageService;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("socialspy|spy")
@CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "socialspy")
public class SocialSpyCommand extends BaseCommand {
    private final AkaniEssentialsPlugin plugin;

    public SocialSpyCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }

    @Subcommand("toggle")
    @CommandCompletion(MessageService.SOCIALSPY_WILDCARD + "|@akaniplayers")
    public void toggleSocialSpy(Player bukkitSender, @Optional String wildcardOrName) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            List<String> spyTargets = plugin.messageService().socialSpy(bukkitSender.getUniqueId());
            if (wildcardOrName == null) {
                if (spyTargets.isEmpty()) {
                    plugin.sendMessage(bukkitSender, EssentialKey.SPECIFY_PLAYER);
                } else {
                    plugin.messageService().clearSocialSpy(bukkitSender.getUniqueId());
                    plugin.sendMessage(bukkitSender, EssentialKey.of("socialspy.disabled"), s -> s.replaceAll("%player%", "ALL"));
                }
            } else {
                if (spyTargets.contains(wildcardOrName)) {
                    if (wildcardOrName.equals(MessageService.SOCIALSPY_WILDCARD)) {
                        toggleSocialSpy(bukkitSender, null);
                    } else {
                        plugin.messageService().removeSocialSpy(bukkitSender.getUniqueId(), wildcardOrName);
                        plugin.sendMessage(bukkitSender, EssentialKey.of("socialspy.enabled"), s -> s.replaceAll("%player%", wildcardOrName));
                    }
                } else {
                    if (wildcardOrName.equals(MessageService.SOCIALSPY_WILDCARD)) {
                        plugin.messageService().clearSocialSpy(bukkitSender.getUniqueId()); // less entries to iterate over
                        plugin.messageService().addSocialSpy(bukkitSender.getUniqueId(), wildcardOrName);
                        plugin.sendMessage(bukkitSender, EssentialKey.of("socialspy.enabled"), s -> s.replaceAll("%player%", "ALL"));
                        return;
                    }
                    AkaniPlayer player = plugin.core().playerManager().onlinePlayerByName(wildcardOrName).orElseThrow(() -> new TargetNotFoundException(wildcardOrName));
                    plugin.messageService().addSocialSpy(bukkitSender.getUniqueId(), player.uuid().toString());
                    plugin.sendMessage(bukkitSender, EssentialKey.of("socialspy.enabled"), s -> s.replaceAll("%player%", player.name()));
                }
            }
        });

    }

    @Subcommand("info")
    public void info(Player bukkitSender) {
        List<String> spyTargets = plugin.messageService().socialSpy(bukkitSender.getUniqueId());
        if (spyTargets.isEmpty()) {
            bukkitSender.sendMessage("§cSocialSpy ist aus.");
            return;
        }
        for (String spying : spyTargets) {
            bukkitSender.sendMessage("§8- §e" + spying);
        }
    }
}
