package it.einjojo.akani.essentials;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.litecommands.OfflinePlayerResolver;
import it.einjojo.akani.core.api.litecommands.OnlinePlayerResolver;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.essentials.command.TeleportCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniEssentialsPlugin extends JavaPlugin {
    public static final String PERMISSION_BASE = "akani.essentials.";
    private AkaniCore core;
    private LiteCommands<CommandSender> commands;

    @Override
    public void onEnable() {
        core = AkaniCoreProvider.get();
        commands = LiteCommandsBukkit.builder("essentials", this)
                .argument(AkaniPlayer.class, new OnlinePlayerResolver<>(core))
                .argument(AkaniOfflinePlayer.class, new OfflinePlayerResolver<>(core))
                .commands(
                        new TeleportCommand(core)
                )
                .build();
        commands.register();
    }

    @Override
    public void onDisable() {
        commands.unregister();
    }
}
