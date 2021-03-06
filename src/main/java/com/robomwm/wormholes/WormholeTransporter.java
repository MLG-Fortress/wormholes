package com.robomwm.wormholes;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 5/29/2017.
 *
 * @author RoboMWM
 */
public class WormholeTransporter implements Listener
{
    Thera thera;

    public WormholeTransporter(JavaPlugin plugin, Thera thera)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.thera = thera;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerTeleportedByWormhole(PlayerTeleportEvent event)
    {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_GATEWAY)
            return;

        Wormhole wormhole = thera.getWormhole(event.getTo());
        if (wormhole == null)
            return;

        //Ensure the other side isn't obstructed
        wormhole.getOtherSide().build(true);

        event.getPlayer().teleport(wormhole.getOtherSide().getLocation().add(0.5, -2, 0.5));

        event.setCancelled(true);

        String message;

        //TODO: fire WormholeTeleportEvent
        switch (wormhole.getOtherSide().getLocation().getWorld().getName())
        {
            case "world":
            case "cityworld":
                message = ChatColor.DARK_AQUA + "You are sucked through the wormhole into a familiar world.";
                break;
            case "world_nether":
            case "cityworld_nether":
                message = ChatColor.DARK_RED + "You are sucked through the wormhole into a not-so-friendly-looking world.";
                break;
            default:
                message = ChatColor.GRAY + "You are sucked through the wormhole into an unknown world.";
        }

        event.getPlayer().sendMessage(message);
    }


    //End_Gateway block does not fire this event...

//    @EventHandler(priority = EventPriority.LOWEST)
//    void onEnterWormhole(EntityPortalEnterEvent event)
//    {
//        Wormhole wormhole = thera.getWormhole(event.getLocation());
//        if (wormhole == null)
//            return;
//
//        //Ensure the other side isn't obstructed
//        wormhole.getOtherSide().build();
//
//        event.getEntity().teleport(wormhole.getOtherSide().getLocation().add(0.5, -2, 0.5));
//
//        //TODO: fire WormholeTeleportEvent
//
//        /*
//        //Currently teleports player to a random spot on the perimeter of the wormhole...
//        Location location = wormhole.getOtherSide().getLocation();
//
//        int randomX = 3;
//        int randomZ = 3;
//
//        if (ThreadLocalRandom.current().nextBoolean())
//            randomX = r4nd0m(-3, 3);
//        else
//            randomZ = r4nd0m(-3, 3);
//
//        if (ThreadLocalRandom.current().nextBoolean())
//        {
//            randomX = -randomX;
//            randomZ = -randomZ;
//        }
//
//        location.add(randomX, 1, randomZ);
//        event.getEntity().teleport(location);
//        */
//    }

}
