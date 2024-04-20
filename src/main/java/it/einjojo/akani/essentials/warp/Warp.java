package it.einjojo.akani.essentials.warp;

import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public record Warp(String name, WarpIcon icon, NetworkLocation networkLocation) {

    /**
     * Warps the player to the network location
     * @param player the player to warp
     */
    public void warp(AkaniPlayer player) {
        player.teleport(networkLocation);
    }

}
