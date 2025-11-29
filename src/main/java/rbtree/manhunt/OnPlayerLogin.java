package rbtree.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST;
import static org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_BANNED;

public class OnPlayerLogin implements Listener {
    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event){
        String playername = event.getName();
        long currentTime = System.currentTimeMillis();
        if(Manhunt.ifBaned.contains(playername)){
            Component kickComponent = Component.text("抱歉，房主已将您封禁。").color(NamedTextColor.RED);
            event.disallow(KICK_BANNED,kickComponent);
            return;
        }
        if(Manhunt.gamePhase == 1 && !Manhunt.ifAllowVisit.contains(playername)){
            long durationTime;

            if(Manhunt.lastLoginTime.containsKey(playername)){
                durationTime = currentTime - Manhunt.lastLoginTime.get(playername);
                Manhunt.lastLoginTime.replace(playername,currentTime);
            }else{
                durationTime = 999999999;
                Manhunt.lastLoginTime.put(playername,currentTime);
                Manhunt.PendingRequests.add(playername);
            }

            if(durationTime >= 60 * 1000){
                Component kickComponent = Component.text("已向房主发送加入请求，请过会再进。").color(NamedTextColor.AQUA);
                event.disallow(KICK_WHITELIST,kickComponent);
                Player fangzhu = Bukkit.getPlayer(Manhunt.fangZhu);
                if(fangzhu != null){
                    fangzhu.sendMessage("§f§l玩家 §b"+playername+" §f§l申请加入服务器！");
                    fangzhu.sendMessage("您可以输入 §7/manhunt allow "+playername+"§f 以允许ta进入");
                    fangzhu.sendMessage("或者输入 §7/manhunt deny "+playername+"§f 以禁止ta进入并取消ta发送申请的权限。");
                }
            }else{
                Component kickComponent = Component.text("请过 " + durationTime / 1000 + " 秒再尝试发送加入请求。").color(NamedTextColor.RED);
                event.disallow(KICK_WHITELIST,kickComponent);
            }
        }
    }
}
