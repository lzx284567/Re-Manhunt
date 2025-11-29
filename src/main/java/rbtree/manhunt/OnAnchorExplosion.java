package rbtree.manhunt;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnAnchorExplosion implements Listener {
    @EventHandler
    public void onAncherExplosion (PlayerInteractEvent event){
        if(!Manhunt.disableAnchor) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR)) return;

        World world = event.getPlayer().getWorld();
        if (world.getEnvironment() == World.Environment.NORMAL || world.getEnvironment() == World.Environment.THE_END) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c§l重生锚爆炸被房主禁用！");
        }
    }
}
