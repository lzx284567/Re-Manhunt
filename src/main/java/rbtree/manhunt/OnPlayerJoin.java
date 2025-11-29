package rbtree.manhunt;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import static rbtree.manhunt.FangZhu.changeFangZhu;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        if(Manhunt.noPlayerJoinedFlag){
            Manhunt.noPlayerJoinedFlag = false;
            Manhunt.gamePhase = 0;
        }
        if(!Manhunt.identity.containsKey(playerUUID)) Manhunt.identity.put(playerUUID,1);//玩家进入默认为猎人
        if(!Manhunt.onlinePlayersUUID.contains(playerUUID)) Manhunt.onlinePlayersUUID.add(playerUUID);
        if(LifeUtils.ifDeath(playerName)){
            Bukkit.getScheduler().runTask(Manhunt.getManhunt(),()->{
                player.setGameMode(GameMode.SPECTATOR);
            });
        }
        if(Manhunt.onlinePlayersUUID.size() == 1){//tips:该事件被触发时，玩家已经进入
            player.sendMessage("§6§l您是目前唯一玩家，已经成为房主！");
            changeFangZhu(playerName);
        }else{
            if(Manhunt.gamePhase == 0 || Manhunt.gamePhase == 2){
                player.sendMessage("§c§l您不是目前唯一玩家，所以您不是房主。");
                player.sendMessage("§a§l房主就是本次猎人游戏的「主持人」");
                player.sendMessage("§a§l可以进行游戏设置以及启动游戏");
                player.sendMessage("§a§l若想成为房主，请让现房主转让给您。");
            }
        }

        if(Manhunt.gamePhase == 0){
            Manhunt.ifAllowVisit.add(playerName);
            World waitingWorld = Bukkit.getWorld(Config.getWaitingRoomName());
            if(Config.getIfEnableLobby()){
                if(waitingWorld == null){
                    Manhunt.getManhunt().getLogger().severe("无法找到等待室维度，请您检查 config.yml！");
                }else{
                    player.teleport(new Location(waitingWorld, Config.getWaitingRoomPosX(),Config.getWaitingRoomPosY(),Config.getWaitingRoomPosZ()));
                }
            }
            player.getInventory().clear();
            PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION, 10000000, 1);
            player.addPotionEffect(saturation);
        }else if(Manhunt.gamePhase == 1){
            if(player.getWorld().getName().equals(Config.getWaitingRoomName()) && Config.getIfEnableLobby()) {
                player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            }
            if(Manhunt.ifJoined.contains(playerName)){
                player.sendMessage("§b§l[Manhunt 猎人游戏] 欢迎回来！");
            }else{
                player.sendMessage("欢迎来到 §6猎人游戏§f！");
                player.sendMessage("由于您在游戏开始后进入，您的身份为 §b§l猎人。");
                player.sendMessage("如想更改，请联系房主。");
                IdentityUtils.setIdentity(player,1);
                //新玩家进入时默认为猎人
            }

            if(IdentityUtils.onlineRunnersCount() == 1){
                //当跑者进入时他是唯一跑者，将所有猎人指南针指向他
                for(Player hunterPlayer : Bukkit.getOnlinePlayers()){
                    if(IdentityUtils.isHunter(hunterPlayer)){
                        TrackingUtils.changeTracking(hunterPlayer,player);
                    }
                    hunterPlayer.sendMessage("§c§l[System] 目前跑者还有 1 人，所有猎人的指南针已经指向ta！");
                }
            }

            if(IdentityUtils.isHunter(player)){
                CompassUtils.addTrackingCompass(player);

                //调整追踪的人
                UUID firstRunnerUUID = IdentityUtils.getRunnerFromIndex(1);
                if(firstRunnerUUID == null){
                    Optional<? extends Player> firstPlayer = Bukkit.getOnlinePlayers().stream().findFirst();
                    if(firstPlayer.isPresent()){
                        TrackingUtils.changeTracking(player,firstPlayer.get());
                    }
                }else{
                    Player firstRunner = Bukkit.getPlayer(firstRunnerUUID);
                    if(firstRunner == null){
                        Manhunt.getManhunt().getLogger().log(Level.SEVERE,"无法找到第一个跑者！");
                    }else{
                        TrackingUtils.changeTracking(player,firstRunner);
                    }

                }

                CompassUtils.addTrackingCompass(player);
            }

        } else if(Manhunt.gamePhase == 2){
            World voidWorld = Bukkit.getWorld(Config.getEndingRoomName());
            if(Config.getIfEnableLobby()){
                if(voidWorld == null){
                    Manhunt.getManhunt().getLogger().severe("无法找到结算处维度，请您检查 config.yml！");
                }else{
                    player.teleport(new Location(voidWorld, Config.getEndingRoomPosX(),Config.getEndingRoomPosY(),Config.getEndingRoomPosZ()));
                }
            }
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
        }

        if(!Manhunt.ifJoined.contains(playerName)){
            Manhunt.ifJoined.add(playerName);
        }
    }
}
