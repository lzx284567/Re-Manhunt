package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;
import java.util.UUID;

import static rbtree.manhunt.FangZhu.changeFangZhu;
import static rbtree.manhunt.FangZhu.removeFangZhu;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        String playerName = event.getPlayer().getName();
        UUID quitPlayerUUID = event.getPlayer().getUniqueId();
        Manhunt.onlinePlayersUUID.remove(quitPlayerUUID);

        if(playerName.equalsIgnoreCase(Manhunt.fangZhu)){
            if(Manhunt.onlinePlayersUUID.isEmpty()){
                removeFangZhu();
            }else{
                Optional<? extends Player> findNewFangzhu = Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !p.getName().equals(playerName))
                        .findAny();
                if(findNewFangzhu.isPresent()){
                    Player newFangzhu = findNewFangzhu.get();
                    newFangzhu.sendMessage("§e§l原房主已退出，您已成为新的房主！");
                    changeFangZhu(newFangzhu.getName());
                }
            }
        }
        if(Manhunt.onlinePlayersUUID.isEmpty()){//之前已经将退出的玩家删掉了
            if(Manhunt.gamePhase == 0){
                Reset.reset();
            }
            if(Manhunt.gamePhase == 2){
                if(Config.getIfAutoRestart()){
                    Restart.restart();
                }else{
                    Bukkit.getServer().shutdown();
                }
            }
            if(Manhunt.gamePhase == 1){
                //如无人就准备重启
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!Manhunt.onlinePlayersUUID.isEmpty()){
                            cancel();
                        }else{
                            if(Config.getIfAutoRestart()){
                                Restart.restart();
                            }else{
                                Bukkit.getServer().shutdown();
                            }
                        }
                    }
                }.runTaskLaterAsynchronously(Manhunt.getManhunt(), 1800);
            }
        }else{
            if(Manhunt.gamePhase == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (IdentityUtils.isHunter(player)) {
                        if(Manhunt.tracking.get(player.getUniqueId()).equals(quitPlayerUUID)){
                            TrackingUtils.nextTracking(player);//猎人追踪的玩家下线，自动更换追踪
                        }
                    }
                }
                if (IdentityUtils.onlineRunnersCount() == 0) {
                    Manhunt.sendToAllPlayers("§c§l[System] 服务器目前没有跑者，如 5 分钟内没有跑者进入，将判猎人胜利！");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (IdentityUtils.onlineRunnersCount() > 0) {
                                cancel();
                            }else{
                                Manhunt.noRunnerStop = true;
                                GameEnd.gameEnd("hunter", "无");
                            }

                        }
                    }.runTaskLaterAsynchronously(Manhunt.getManhunt(), 6000);
                }
            }
        }
    }
}
