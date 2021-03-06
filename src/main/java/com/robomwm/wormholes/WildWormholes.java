package com.robomwm.wormholes;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created on 5/29/2017.
 *
 * @author RoboMWM
 */
public class WildWormholes extends JavaPlugin
{
    private Thera thera;

    //TODO: Store and cleanup old wormholes (in case of server crash)
    public void onEnable()
    {
        getConfig().options().header(
                        "maxRadius determines the default radius from the world's spawn where a wormhole may spawn. Set to -1 to use the worldborder instead." +
                        "\ncustomMaxRadius allows you to override the maxRadius for a specific world."
        );
        getConfig().addDefault("blacklistedWorlds", Collections.singletonList("spawn"));
        getConfig().addDefault("maxRadius", -1);
        getConfig().addDefault("customMaxRadius", Collections.singletonMap("space", 5000));
        getConfig().options().copyDefaults(true);
        saveConfig();

        Map<String, Integer> customMaxRadiusWorlds = new HashMap<>();
        for (String worldKey : getConfig().getConfigurationSection("customMaxRadius").getKeys(false))
            customMaxRadiusWorlds.put(worldKey, getConfig().getConfigurationSection("customMaxRadius").getInt(worldKey));

        thera = new Thera(this);
        new WormholeSpawner(this, thera,
                new HashSet<>(getConfig().getStringList("blacklistedWorlds")),
                getConfig().getInt("maxRadius"),
                customMaxRadiusWorlds);
        new WormholeTransporter(this, thera);
        try
        {
            new Metrics(this, 3412).addCustomChart(new Metrics.SimplePie("bukkit_implementation", new Callable<String>()
            {
                @Override
                public String call() throws Exception
                {
                    return getServer().getVersion().split("-")[1];
                }
            }));
        }
        catch (Throwable ignored){}
    }

    public void onDisable()
    {
        getLogger().info("Destroying " + thera.destroyAllWormholes() + " wormholes.");
    }
}
