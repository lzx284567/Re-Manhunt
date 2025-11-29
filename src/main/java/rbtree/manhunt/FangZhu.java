package rbtree.manhunt;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class FangZhu {
    private static final Permission perms = Manhunt.getPermissions();
    private static final Plugin manhunt = Manhunt.getManhunt();
    static void removeFangZhu(){
        if(perms == null) return;
        if(Manhunt.fangZhu != null){
            perms.playerRemove(null,Bukkit.getOfflinePlayer(Manhunt.fangZhu),"manhunt.cmd");
            perms.playerRemove(null,Bukkit.getOfflinePlayer(Manhunt.fangZhu),"minecraft.difficulty");
        }
        Manhunt.fangZhu = null;
    }

    static void changeFangZhu(String playername){
        if(perms == null) return;
        OfflinePlayer newFangzhu = Bukkit.getOfflinePlayer(playername);
        Player newFangzhuOnline = Bukkit.getPlayer(playername);
        if(newFangzhuOnline == null) return;

        if(Manhunt.fangZhu != null){
            perms.playerRemove(null,Bukkit.getOfflinePlayer(Manhunt.fangZhu),"manhunt.cmd");
            perms.playerRemove(null,Bukkit.getOfflinePlayer(Manhunt.fangZhu),"minecraft.difficulty");
        }

        perms.playerAdd(null,newFangzhu,"manhunt.cmd");
        perms.playerAdd(null,newFangzhu,"minecraft.difficulty");
        Manhunt.fangZhu = playername;

        Manhunt.fangzhuInformation(newFangzhuOnline);
    }
    static void clearFangzhu(){
        //理论上没有玩家应被清除权限（在游戏结束时已经清除）
        //但是以防出现bug还是清除一下
        if(perms == null) return;
        int count = 0;
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        manhunt.getLogger().info("共发现 " + offlinePlayers.length + " 名玩家需要检查");
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            String playerName = offlinePlayer.getName();
            if (perms.playerHas(null, offlinePlayer, "manhunt.cmd")) {
                perms.playerRemove(null, offlinePlayer, "manhunt.cmd");
                count++;
                manhunt.getLogger().info("已从玩家 " + playerName + " 移除权限: " + "manhunt.cmd");
            }
            if (perms.playerHas(null, offlinePlayer, "minecraft.difficulty")) {
                perms.playerRemove(null, offlinePlayer, "minecraft.difficulty");
                count++;
                manhunt.getLogger().info("已从玩家 " + playerName + " 移除权限: " + "minecraft.difficulty");
            }
        }
        manhunt.getLogger().info("操作完成！共从 " + count + " 名玩家移除了权限");
    }
}
