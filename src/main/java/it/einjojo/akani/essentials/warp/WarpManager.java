package it.einjojo.akani.essentials.warp;


import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import it.einjojo.akani.essentials.AkaniEssentialsPlugin;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Warps are made by the admin and can be used by the players to teleport to a specific location
 */
public class WarpManager {
    private final Map<String, Warp> warps = new ConcurrentHashMap<>();
    private final WarpStorage storage;
    private final AkaniEssentialsPlugin plugin;

    public WarpManager(AkaniEssentialsPlugin plugin) {
        storage = new WarpStorage(plugin.core().dataSource(), plugin.gson());
        storage.init();
        this.plugin = plugin;
    }

    public void load() {
        warps.clear();
        for (Warp warp : storage.loadWarps()) {
            warps.put(warp.name(), warp);
        }
    }


    public Warp warp(String name) {
        return warps.get(name);
    }


    public Set<String> warpNames() {
        return Collections.unmodifiableSet(warps.keySet());
    }


    public Collection<Warp> warps() {
        return Collections.unmodifiableCollection(warps.values());
    }

    public void deleteWarp(Warp warp) {
        warps.remove(warp.name());
        storage.deleteWarp(warp.name());
    }

    public void updateWarp(Warp warp) {
        warps.put(warp.name(), warp);
        storage.updateWarp(warp);
    }

    public Warp createWarp(String name, Location location, NetworkLocation.Type type) {
        var warp = new Warp(
                name,
                WarpIcon.DEFAULT,
                AkaniBukkitAdapter.networkLocation(location).type(type)
                        .referenceName(type == NetworkLocation.Type.SERVER ? core().brokerService().brokerName() :
                                type == NetworkLocation.Type.GROUP ? core().brokerService().groupName() : null)
                        .build());
        warps.put(name, warp);
        storage.createWarp(warp);
        return warp;
    }

    protected AkaniCore core() {
        return plugin.core();
    }
}
