package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class OnPotionArrowShoot implements Listener {
    @EventHandler
    public void onPotionArrowShoot(EntityShootBowEvent event){
        if(!Manhunt.disablePotionArrow) return;
        Entity projectile = event.getProjectile();
        LivingEntity shooter = event.getEntity();

        if(shooter instanceof Player && projectile instanceof AbstractArrow){
            if(((AbstractArrow) projectile).getItemStack().getType().equals(Material.TIPPED_ARROW)){
                shooter.sendMessage("§c§l药水箭已被房主禁用！");
                event.setCancelled(true);
            }
        }
    }
}
