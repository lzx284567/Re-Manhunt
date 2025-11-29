package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class OnPlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Boolean keepInventory = Manhunt.deathKeepInventoryMap.get(uuid);
        Manhunt.deathKeepInventoryMap.remove(uuid);

        if (keepInventory == null) {
            player.sendMessage("[Manhunt] 插件错误：无法获取死亡世界信息!");
            return;
        }

        if(!keepInventory && IdentityUtils.isHunter(player)){
            event.getPlayer().getInventory().setItem(8,CompassUtils.getTrackingCompass());
        }

        if(Manhunt.gamePhase == 2){
            World voidWorld = Bukkit.getWorld(Config.getEndingRoomName());
            if(Config.getIfEnableLobby()){
                if(voidWorld == null){
                    Manhunt.getManhunt().getLogger().severe("无法找到大厅维度，请您检查 config.yml！");
                }else{
                    event.getPlayer().teleport(new Location(voidWorld, Config.getEndingRoomPosX(),Config.getEndingRoomPosY(),Config.getEndingRoomPosZ()));
                }
            }

            event.getPlayer().getInventory().clear();
            event.getPlayer().getActivePotionEffects().clear();
        }
    }
}
