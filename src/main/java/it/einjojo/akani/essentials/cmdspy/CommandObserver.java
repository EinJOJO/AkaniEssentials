package it.einjojo.akani.essentials.cmdspy;

import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface CommandObserver {
    UUID observer();

    AkaniEssentialsPlugin plugin();

    void onCommand(Player commandSender, String command);

    record All(AkaniEssentialsPlugin plugin, UUID observer) implements CommandObserver {

        @Override
        public void onCommand(Player commandSender, String command) {

        }
    }


    record Target(AkaniEssentialsPlugin plugin, UUID observer,
                                 UUID target) implements CommandObserver {
        @Override
        public void onCommand(Player commandSender, String command) {
            if (!target.equals(commandSender.getUniqueId())) {
                return;
            }
        }
    }

}