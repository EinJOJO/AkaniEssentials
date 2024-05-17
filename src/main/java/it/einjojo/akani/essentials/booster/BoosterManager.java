package it.einjojo.akani.essentials.booster;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.essentials.booster.impl.Break3x3Booster;
import it.einjojo.akani.essentials.booster.impl.FlyBooster;
import it.einjojo.akani.essentials.booster.impl.HasteBooster;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class BoosterManager implements Runnable {
    private final List<Booster> boosters = new LinkedList<>();

    public BoosterManager(JavaPlugin plugin) {
        register(new FlyBooster());
        register(new HasteBooster());
        register(new Break3x3Booster(plugin));
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 10);
    }

    public List<Booster> boosters() {
        return ImmutableList.copyOf(boosters);
    }

    public <T> T booster(Class<T> boosterClass) {
        for (Booster booster : boosters) {
            if (boosterClass.isInstance(booster)) {
                return boosterClass.cast(booster);
            }
        }
        return null;
    }

    private void register(Booster booster) {
        boosters.add(booster);
    }


    @Override
    public void run() {
        for (Booster booster : boosters) {
            booster.tick();
        }
    }
}
