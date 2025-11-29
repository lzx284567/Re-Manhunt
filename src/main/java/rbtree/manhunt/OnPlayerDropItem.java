package rbtree.manhunt;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnPlayerDropItem implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if(event.getItemDrop().getItemStack().hasItemMeta()){
            ItemMeta meta = event.getItemDrop().getItemStack().getItemMeta();
            NamespacedKey trackingKey = new NamespacedKey(Manhunt.getManhunt(), "tracking_compass");
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if(pdc.has(trackingKey, PersistentDataType.BYTE)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§c§l追踪指南针不允许扔掉哦！");
            }
        }
    }
}
