package it.einjojo.akani.essentials;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.core.paper.player.PaperAkaniPlayer;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardManager;
import it.einjojo.akani.essentials.cmdspy.CommandObserverRegistry;
import it.einjojo.akani.essentials.command.*;
import it.einjojo.akani.essentials.command.economy.MoneyCommand;
import it.einjojo.akani.essentials.command.economy.PayCommand;
import it.einjojo.akani.essentials.command.economy.RubyCommand;
import it.einjojo.akani.essentials.command.item.GiveCommand;
import it.einjojo.akani.essentials.command.item.HatCommand;
import it.einjojo.akani.essentials.command.item.RenameCommand;
import it.einjojo.akani.essentials.command.item.SignCommand;
import it.einjojo.akani.essentials.command.msg.MsgCommand;
import it.einjojo.akani.essentials.command.msg.ReplyCommand;
import it.einjojo.akani.essentials.command.msg.SocialSpyCommand;
import it.einjojo.akani.essentials.emoji.EmojiManager;
import it.einjojo.akani.essentials.emoji.EmojiMessageProcessor;
import it.einjojo.akani.essentials.listener.ChatListener;
import it.einjojo.akani.essentials.listener.CommandSpyListener;
import it.einjojo.akani.essentials.listener.MessageListener;
import it.einjojo.akani.essentials.service.MessageService;
import it.einjojo.akani.essentials.service.TpaService;
import it.einjojo.akani.essentials.util.EmojiConfig;
import it.einjojo.akani.essentials.util.EssentialKey;
import it.einjojo.akani.essentials.util.EssentialsConfig;
import it.einjojo.akani.essentials.util.EssentialsMessageProvider;
import it.einjojo.akani.essentials.warp.WarpManager;
import mc.obliviate.inventory.InventoryAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public class AkaniEssentialsPlugin extends JavaPlugin {
    public static final String PERMISSION_BASE = "akani.essentials.";
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private PaperAkaniCore core;
    private WarpManager warpManager;
    private PaperCommandManager commandManager;

    private EmojiManager emojiManager;
    private MessageService messageService;
    private TpaService tpaService;
    private Gson gson;
    private ScoreboardManager scoreboardManager;
    private EssentialsConfig config;


    @Override
    public void onEnable() {
        new InventoryAPI(this).init();
        try {
            config = new EssentialsConfig(this);
            core = (PaperAkaniCore) AkaniCoreProvider.get();
            core.registerMessageProvider(new EssentialsMessageProvider());
            gson = new Gson();
            warpManager = new WarpManager(this);
            warpManager.load();
            //services
            tpaService = new TpaService(core.jedisPool());
            messageService = new MessageService(core().brokerService(), this, core().jedisPool(), core().tagManager());
            CommandObserverRegistry commandObserverRegistry = new CommandObserverRegistry(this);


            RegisteredServiceProvider<ScoreboardManager> scoreboardManagerProvider = Bukkit.getServicesManager().getRegistration(ScoreboardManager.class);
            if (scoreboardManagerProvider != null) {
                scoreboardManager = scoreboardManagerProvider.getProvider();
            } else {
                getLogger().severe("ScoreboardManager not found");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }


            // PAPI
            new EssentialsPlaceholderExpansion(this).register();

            // emoji
            emojiManager = new EmojiManager(new EmojiConfig(this));
            emojiManager.load();

            // Listener
            new ChatListener(this, new EmojiMessageProcessor(emojiManager));
            new MessageListener(this);
            new CommandSpyListener(this, commandObserverRegistry);
            //new BlockThrower(this);


            // commands
            getLogger().info("Registering commands");
            commandManager = new PaperCommandManager(this);
            commandManager.enableUnstableAPI("help");
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
                CommandSender commandSender = sender.getIssuer();
                if (t instanceof TargetNotFoundException) {
                    sendMessage(commandSender, EssentialKey.PLAYER_NOT_FOUND);
                    return true;
                }
                if (t instanceof NoSuchElementException) {
                    commandSender.sendMessage("§cEingabe nicht gefunden");
                    return true;
                }
                if (t instanceof BadBalanceException ex) {
                    if (ex.type().equals(BadBalanceException.Type.NOT_ENOUGH_FUNDS)) {
                        sendMessage(commandSender, EssentialKey.NOT_ENOUGH_COINS);
                        return true;
                    } else {
                        sendMessage(commandSender, EssentialKey.of("coins.bad-value"));
                    }
                }
                getSLF4JLogger().error("Error while executing command {} {}", registeredCommand.getCommand(), String.join(" ", args), t);

                sendMessage(commandSender, EssentialKey.GENERIC_ERROR);
                return true;
            }, false);

            commandManager.getCommandContexts().registerContext(AkaniOfflinePlayer.class, c -> {
                String s = c.popFirstArg();
                return core().playerManager().loadPlayerByName(s).join().orElseThrow(() -> new TargetNotFoundException(s));

            });
            commandManager.getCommandContexts().registerContext(AkaniPlayer.class, c -> {
                String s = c.popFirstArg();
                return core().playerManager().onlinePlayerByName(s).orElseThrow(() -> new TargetNotFoundException(s));

            });
            commandManager.getCommandContexts().registerContext(PaperAkaniPlayer.class, c -> {
                String s = c.popFirstArg();
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
            new RubyCommand(this);
            new PayCommand(this);
            new AkaniAdminCommand(this);
            new BackCommand(this);
            new GiveCommand(this);
            new BackCommand(this);
            new ServerCommand(this);
            new MsgCommand(this);
            new ReplyCommand(this);
            new SocialSpyCommand(this);
            new InvseeCommand(this);
            new TrashCommand(this);
            new EnderChestCommand(this);
            new CommandSpyCommand(this, commandObserverRegistry);
            new HatCommand(this);
            new SignCommand(this);
            new RenameCommand(this);
            new TpaCommand(this);
            new HomeCommand(this);
            new EmojiCommand(this);
            if (Bukkit.getServer().getPluginManager().getPlugin("PlotSquared") == null) {
                new PlotSquaredCommand(this);
            }
        } catch (Exception e) {
            getLogger().severe("Error while enabling AkaniEssentials");
            getLogger().severe(e.getMessage());
            e.fillInStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
            player.kick();
        }
    }

    public TpaService tpaService() {
        return tpaService;
    }

    public ScoreboardManager scoreboardManager() {
        return scoreboardManager;
    }

    // Utility

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

    public void sendMessage(@NotNull AkaniPlayer player, @NotNull String key) {
        core().messageManager().sendMessage(player, key);
    }

    public void sendMessage(@NotNull AkaniPlayer player, @NotNull String key, @Nullable Function<String, String> modifier) {
        core().messageManager().sendMessage(player, key, modifier);
    }

    public void sendCommandUsageMessage(@NotNull CommandSender sender, @NotNull String syntax) {
        sender.sendMessage(Component.text("[TEMP-DEV] Usage: ").append(Component.text(syntax)));
    }

    // Getters
    public PaperCommandManager commandManager() {
        return commandManager;
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


    public MessageService messageService() {
        return messageService;
    }

    public EssentialsConfig config() {
        return config;
    }


    public EmojiManager emojiManager() {
        return emojiManager;
    }
}
