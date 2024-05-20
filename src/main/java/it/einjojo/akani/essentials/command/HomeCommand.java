package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.home.HomeHolder;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandAlias("home|homes")
public class HomeCommand extends BaseCommand {

    private static final Pattern PERMISSION_PATTERN = Pattern.compile("essentials\\.homes\\.(\\d+)");
    private final AkaniEssentialsPlugin plugin;

    public HomeCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
        plugin.commandManager().getCommandCompletions().registerAsyncCompletion("homes", c -> plugin.core().homeManager().homes(c.getPlayer().getUniqueId()).homes().stream().map(Home::name).toList());
    }

    @Default
    @CommandCompletion("@homes")
    @Description("teleport to a home")
    public void teleportHome(Player sender, @Optional @Single String homeName) {
        plugin.core().homeManager().homesAsync(sender.getUniqueId()).thenAcceptAsync((homeHolder -> {
            homeHolder.home(homeName).ifPresentOrElse((home) -> {
                home.teleport(sender);
                plugin.sendMessage(sender, EssentialKey.of("home.teleport"), (s) -> s.replace("%home%", homeName));
            }, () -> {
                plugin.sendMessage(sender, EssentialKey.of("home.not-found"), (s) -> s.replace("%home%", homeName));
            });
        })).exceptionally((e) -> {
            plugin.sendMessage(sender, EssentialKey.GENERIC_ERROR);
            plugin.getSLF4JLogger().error("Error while teleporting home", e);
            return null;
        });
    }

    @Subcommand("list")
    @Description("List your homes")
    public void listHomes(Player sender) {
        plugin.core().homeManager().homesAsync(sender.getUniqueId()).thenAccept(homeHolder -> {
            plugin.sendMessage(sender, EssentialKey.of("home.list.title"));
            for (Home home : homeHolder.homes()) {
                plugin.sendMessage(sender, EssentialKey.of("home.list.entry"), (s) -> s.replaceAll("%home%", home.name()));
            }
        }).exceptionally((e) -> {
            plugin.sendMessage(sender, EssentialKey.GENERIC_ERROR);
            plugin.getSLF4JLogger().error("Error while listing homes", e);
            return null;
        });
    }

    @Subcommand("set")
    @CommandCompletion("<name>")
    @Description("Set a home")
    public void setHome(Player sender, String name) {
        if (name.length() > 16) {
            plugin.sendMessage(sender, EssentialKey.of("home.name-too-long"));
            return;
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            HomeHolder homeHolder = plugin.core().homeManager().homes(sender.getUniqueId());
            if (homeHolder.home(name).isPresent()) {
                plugin.sendMessage(sender, EssentialKey.of("home.exists"), (s) -> s.replace("%home%", name));
                return;
            }
            int limit = getMaxHomeCount(sender);
            if (homeHolder.homeCount() >= limit) {
                plugin.sendMessage(sender, EssentialKey.of("home.limit"), (s) -> s.replace("%limit%", limit + ""));
                return;
            }

            Home home = plugin.core().createHomeFactory().createHome(sender.getUniqueId(), name, AkaniBukkitAdapter.networkLocation(sender.getLocation()).referenceName(plugin.core().serverName()).type(NetworkLocation.Type.SERVER).build());
            if (homeHolder.addHome(home)) {
                plugin.sendMessage(sender, EssentialKey.of("home.set"), (s) -> s.replace("%home%", name));
            } else {
                plugin.sendMessage(sender, EssentialKey.GENERIC_ERROR);
            }
        });
    }

    private int getMaxHomeCount(Player player) {
        if (player.hasPermission("essentials.homes")) {
            return Integer.MAX_VALUE;
        }
        for (String permission : player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).toList()) {
            Matcher matcher = PERMISSION_PATTERN.matcher(permission);
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return 3;
    }

    @Subcommand("remove")
    @CommandCompletion("@homes")
    @Description("Remove a home")
    public void removeHome(Player sender, String name) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.core().homeManager().homes(sender.getUniqueId()).removeHome(name)) {
                plugin.sendMessage(sender, EssentialKey.of("home.remove"), (s) -> s.replace("%home%", name));
            } else {
                plugin.sendMessage(sender, EssentialKey.of("home.not-found"), (s) -> s.replace("%home%", name));
            }
        });
    }


}
