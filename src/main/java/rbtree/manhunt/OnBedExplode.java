package rbtree.manhunt;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnBedExplode implements Listener {
    @EventHandler
    public void onBedExplode(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().name().endsWith("_BED")) return;

        World world = event.getPlayer().getWorld();
        if (world.getEnvironment() == World.Environment.NETHER || world.getEnvironment() == World.Environment.THE_END) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c§l床爆炸被房主禁用！");
        }
    }
}