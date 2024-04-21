package it.einjojo.akani.essentials.command.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import it.einjojo.akani.essentials.warp.Warp;
import it.einjojo.akani.essentials.warp.WarpManager;
import org.bukkit.command.CommandSender;

public class WarpResolver extends ArgumentResolver<CommandSender, Warp> {

    private final WarpManager warpManager;

    public WarpResolver(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    protected ParseResult<Warp> parse(Invocation<CommandSender> invocation, Argument<Warp> context, String argument) {
        Warp warp = warpManager.warp(argument);
        if (warp == null) {
            return ParseResult.failure("warp not found");
        }
        return ParseResult.success(warp);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Warp> argument, SuggestionContext context) {
        return warpManager.warpNames().stream().collect(SuggestionResult.collector());
    }
}
