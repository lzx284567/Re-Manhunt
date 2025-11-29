package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static rbtree.manhunt.Manhunt.life;

public class IdentityUtils {
    public static int onlineRunnersCount(){
        int runnersCount=0;
        for(Player player : Bukkit.getOnlinePlayers()){
            UUID uuid = player.getUniqueId();
            if(Manhunt.identity.get(uuid) == 0){
                runnersCount++;
            }
        }
        return runnersCount;
    }
    public static int onlineHuntersCount(){
        int huntersCount=0;
        for(Player player : Bukkit.getOnlinePlayers()){
            UUID uuid = player.getUniqueId();
            if(Manhunt.identity.get(uuid) == 1){
                huntersCount++;
            }
        }
        return huntersCount;
    }
    public static boolean isRunner(Player player){
        return Manhunt.identity.get(player.getUniqueId()) == 0;
    }
    public static boolean isRunner(UUID playerUUID){
        return Manhunt.identity.get(playerUUID) == 0;
    }
    public static boolean isHunter(Player player){
        return Manhunt.identity.get(player.getUniqueId()) == 1;
    }
    public static UUID getRunnerFromIndex(int index){//index太大返回null
        int count = 0;
        for(UUID uuid : Manhunt.onlinePlayersUUID){
            if(isRunner(uuid)) count++;
            if(count==index) return uuid;
        }
        return null;
    }
    public static Integer getIndexFromRunner(UUID targetUuid){//找不到返回null
        int count = 0;
        for(UUID uuid : Manhunt.onlinePlayersUUID){
            count++;
            if(uuid.equals(targetUuid)) return count;
        }
        return null;
    }
    public static UUID getNextRunner(UUID uuid){
        Integer nowIndex = getIndexFromRunner(uuid);
        if(nowIndex == null) return null;
        nowIndex++;
        if(nowIndex>onlineRunnersCount()) nowIndex = 1;
        return getRunnerFromIndex(nowIndex);
    }
    public static void setIdentity(Player player,int identity){
        Manhunt.identity.put(player.getUniqueId(),identity);
        if(identity == 0){
            if(Manhunt.gamePhase == 1) CompassUtils.removeTrackingCompass(player);
            if(!life.containsKey(player.getName())){
                life.put(player.getName(),1);
            }
        }else{
            if(Manhunt.gamePhase == 1) CompassUtils.addTrackingCompass(player);
            life.remove(player.getName());
        }
    }
}
