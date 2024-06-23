package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.emoji.Emoji;
import it.einjojo.akani.essentials.emoji.EmojiManager;
import it.einjojo.akani.essentials.emoji.EmojiRarity;
import it.einjojo.akani.essentials.emoji.gui.SendEmojiGui;
import it.einjojo.akani.essentials.service.MessageService;
import it.einjojo.akani.essentials.util.EmojiConfig;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@CommandAlias("emoji|emojis")
public class EmojiCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public EmojiCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandContexts().registerContext(EmojiRarity.class, (c) -> {
            try {
                return EmojiRarity.valueOf(c.popFirstArg().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new NoSuchElementException("Invalid rarity");
            }
        });
        plugin.commandManager().getCommandCompletions().registerStaticCompletion("emojiRarity", Stream.of(EmojiRarity.values()).map(Enum::name).toList());
        plugin.commandManager().registerCommand(this);
    }

    @Default
    public void openEmojiSendGui(Player sender) {
        new SendEmojiGui(sender, emojiManager(), messageService()).open();
    }

    @Subcommand("reload")
    @Description("Reloads the emoji configuration")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "emoji.reload")
    public void reloadEmojis(Player sender) {
        emojiManager().load();
        plugin.sendMessage(sender, EssentialKey.of("emoji.reload"));
    }


    @Subcommand("save")
    @Description("Saves the emoji configuration")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "emoji.save")

    public void saveEmojis(Player sender) {
        emojiManager().save();
        plugin.sendMessage(sender, EssentialKey.of("emoji.save"));
    }

    @Subcommand("create")
    @Description("Create a new emoji")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "emoji.create")
    @CommandCompletion("<name> <emoji> @emojiRarity <aliases> <permission>|chat.emoji. <customModelData>")
    public void createEmoji(Player sender, @Single String name, @Single String emoji, @Values("@emojiRarity") EmojiRarity rarity, @Split(",") String[] aliases, @Single String permission, int customModelData) {
        emojiManager().addEmoji(new Emoji(name, permission, aliases, rarity, emoji, customModelData));
        plugin.sendMessage(sender, EssentialKey.of("emoji.create"), (s) -> s.replace("%emojiName%", name)
                .replace("%rarity%", rarity.name())
                .replace("%permission%", permission)
                .replace("%customModelData%", String.valueOf(customModelData))
                .replace("%aliases%", Arrays.toString(aliases))
                .replace("%emoji%", emoji));
    }

    @Subcommand("remove")
    @Description("Remove an emoji")
    @CommandPermission(AkaniEssentialsPlugin.PERMISSION_BASE + "emoji.remove")
    public void removeEmoji(Player sender, String name) {
        Emoji emoji = emojiManager().emoji(name);
        if (emoji == null) {
            plugin.sendMessage(sender, EssentialKey.of("emoji.notFound"));
            return;
        }
        emojiManager().removeEmoji(emoji);
        plugin.sendMessage(sender, EssentialKey.of("emoji.remove"), (s) -> s.replaceAll("%emoji%", name));
    }


    public EmojiManager emojiManager() {
        return plugin.emojiManager();
    }

    public MessageService messageService() {
        return plugin.messageService();
    }


}
