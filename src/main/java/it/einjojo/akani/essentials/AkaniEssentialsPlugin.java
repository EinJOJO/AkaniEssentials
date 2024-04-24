package it.einjojo.akani.essentials;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.core.paper.player.PaperAkaniPlayer;
import it.einjojo.akani.essentials.command.*;
import it.einjojo.akani.essentials.command.admin.AkaniPlayersCommand;
import it.einjojo.akani.essentials.command.economy.MoneyCommand;
import it.einjojo.akani.essentials.command.economy.PayCommand;
import it.einjojo.akani.essentials.command.economy.ThalerCommand;
import it.einjojo.akani.essentials.listener.ChatListener;
import it.einjojo.akani.essentials.listener.MessageCancelListener;
import it.einjojo.akani.essentials.util.EssentialsMessageProvider;
import it.einjojo.akani.essentials.util.MessageKey;
import it.einjojo.akani.essentials.warp.WarpManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
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
        // all online players except the sender
        commandManager.getCommandCompletions().registerAsyncCompletion("akaniplayers", c -> {
            boolean includeSender = c.hasConfig("includeSender");
            return core().playerManager().onlinePlayers().stream().map(AkaniPlayer::name).filter(name -> includeSender || !(name.equals(c.getSender().getName()))).toList();
        });
        commandManager.getCommandCompletions().registerAsyncCompletion("akaniofflineplayers", c -> {
            int limit = c.hasConfig("limit") ? Integer.parseInt(c.getConfig("limit")) : 10;
            return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(n -> n.toLowerCase().startsWith(c.getInput().toLowerCase()))
                    .limit(limit).toList();
        });
        commandManager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t) -> {
            if (t instanceof TargetNotFoundException) {
                sendMessage(sender.getIssuer(), MessageKey.PLAYER_NOT_FOUND);
                return true;
            }
            getLogger().severe("Error while executing command " + registeredCommand.getCommand() + " " + String.join(" ", args));
            t.printStackTrace();
            return false;
        }, false);

        commandManager.getCommandContexts().registerContext(AkaniOfflinePlayer.class, c -> {
            String s = c.popFirstArg();
            return core().playerManager().loadPlayerByName(s).join().orElseThrow(() -> new TargetNotFoundException(s));

        });
        commandManager.getCommandContexts().registerContext(AkaniPlayer.class, c -> {
            String s = c.popFirstArg();
            System.out.println(c);
            System.out.println("Resolve AkaniPlayer Context: " + s);
            return core().playerManager().onlinePlayerByName(s).orElseThrow(() -> new TargetNotFoundException(s));

        });
        commandManager.getCommandContexts().registerContext(PaperAkaniPlayer.class, c -> {
            String s = c.popFirstArg();
            System.out.println("Resolve PaperAkaniPlayer Context: " + s);
            return (PaperAkaniPlayer) core().playerManager().onlinePlayerByName(s).orElseThrow(() -> new TargetNotFoundException(s));
        });
        //register commands
        new TeleportCommand(this);
        new WarpCommand(this);
        new GamemodeCommand(this);
        new MoneyCommand(this);
        new TimeCommand(this);
        new HealFeedCommand(this);
        new SpeedCommand(this);
        new FlyCommand(this);
        new WorkbenchCommand(this);
        new ThalerCommand(this);
        new PayCommand(this);
        new AkaniPlayersCommand(this);
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

    public void sendCommandUsageMessage(@NotNull CommandSender sender, @NotNull String syntax) {
        sender.sendMessage(Component.text("[TEMP-DEV] Usage: ").append(Component.text(syntax)));
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
        gson = new Gson();
        warpManager = new WarpManager(this);
        warpManager.load();
        new ChatListener(this);
        new MessageCancelListener(this);
    }


    @Override
    public void onDisable() {

    }
}
