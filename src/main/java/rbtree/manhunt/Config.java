package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {

   private static Integer endingTime;
   private static boolean ifFreezeTime;
   private static boolean ifPreloadChunk;
   private static Integer preloadChunkRadius;
   private static Integer tickPerChunk;
   private static boolean ifAutoRestart;
   private static String restartBatPosition;
   private static boolean ifGiveReward;
   private static Double rewardRatio;
   private static boolean ifEnableLobby;
   private static String waitingRoomName;
   private static Double waitingRoomPosX;
   private static Double waitingRoomPosY;
   private static Double waitingRoomPosZ;
   private static String endingRoomName;
   private static Double endingRoomPosX;
   private static Double endingRoomPosY;
   private static Double endingRoomPosZ;
   public static void loadConfig(){
       Plugin mh = Manhunt.getManhunt();
       FileConfiguration config = mh.getConfig();
       mh.saveDefaultConfig();
       mh.reloadConfig();
       /*
       final int latestConfigVersion = 2;
       if(!config.contains("version")){
           config.set("version",1);
           mh.saveConfig();
       }
       int configVersion=config.getInt("version");
       mh.getLogger().info("nowVersion: "+configVersion);

       if(configVersion < latestConfigVersion){
           Manhunt.getManhunt().getLogger().info("检测到 config.yml 版本过低，准备升级.....");
           for(int nowVersion = configVersion + 1; nowVersion <= latestConfigVersion; nowVersion++){
               updateConfig(nowVersion);
           }
           config.set("version",latestConfigVersion);
           mh.saveConfig();
       }
       */
       Manhunt.huntersBlindTime=config.getInt("game.default.hunters_blind_time",20);
       Manhunt.disableAnchor=config.getBoolean("game.default.disable_anchor",false);
       Manhunt.disableBed=config.getBoolean("game.default.disable_bed",false);
       Manhunt.disablePotionArrow=config.getBoolean("game.default.disable_potion_arrow",false);
       Manhunt.displayAdvancement=config.getBoolean("game.default.display_advancement",true);
       Manhunt.gameSeconds=config.getInt("game.default.game_seconds.before_game",900);
       Manhunt.stopTime=config.getInt("game.default.game_seconds.in_game",43200);
       endingTime=config.getInt("game.default.game_seconds.after_game",120);
       ifFreezeTime = config.getBoolean("game.freeze_time",false);
       ifPreloadChunk = config.getBoolean("game.preload_chunks.enabled",true);
       preloadChunkRadius = config.getInt("game.preload_chunks.radius",6);
       tickPerChunk = config.getInt("game.preload_chunks.tick_per_chunk",5);
       ifAutoRestart = config.getBoolean("game.auto_restart.enabled",false);
       restartBatPosition = config.getString("game.auto_restart.restart_bat_pos","restart.bat");
       ifGiveReward = config.getBoolean("game.reward.enabled",false);
       rewardRatio = config.getDouble("game.reward.ratio",1);
       ifEnableLobby = config.getBoolean("others.lobby.enabled",false);
       waitingRoomName = config.getString("others.lobby.waiting_world_name","lobby");
       waitingRoomPosX = config.getDouble("others.lobby.waiting_room_pos.x",100.5);
       waitingRoomPosY = config.getDouble("others.lobby.waiting_room_pos.y",66);
       waitingRoomPosZ = config.getDouble("others.lobby.waiting_room_pos.z",100.5);
       endingRoomName = config.getString("others.lobby.ending_world_name","lobby");
       endingRoomPosX = config.getDouble("others.lobby.ending_room_pos.x",0.5);
       endingRoomPosY = config.getDouble("others.lobby.ending_room_pos.y",65);
       endingRoomPosZ = config.getDouble("others.lobby.ending_room_pos.z",0.5);
       checkDependencies();
   }
   public static void checkDependencies(){
       if (Manhunt.getEconomy()==null) {
           if(ifGiveReward){
               Manhunt.getManhunt().getLogger().severe("需要 Vault 插件才能在游戏结束后给予玩家奖励！");
               Manhunt.getManhunt().getLogger().severe("需要 Vault 插件才能有房主，如没有 Vault，默认只有 OP 才能能执行 /manhunt 指令。");
               ifGiveReward=false;
           }
       }
       if (!Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core") && ifEnableLobby) {
           Manhunt.getManhunt().getLogger().severe("无法启用大厅，需要Multiverse-Core插件才能使用多维度！");
           ifEnableLobby=false;
       }
   }
   public static Integer getEndingTime(){return endingTime;}
   public static boolean getIfFreezeTime(){return ifFreezeTime;}
   public static boolean getIfPreloadChunk(){return ifPreloadChunk;}
   public static Integer getPreloadChunkRadius(){return preloadChunkRadius;}
   public static Integer getTickPerChunk(){return tickPerChunk;}
   public static boolean getIfAutoRestart(){return ifAutoRestart;}
   public static String getRestartBatPosition(){return restartBatPosition;}
   public static boolean getIfGiveReward(){return ifGiveReward;}
   public static Double getRewardRatio(){return rewardRatio;}
   public static boolean getIfEnableLobby(){return ifEnableLobby;}
   public static String getWaitingRoomName(){return waitingRoomName;}
   public static Double getWaitingRoomPosX(){return waitingRoomPosX;}
   public static Double getWaitingRoomPosY(){return waitingRoomPosY;}
   public static Double getWaitingRoomPosZ(){return waitingRoomPosZ;}
   public static String getEndingRoomName(){return endingRoomName;}
   public static Double getEndingRoomPosX(){return endingRoomPosX;}
   public static Double getEndingRoomPosY(){return endingRoomPosY;}
   public static Double getEndingRoomPosZ(){return endingRoomPosZ;}
}