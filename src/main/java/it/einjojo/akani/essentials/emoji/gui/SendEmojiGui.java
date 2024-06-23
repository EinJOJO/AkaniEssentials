package it.einjojo.akani.essentials.emoji.gui;

import it.einjojo.akani.essentials.emoji.Emoji;
import it.einjojo.akani.essentials.emoji.EmojiManager;
import it.einjojo.akani.essentials.emoji.PlayerEmojiContainer;
import it.einjojo.akani.essentials.service.MessageService;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.stream.Stream;

public class SendEmojiGui extends Gui {
    private final PaginationManager paginationManager = new PaginationManager(this);
    private SortType sortType = SortType.NAME_ASC;
    private final EmojiManager emojiManager;
    private final MessageService messageService;
    private final Stream<Emoji> emojiStream;

    public SendEmojiGui(@NotNull Player player, EmojiManager emojiManager, MessageService messageService) {
        super(player, "emojiSelector", "§cSende einen Emoji", 6);
        this.emojiManager = emojiManager;
        this.messageService = messageService;
        paginationManager.registerPageSlots(0, 9 * 5 - 1);
        emojiStream = emojiManager.allEmojis().stream().filter((emoji) -> emoji.isUnlocked(player));
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        paginationManager.getItems().clear();
        (switch (sortType) {
            case RARITY_ASC -> emojiStream.sorted(Comparator.comparing(Emoji::rarity));
            case NAME_ASC -> emojiStream.sorted(Comparator.comparing(Emoji::name));
            case RARITY_DESC -> emojiStream.sorted(Comparator.comparing(Emoji::rarity).reversed());
            case NAME_DESC -> emojiStream.sorted(Comparator.comparing(Emoji::name).reversed());
        }).forEachOrdered(emoji -> {
            paginationManager.addItem(new Icon(emoji.guiItem()).onClick(clickEvent -> {
                onEmojiClick(emoji, clickEvent);
            }));
        });
    }



    public void onEmojiClick(Emoji emoji, InventoryClickEvent event) {
        player.closeInventory();
        messageService.publishPublicChatMessage(player, emoji.emoji());
    }

    private enum SortType {
        RARITY_ASC,
        RARITY_DESC,
        NAME_ASC,
        NAME_DESC
    }
}
