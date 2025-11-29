package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static rbtree.manhunt.GameEnd.gameEnd;

public class OnPlayerDie implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDie(PlayerDeathEvent event){
        if(Manhunt.gamePhase != 1) return;

        Player deathPlayer = event.getPlayer();
        String playerName = event.getPlayer().getName();
        LifeUtils.decreaseLife(playerName);
        World deathWorld = deathPlayer.getWorld();
        Manhunt.deathKeepInventoryMap.put(deathPlayer.getUniqueId(), deathWorld.getGameRuleValue(GameRule.KEEP_INVENTORY));

        if(!IdentityUtils.isRunner(deathPlayer)) return;
        boolean flag = true;
        for(Player player : Bukkit.getOnlinePlayers()){
            String name = player.getName();
            if(IdentityUtils.isRunner(player) && !LifeUtils.ifDeath(name)) {
                flag = false;
                break;
            }
        }
        if(flag){
            //如所有跑者都死了
            gameEnd("hunter",event.getPlayer().getName());
        }


    }
}
