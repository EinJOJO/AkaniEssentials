package it.einjojo.akani.essentials.command;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.command.CommandSender;

public class InvalidUsageCommandHandler implements InvalidUsageHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();


        sender.sendMessage(AkaniEssentialsPlugin.miniMessage.deserialize("<red>Invalid usage of command!"));
        for (String scheme : schematic.all()) {
            sender.sendMessage(AkaniEssentialsPlugin.miniMessage.deserialize("<dark_gray> - <yellow>" + scheme));
        }
    }
}
