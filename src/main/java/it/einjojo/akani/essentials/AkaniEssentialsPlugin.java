package it.einjojo.akani.essentials;

import com.google.gson.Gson;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.litecommands.OfflinePlayerResolver;
import it.einjojo.akani.core.api.litecommands.OnlinePlayerResolver;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.essentials.command.InvalidUsageCommandHandler;
import it.einjojo.akani.essentials.command.TeleportCommand;
import it.einjojo.akani.essentials.command.WarpCommand;
import it.einjojo.akani.essentials.command.resolver.GamemodeResolver;
import it.einjojo.akani.essentials.command.resolver.WarpResolver;
import it.einjojo.akani.essentials.listener.ChatListener;
import it.einjojo.akani.essentials.util.EssentialsMessageProvider;
import it.einjojo.akani.essentials.warp.Warp;
import it.einjojo.akani.essentials.warp.WarpManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniEssentialsPlugin extends JavaPlugin {
    public static final String PERMISSION_BASE = "akani.essentials.";
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private PaperAkaniCore core;
    private WarpManager warpManager;
    private LiteCommands<CommandSender> commands;
    private Gson gson;

    @Override
    public void onEnable() {
        initClasses();
        commands = LiteCommandsBukkit.builder("essentials", this)
                .argument(AkaniPlayer.class, new OnlinePlayerResolver<>(core))
                .argument(AkaniOfflinePlayer.class, new OfflinePlayerResolver<>(core))
                .argument(Warp.class, new WarpResolver(warpManager))
                .argument(GameMode.class, new GamemodeResolver())
                .message(LiteBukkitMessages.MISSING_PERMISSIONS, input -> "Keine Rechte.")
                .invalidUsage(new InvalidUsageCommandHandler())
                .commands(
                        new TeleportCommand(core),
                        new WarpCommand(this)
                )
                .build();
        commands.register();
    }

    public MiniMessage miniMessage() {
        return miniMessage;
    }

    public PaperAkaniCore core() {
        return core;
    }

    public WarpManager warpManager() {
        return warpManager;
    }

    public LiteCommands<CommandSender> commands() {
        return commands;
    }

    public Gson gson() {
        return gson;
    }

    private void initClasses() {
        core = (PaperAkaniCore) AkaniCoreProvider.get();
        core.registerMessageProvider(new EssentialsMessageProvider());
        gson = new Gson();
        warpManager = new WarpManager(this);
        warpManager.load();
        new ChatListener(this);

    }

    @Override
    public void onDisable() {
        if (commands != null) commands.unregister();
    }
}
