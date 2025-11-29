package rbtree.manhunt;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnPlayerJump implements Listener {
    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event){
        if(!(Manhunt.gamePhase == 1 && Manhunt.gameSeconds <= Manhunt.huntersBlindTime)) return;
        if(IdentityUtils.isRunner(event.getPlayer())) return;

        event.getPlayer().sendMessage("在失明期间不能跳跃！");
        event.setCancelled(true);
    }
}
