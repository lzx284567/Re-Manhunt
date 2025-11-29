package rbtree.manhunt;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.*;

public final class Manhunt extends JavaPlugin {
    /*
    to-do:
     */
    private static Economy econ = null;
    private static Permission perms = null;
    private static Manhunt instance;
    public static Manhunt getManhunt(){return instance;}


    public static int huntersBlindTime = 20;
    public static Boolean disableAnchor = false;
    public static Boolean disableBed = false;
    public static Boolean disablePotionArrow = false;
    public static Boolean displayAdvancement = true;
    public static int gameSeconds = 900;
    public static int stopTime = 12*60*60;//12小时

    public static HashMap<String, Integer> life = new HashMap<>();
    public static Vector<String> ifAllowVisit = new Vector<>();
    public static Vector<String> ifBaned = new Vector<>();
    public static Vector<String> ifJoined = new Vector<>();//仅维护，未使用，可能有Bug未发现
    public static List<String> PendingRequests = new Vector<>();
    public static Map<String,Long> lastLoginTime = new HashMap<>();
    public static Boolean noPlayerJoinedFlag = true;
    public static int endMethod = 0;//1=beat,2=advancement,3=getItem,0=目前暂未指定
    public static String goalAdvancement;
    public static Material goalItem;
    public static Vector<String> changedGamerule = new Vector<>();
    public static Map<UUID, Boolean> deathKeepInventoryMap = new HashMap<>();
    public static Map<UUID, Integer> identity = new HashMap<>();//0=runner 1=hunter 2=spectator
    public static ArrayList<UUID> onlinePlayersUUID = new ArrayList<>();
    public static int gamePhase = -1;
    public static String fangZhu = null;
    public static boolean forceStop = false;
    public static boolean noRunnerStop = false;
    public static HashMap<UUID,UUID> tracking = new HashMap<>();
    public static double hunterReward;
    public static double runnerReward;
    private static JsonObject toChinese;

    public static final TextColor PREFIX_COLOR = TextColor.color(0xFFAA00); // 金色
    public static final TextColor ERROR_COLOR = NamedTextColor.RED;
    public static final TextColor SUCCESS_COLOR = NamedTextColor.GREEN;
    public static final TextColor INFO_COLOR = NamedTextColor.YELLOW;
    public static final TextColor USAGE_COLOR = NamedTextColor.GOLD;
    public static final TextColor STATUS_COLOR = NamedTextColor.AQUA;

    public static void runCommand(String s) throws CommandException {
        Bukkit.getScheduler().runTask(instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
    }

    public static void sendMessage(CommandSender sender, String message, TextColor color) {
        Component prefix = Component.text("[Manhunt] ").color(PREFIX_COLOR);
        Component content = Component.text(message).color(color);
        sender.sendMessage(prefix.append(content));
    }

    public static void sendToAllPlayers(String message, TextColor color){
        for(Player player : Bukkit.getOnlinePlayers()){
            sendMessage(player,message,color);
        }
    }

    public static void sendToAllPlayers(Component message){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(message);
        }
    }

