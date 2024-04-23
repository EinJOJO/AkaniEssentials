package it.einjojo.akani.essentials.command.economy;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("pay")
public class PayCommand {


    @Default
    @CommandCompletion("@akaniplayers 1")
    @Syntax("<player> <amount>")
    public void pay(Player sender, AkaniOfflinePlayer target, double amount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
