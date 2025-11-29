package rbtree.manhunt;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnChangeTracking implements Listener {
    @EventHandler
    public static void onChangeTracking(PlayerInteractEvent event){
        Player eventPlayer = event.getPlayer();
        if(IdentityUtils.isRunner(eventPlayer)) return;
        if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
            return;
        }
        ItemStack item = event.getItem();
        if(item == null || item.isEmpty()) return;
        if(!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey trackingKey = new NamespacedKey(Manhunt.getManhunt(), "tracking_compass");
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.has(trackingKey, PersistentDataType.BYTE)){
            TrackingUtils.nextTracking(event.getPlayer());
        }
    }

}
