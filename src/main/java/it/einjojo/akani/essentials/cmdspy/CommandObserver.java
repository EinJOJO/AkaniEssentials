package it.einjojo.akani.essentials.cmdspy;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import it.einjojo.akani.essentials.util.EssentialKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface CommandObserver {
    UUID observer();

    default Optional<Player> observerPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(observer()));
    }

    AkaniEssentialsPlugin plugin();

    void onCommand(Player commandSender, String command);

    record All(AkaniEssentialsPlugin plugin, UUID observer) implements CommandObserver {

        @Override
        public void onCommand(Player commandSender, String command) {
            observerPlayer().ifPresent((player) -> {
                plugin.sendMessage(player, EssentialKey.of("cmdspy.format"), s -> s.replaceAll("%player%", commandSender.getName()).replaceAll("%command%", command));
            });
        }
    }


    record Target(AkaniEssentialsPlugin plugin, UUID observer,
                  UUID target) implements CommandObserver {
        @Override
        public void onCommand(Player commandSender, String command) {
            if (!target.equals(commandSender.getUniqueId())) {
                return;
            }
            observerPlayer().ifPresent((player) -> {
                plugin.sendMessage(player, EssentialKey.of("cmdspy.format"), s -> s.replaceAll("%player%", commandSender.getName()).replaceAll("%command%", command));
            });

        }
    }

}