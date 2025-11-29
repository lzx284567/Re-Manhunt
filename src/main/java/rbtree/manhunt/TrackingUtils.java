package rbtree.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static rbtree.manhunt.Manhunt.STATUS_COLOR;
import static rbtree.manhunt.Manhunt.tracking;

public class TrackingUtils {
    public static void nextTracking(Player player){//让玩家指向下一个人
        if(Manhunt.onlinePlayersUUID.size() == 1){
            player.sendMessage("§c[System] 目前跑者只有1人，无法更换！");
            return;
        }
        UUID nowTracking = tracking.get(player.getUniqueId());
        Manhunt.tracking.replace(player.getUniqueId(),IdentityUtils.getNextRunner(nowTracking));

        updateTracking(player);
    }
    public static void changeTracking(Player player, Player targetPlayer){//将玩家追踪的玩家改为参数玩家
        if(player.getUniqueId().equals(targetPlayer.getUniqueId())) return;//不能跟踪自己
        if(!Manhunt.tracking.containsKey(player.getUniqueId())){
            Manhunt.tracking.put(player.getUniqueId(),targetPlayer.getUniqueId());
        }else{
            Manhunt.tracking.replace(player.getUniqueId(),targetPlayer.getUniqueId());
        }
        updateTracking(player);
    }
    private static void updateTracking(Player player){
        UUID nowTrackingUUID = Manhunt.tracking.get(player.getUniqueId());
        Player nowTracking = Bukkit.getPlayer(nowTrackingUUID);
        if(nowTracking == null){
            Component errorComponent = Component.text("错误：您追踪的玩家不存在！");
            player.sendActionBar(errorComponent);
            return;
        }
        Component part1 = Component.text().content("您正在追踪 ").color(STATUS_COLOR).build();
        Component part2;
        if(IdentityUtils.isRunner(nowTracking)){
            part2 = Component.text("[对手] ").color(NamedTextColor.RED);
        }else{
            part2 = Component.text("[队友] ").color(NamedTextColor.GREEN);
        }
        String trackingPlayerName = nowTracking.getName();
        Component part3 = Component.text().content(trackingPlayerName).color(TextColor.color(0xFFAA00)).build();
        Component trackingActionbar = part1.append(part2).append(part3);
        player.sendActionBar(trackingActionbar);
    }
}
