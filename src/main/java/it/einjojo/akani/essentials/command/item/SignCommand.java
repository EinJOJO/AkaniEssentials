package it.einjojo.akani.essentials.command.item;

import co.aikar.commands.BaseCommand;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;

public class SignCommand extends BaseCommand {

    private final AkaniEssentialsPlugin plugin;

    public SignCommand(AkaniEssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.commandManager().registerCommand(this);
    }



}
