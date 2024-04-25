package it.einjojo.akani.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("give")
public class GiveCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public GiveCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().getCommandCompletions().registerStaticCompletion("items", Arrays.stream(Material.values()).map(Enum::name).toList());
        plugin.commandManager().registerCommand(this);
    }

    @Default
    @CommandCompletion("@players|@items ")
    public void giveItem(Player sender, String[] args) {

    }


    public void giveOraxenItem(Player player, String itemId, int amount) {

    }

}
