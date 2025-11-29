package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class GameStart {
    /*
    step 0:如果必要设置没有完成，则取消并提示
    step 1:tp
    step 2:heal,hunger
    step 3:hunters effect
    §
     */
    public static void gameStart(){
        Manhunt.runCommand("title @a title \"§25 秒后开始！\"");
        Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(),()-> Manhunt.runCommand("title @a title \"§a4 秒后开始！\""),20);
        Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(),()-> Manhunt.runCommand( "title @a title \"§e3 秒后开始！\""),40);
        Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(),()-> Manhunt.runCommand( "title @a title \"§62 秒后开始！\""),60);
        Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(),()-> Manhunt.runCommand( "title @a title \"§c1 秒后开始！\""),80);

        Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(), GameStart::prepare,100);
    }

    private static void prepare() {
        if(Config.getIfFreezeTime()) Manhunt.runCommand("execute in overworld run tick unfreeze");
        UUID firstRunnerUUID = IdentityUtils.getRunnerFromIndex(1);

        if(firstRunnerUUID == null){
            Manhunt.getManhunt().getLogger().log(Level.SEVERE,"错误：无法找到第一个跑者，无法开始游戏！");
            return;
        }

        Player firstRunner = Bukkit.getPlayer(firstRunnerUUID);
        if(firstRunner == null){
            Manhunt.getManhunt().getLogger().log(Level.SEVERE,"错误：第一个跑者不存在，无法开始游戏！");
            Manhunt.getManhunt().getLogger().log(Level.INFO,"UUID:"+firstRunnerUUID);
            return;
        }
        Location spawnLocation = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.clearActivePotionEffects();
            player.teleport(spawnLocation);
            player.sendHealthUpdate(20.0, 20, 5);
            player.getInventory().clear();
            if (IdentityUtils.isHunter(player)) {
                TrackingUtils.changeTracking(player,firstRunner);
                PotionEffect darkness = new PotionEffect(PotionEffectType.DARKNESS, Manhunt.huntersBlindTime * 20, 254);
                PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, Manhunt.huntersBlindTime * 20, 254);
                PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, Manhunt.huntersBlindTime * 20, 254);
                PotionEffect resistance = new PotionEffect(PotionEffectType.RESISTANCE, Manhunt.huntersBlindTime * 20, 4);
                PotionEffect miningFatigue = new PotionEffect(PotionEffectType.MINING_FATIGUE, Manhunt.huntersBlindTime * 20, 4);
                player.addPotionEffect(darkness);
                player.addPotionEffect(weakness);
                player.addPotionEffect(slowness);
                player.addPotionEffect(resistance);
                player.addPotionEffect(miningFatigue);
                player.getInventory().setItem(8,CompassUtils.getTrackingCompass());//获取追踪指南针
            }
            player.sendMessage("§6§l[System] 游戏开始！");
            player.sendMessage("§6§l[System] 所有猎人将被限制移动并失明 §b§l"+Manhunt.huntersBlindTime+"§6§l 秒！");
            if(!Manhunt.changedGamerule.isEmpty()){
                player.sendMessage("§6§l[System] 本次游戏被更改的游戏规则有：");
                for(int i=0;i<Manhunt.changedGamerule.size();i++){
                    player.sendMessage("§6§l"+i+1+".§b§l "+Manhunt.changedGamerule.get(i));
                }
            }
        }
        Manhunt.gamePhase = 1;
        Manhunt.gameSeconds = 0;
    }
}
