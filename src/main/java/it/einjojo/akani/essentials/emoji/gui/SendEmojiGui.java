package it.einjojo.akani.essentials.emoji.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.pagination.PaginationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class SendEmojiGui extends Gui {
    private final PaginationManager paginationManager = new PaginationManager(this);
    private SortType sortType = SortType.NAME;

    public SendEmojiGui(@NotNull Player player) {
        super(player, "emojiSelector", "§cSende einen Emoji", 6);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    private enum SortType {
        RARITY,
        NAME
    }
}
