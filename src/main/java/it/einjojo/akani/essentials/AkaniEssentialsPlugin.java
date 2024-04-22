package it.einjojo.akani.essentials;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.essentials.command.GamemodeCommand;
import it.einjojo.akani.essentials.command.TeleportCommand;
import it.einjojo.akani.essentials.command.WarpCommand;
import it.einjojo.akani.essentials.listener.ChatListener;
import it.einjojo.akani.essentials.util.EssentialsMessageProvider;
import it.einjojo.akani.essentials.warp.WarpManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class AkaniEssentialsPlugin extends JavaPlugin {
    public static final String PERMISSION_BASE = "akani.essentials.";
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private PaperAkaniCore core;
    private WarpManager warpManager;
    private PaperCommandManager commandManager;
    private Gson gson;

    @Override
    public void onEnable() {
        try {
            initClasses();
            registerCommands();
        } catch (Exception e) {
            getLogger().severe("Error while enabling AkaniEssentials");
            getLogger().severe(e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public PaperCommandManager commandManager() {
        return commandManager;
    }

    private void registerCommands() {
        getLogger().info("Registering commands");
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.getCommandCompletions().registerAsyncCompletion("warps", c -> warpManager.warpNames());
        // all online players except the sender
        commandManager.getCommandCompletions().registerAsyncCompletion("akaniplayers", c -> {
            boolean includeSender = c.hasConfig("includeSender");
            getLogger().info("Include sender: " + includeSender);
            return core().playerManager().onlinePlayers().stream().map(AkaniPlayer::name).filter(name -> includeSender || !(name.equals(c.getSender().getName()))).toList();
        });
        commandManager.getCommandContexts().registerContext(AkaniPlayer.class, c -> core().playerManager().onlinePlayerByName(c.popFirstArg()).orElse(null));
        //register commands
        new TeleportCommand(this);
        new WarpCommand(this);
        new GamemodeCommand(this);

    }


    /**
     * Sends a translated message
     *
     * @param sender the receiver of the message
     * @param key    the key of the message
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String key) {
        sendMessage(sender, key, null);
    }

    /**
     * Sends a translated message
     *
     * @param sender   the receiver of the message
     * @param key      the key of the message
     * @param modifier a function that modifies the message before its sent
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String key, @Nullable Function<String, String> modifier) {
        core().messageManager().sendMessage(sender, key, modifier);
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


    public Gson gson() {
        return gson;
    }

    private void initClasses() {
        core = (PaperAkaniCore) AkaniCoreProvider.get();
        core.registerMessageProvider(new EssentialsMessageProvider());
        core.delayedMessageReload();
        gson = new Gson();
        warpManager = new WarpManager(this);
        warpManager.load();
        new ChatListener(this);

    }

    @Override
    public void onDisable() {

    }
}
