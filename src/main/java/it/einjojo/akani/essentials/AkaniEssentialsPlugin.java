package it.einjojo.akani.essentials;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.essentials.command.AkaniPlayerCommand;
import it.einjojo.akani.essentials.command.TeleportCommand;
import it.einjojo.akani.essentials.command.WarpCommand;
import it.einjojo.akani.essentials.listener.ChatListener;
import it.einjojo.akani.essentials.util.EssentialsMessageProvider;
import it.einjojo.akani.essentials.warp.WarpManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.paper.PaperCommandManager;

public class AkaniEssentialsPlugin extends JavaPlugin {
    public static final String PERMISSION_BASE = "akani.essentials.";
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private PaperAkaniCore core;
    private WarpManager warpManager;
    private PaperCommandManager<CommandSender> commandManager;
    private Gson gson;

    @Override
    public void onEnable() {
        initClasses();
        registerCommands();
    }

    private void registerCommands() {
        commandManager = new PaperCommandManager<>(
                this,
                ExecutionCoordinator.asyncCoordinator(),
                SenderMapper.identity()
        );
        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier();
        } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
        }
        MinecraftExceptionHandler.<CommandSender>create(sender -> sender)
                .defaultInvalidSenderHandler()
                .defaultInvalidSyntaxHandler()
                .defaultNoPermissionHandler()
                .defaultArgumentParsingHandler()
                .defaultCommandExecutionHandler()
                .decorator(
                        component -> core.messageManager().message("prefix")
                ).registerTo(commandManager);

        AnnotationParser<CommandSender> parser = new AnnotationParser<>(commandManager, CommandSender.class);
        parser.parse(new AkaniPlayerCommand(this));
        parser.parse(new WarpCommand(this));
        parser.parse(new TeleportCommand(core()));


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

    }

    @Override
    public void onDisable() {

    }
}
