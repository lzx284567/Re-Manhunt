package rbtree.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CompassUtils {
    static public ItemStack getTrackingCompass(){
        ItemStack trackcompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = trackcompass.getItemMeta();

        Component message = Component.text("追踪指南针");
        Component lore1 = Component.text("滴——滴——滴—滴—滴—滴滴滴滴滴滴滴滴").color(NamedTextColor.WHITE);
        Component lore2 = Component.text("右键点击以切换追踪者！（队友也可以追踪哦！）").color(NamedTextColor.GOLD);
        List<Component> loreList = new ArrayList<>();
        loreList.add(lore1);
        loreList.add(lore2);

        meta.lore(loreList);
        meta.displayName(message);

        NamespacedKey trackingKey = new NamespacedKey(Manhunt.getManhunt(), "tracking_compass");
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(trackingKey, PersistentDataType.BYTE,(byte) 1);

        trackcompass.setItemMeta(meta);
        trackcompass.addEnchantment(Enchantment.VANISHING_CURSE,1);
        return trackcompass;
    }
    static public void removeTrackingCompass(Player player){
        Inventory inv = player.getInventory();
        for(int slot=0; slot<=40; slot++){
            ItemStack item = inv.getItem(slot);
            if(item == null || item.isEmpty()) continue;
            if (!item.hasItemMeta()) continue;

            ItemMeta meta = item.getItemMeta();
            NamespacedKey trackingKey = new NamespacedKey(Manhunt.getManhunt(), "tracking_compass");
            PersistentDataContainer pdc = meta.getPersistentDataContainer();

            if(pdc.has(trackingKey, PersistentDataType.BYTE)){
                //如检查到带标记的指南针
                inv.clear(slot);
            }
        }
    }
    public static void updateCompass(Player player){
        PlayerInventory inv = player.getInventory();
        if(Manhunt.onlinePlayersUUID.size() > 1 && IdentityUtils.isHunter(player)){
            //当存在其他人且检查的玩家是猎人
            for(int slot = 0; slot <= 40; slot++){
                ItemStack item = inv.getItem(slot);
                if(item == null || item.isEmpty()) continue;
                if (!item.hasItemMeta()) continue;

                ItemMeta meta = item.getItemMeta();
                NamespacedKey trackingKey = new NamespacedKey(Manhunt.getManhunt(), "tracking_compass");
                PersistentDataContainer pdc = meta.getPersistentDataContainer();

                if(pdc.has(trackingKey, PersistentDataType.BYTE)){
                    //如检查到带标记的指南针
                    Player trackedplayer = Bukkit.getPlayer(Manhunt.tracking.get(player.getUniqueId()));
                    if(trackedplayer != null){
                        Location trackedlocation = trackedplayer.getLocation();
                        CompassMeta newMeta = (CompassMeta) meta;
                        newMeta.setLodestoneTracked(true);
                        newMeta.setLodestone(getRelativeLocation(player.getLocation(),trackedlocation));
                        item.setItemMeta(newMeta);
                    }
                    return;
                }
            }
        }
    }
    public static void addTrackingCompass(Player player){
        boolean noCompass=true;
        PlayerInventory inv = player.getInventory();
        for(int slot = 0; slot <= 40; slot++){
            ItemStack item = inv.getItem(slot);
            if(item == null || item.isEmpty()) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

            NamespacedKey trackingKey = new NamespacedKey(Manhunt.getManhunt(), "tracking_compass");
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if(pdc.has(trackingKey, PersistentDataType.BYTE)){
                noCompass=false;
                break;
            }
        }
        if(noCompass){
            ItemStack track_compass = CompassUtils.getTrackingCompass();
            inv.addItem(track_compass);
        }
    }
    private static Location getRelativeLocation(Location userPos,Location trackingPos){
        double x1=userPos.x();
        double z1=userPos.z();
        double x2=trackingPos.x();
        double z2=trackingPos.z();
        if(x2-x1==0){
            return new Location(trackingPos.getWorld(),x1,64.0,z1+100);
        }

        double k=(z2-z1)/(x2-x1);
        return new Location(trackingPos.getWorld(),x1+100,64.0,z1+100*k);
    }
}