    public static void sendToAllPlayers(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(message);
        }
    }

    public static String timeFormat(int seconds){
        Duration duration = Duration.ofSeconds(seconds);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long second = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, second);
    }
    public static Component getMaterialComponent(String name) {
        String processedName = name.toUpperCase().replace(" ","_");
        Material material = Material.getMaterial(processedName);
        if(material != null){
            String translationKey = material.translationKey();
            return Component.translatable(translationKey);
        }
        return null;
    }
    public static String getAdvancementChinese(String name) {
        Advancement advancement = AdvancementUtils.getAdvancement(name);
        if(advancement != null){
            String translationKey = "advancements." + advancement.getKey().getKey().replace('/', '.') + ".title";
            return toChinese.get(translationKey).getAsString();
        }else{
            return null;
        }
    }

    public static void fangzhuInformation(Player player){
        player.sendMessage("§a§l房主就是本次猎人游戏的「主持人」");
        player.sendMessage("§a§l可以§6§l进行游戏设置§a§l以及§6§l启动游戏");
        player.sendMessage("§a§l也可以更改游戏规则和难度");
        player.sendMessage("§a§l输入§6§l /manhunt help §a§l以查询如何进行游戏设置！");
        player.sendMessage("§a§l如想转让房主，请输入§6§l /manhunt fangzhu 玩家名！");
        player.sendMessage("§b§l游戏规则更改方式：§6§l/manhunt gamerule {世界} {条目} {值}");
    }

    @Override
    public void onEnable() {
        instance = this;
        if(setupEconomy()) setupPermissions();
        Config.loadConfig();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiExpansions(this).register();
        } else {
            getLogger().info("需要PlaceholderAPI插件才能注册占位符，不过没有也不影响使用。");
        }

        if(getPermissions() != null) FangZhu.clearFangzhu();
        toChinese = ParseLangFile.loadLanguage();
        getServer().getPluginManager().registerEvents(new OnBedExplode(), this);
        getServer().getPluginManager().registerEvents(new OnChangeTracking(), this);
        getServer().getPluginManager().registerEvents(new OnAnchorExplosion(), this);
        getServer().getPluginManager().registerEvents(new OnPotionArrowShoot(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(),this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLogin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDie(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerChangeWorld(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerGetAdvancement(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJump(), this);

        this.getServer().getCommandMap().register(
                this.getName().toLowerCase(),
                new ManhuntCommand("manhunt", "游戏设置及操作", "请参考 /manhunt help", List.of())
        );

        if(Config.getIfPreloadChunk()) ChunkPreloader.preloadChunks(Objects.requireNonNull(Bukkit.getWorld("world")));//加载区块
        if(Config.getIfFreezeTime()) runCommand("execute in overworld run tick freeze");//避免玩家开始游戏前时间流逝

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, task -> {
            if(gamePhase == 1){
                for(Player player : Bukkit.getOnlinePlayers()){
                    PlayerInventoryScanner.goalItemCheck(player);
                    CompassUtils.updateCompass(player);
                    PlayerLocationScanner.playerLocationScanner(player);
                }
            }
        },0,1);


        Bukkit.getScheduler().runTaskTimerAsynchronously(this, task -> {
            if(gamePhase == 0){
                gameSeconds--;
                if(gameSeconds == 120) sendToAllPlayers("还有2分钟关闭服务器，请尽快开始游戏！",INFO_COLOR);
                if(gameSeconds == 60) sendToAllPlayers("还有1分钟关闭服务器，请尽快开始游戏！",INFO_COLOR);
                if(gameSeconds == 20) sendToAllPlayers("还有20秒关闭服务器，请尽快开始游戏！",INFO_COLOR);
                if(gameSeconds <= 0){
                    if(Config.getIfAutoRestart()){
                        Restart.restart();
                    }else{
                        getServer().shutdown();
                    }
                }
            }else if(gamePhase == 1){
                gameSeconds++;
                if(gameSeconds >= stopTime){
                    if(Config.getIfAutoRestart()){
                        Restart.restart();
                    }else{
                        getServer().shutdown();
                    }
                }
                if(stopTime - gameSeconds == 20*60){
                    sendToAllPlayers("还有20分钟关闭服务器，请尽快结束游戏！",INFO_COLOR);
                }
                if(stopTime - gameSeconds == 60){
                    sendToAllPlayers("还有1分钟关闭服务器，请尽快结束游戏！",INFO_COLOR);
                }
                if(stopTime - gameSeconds == 10){
                    sendToAllPlayers("还有10秒关闭服务器！",INFO_COLOR);
                }
            }else if(gamePhase == 2){
                gameSeconds--;
                if(gameSeconds == 10) sendToAllPlayers("游戏结束，还有10秒关闭服务器！",INFO_COLOR);
            }
        },0,20);
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("作者联系方式，B站：rbtree_，插件已关闭！");
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }


    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            perms = rsp.getProvider();
        }
    }
}
