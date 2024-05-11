package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;


@CommandAlias("enderchest|ec")
public class EnderchestCommand extends BaseCommand {


    @Default
    public void onEnderchest(Player sender) {
        sender.openInventory(sender.getEnderChest());
    }

}
