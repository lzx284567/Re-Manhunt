package rbtree.manhunt;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class LifeUtils {
    public static boolean ifDeath(String playerName){
        if(Manhunt.life.containsKey(playerName)){
            return Manhunt.life.get(playerName) <= 0;
        }else{
            return false;
        }
    }
    public static void decreaseLife(String playerName){
        Player player = Bukkit.getPlayer(playerName);
        if(player == null){
            Manhunt.getManhunt().getLogger().severe("在减少玩家 "+playerName+" 的命数时，发现该玩家不存在。");
            return;
        }
        if(Manhunt.life.containsKey(playerName)){
            int remainLifes=Manhunt.life.get(playerName);
            remainLifes--;
            Manhunt.life.replace(playerName,remainLifes);
            Audience listening = Audience.audience(Bukkit.getOnlinePlayers());
            if(remainLifes == 0){
                Bukkit.getScheduler().runTask(Manhunt.getManhunt(),()->{
                    player.setGameMode(GameMode.SPECTATOR);
                });
                Component specComponent = Component.text("[System] "+playerName+" 已耗尽复活次数，现已变成旁观者。").color(NamedTextColor.RED);
                listening.sendMessage(specComponent);
            }else{
                Component deathComponent = Component.text("[System] "+playerName+" 已死亡，还有 "+remainLifes+" 条生命（即 "+(remainLifes-1)+" 次复活次数。").color(TextColor.color(0xe67e22));
                listening.sendMessage(deathComponent);
            }
        }
    }
}
