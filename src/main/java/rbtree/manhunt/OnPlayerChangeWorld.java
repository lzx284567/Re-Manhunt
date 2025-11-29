package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class OnPlayerChangeWorld implements Listener {
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event){
        if(LifeUtils.ifDeath(event.getPlayer().getName())){
            Bukkit.getScheduler().runTask(Manhunt.getManhunt(),()-> event.getPlayer().setGameMode(GameMode.SPECTATOR));
        }
    }
}
