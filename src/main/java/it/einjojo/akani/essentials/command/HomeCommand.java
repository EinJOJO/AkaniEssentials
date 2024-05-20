package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

@CommandAlias("home|homes")
public class HomeCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public HomeCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
        plugin.commandManager().getCommandCompletions().registerAsyncCompletion("homes", c -> plugin.core().homeManager().homes(c.getPlayer().getUniqueId()).homes().stream().map(Home::name).toList());
    }

    @Default
    @CommandCompletion("@homes")
    public void teleportHome(Player sender, String homeName) {
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
    public void listHomes(Player sender) {
        plugin.core().homeManager().homesAsync(sender.getUniqueId()).thenAccept(homeHolder -> {
            plugin.sendMessage(sender, EssentialKey.of("home.list.title"));
            for (Home home : homeHolder.homes()) {
                sender.sendMessage("§7" + homeHolder.homeCount());
                plugin.sendMessage(sender, EssentialKey.of("home.list.entry"), (s) -> s.replace("%home%", home.name()));
            }
        });
    }

    @Subcommand("set")
    @CommandCompletion("<name>")
    public void setHome(Player sender, String name) {
        if (name.length() > 16) {
            plugin.sendMessage(sender, EssentialKey.of("home.name-too-long"));
            return;
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.core().homeManager().homes(sender.getUniqueId()).home(name).isPresent()) {
                plugin.sendMessage(sender, EssentialKey.of("home.exists"), (s) -> s.replace("%home%", name));
                return;
            }
            Home home = plugin.core().createHomeFactory().createHome(sender.getUniqueId(), name, AkaniBukkitAdapter.networkLocation(sender.getLocation()).referenceName(plugin.core().serverName()).type(NetworkLocation.Type.SERVER).build());
            if (plugin.core().homeManager().homes(sender.getUniqueId()).addHome(home)) {
                plugin.sendMessage(sender, EssentialKey.of("home.set"), (s) -> s.replace("%home%", name));
            } else {
                plugin.sendMessage(sender, EssentialKey.GENERIC_ERROR);
            }
        });
    }

    @Subcommand("remove")
    @CommandCompletion("@homes")
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
