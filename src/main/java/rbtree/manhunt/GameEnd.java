package rbtree.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

import static rbtree.manhunt.FangZhu.removeFangZhu;

public class GameEnd {
    /*
    step 1:gamemode adventure
    step 2:show messages
    step 3:reload world
    step 4:reset
    §
     */
    public static void gameEnd(String winner,String relativePlayer){//0=runner 1=hunter
        Manhunt.gamePhase = 2;
        Plugin plugin = Manhunt.getManhunt();
        Bukkit.getScheduler().runTask(plugin, () -> {
            removeFangZhu();

            Component endInfo;
            Component bang = Component.text("！").color(NamedTextColor.WHITE);
            Manhunt.runnerReward = CalculateReward.calculateRunnerReward(winner);
            Manhunt.hunterReward = CalculateReward.calculateHunterReward(winner);
            TextDecoration bold = TextDecoration.BOLD;

            if(winner.equals("hunter")){
                Component p1 = Component.text("最后一个死亡的跑者：").color(NamedTextColor.RED);
                Component p2 = Component.text(relativePlayer).color(NamedTextColor.DARK_RED).decorate(bold);
                endInfo = p1.append(p2);
            }else{
                if(Manhunt.endMethod == 1){
                    Component p1 = Component.text(relativePlayer).color(NamedTextColor.AQUA).decorate(bold);
                    Component p2 = Component.text(" 是通关者").color(NamedTextColor.YELLOW);
                    endInfo = p1.append(p2.append(bang));
                }else if(Manhunt.endMethod == 2){
                    Component p1 = Component.text(relativePlayer).color(NamedTextColor.AQUA).decorate(bold);
                    Component p2 = Component.text(" 取得了进度 ").color(NamedTextColor.YELLOW);
                    String advancementName = Manhunt.getAdvancementChinese(Manhunt.goalAdvancement);
                    Component p3 = Component.text(Objects.requireNonNullElse(advancementName, "无")).color(NamedTextColor.AQUA).decorate(bold);
                    endInfo = p1.append(p2.append(p3.append(bang)));
                }else{
                    Component p1 = Component.text(relativePlayer).color(NamedTextColor.AQUA).decorate(bold);
                    Component p2 = Component.text(" 获得了 ").color(NamedTextColor.YELLOW);
                    Component itemName = Manhunt.getMaterialComponent(Manhunt.goalItem.name());
                    Component p3;
                    if(itemName == null){
                        p3 = Component.text("无").color(NamedTextColor.AQUA).decorate(bold);
                    }else{
                        p3 = itemName.color(NamedTextColor.AQUA).decorate(bold);
                    }
                    endInfo = p1.append(p2.append(p3.append(bang)));
                }
            }
            if(Manhunt.noRunnerStop){
                endInfo = Component.text("游戏因 5 分钟没有跑者而强制结束！").color(NamedTextColor.GRAY);
            }
            if(Manhunt.forceStop){
                endInfo = Component.text("游戏被房主强制结束！").color(NamedTextColor.GRAY);
            }

            for(Player player : Bukkit.getOnlinePlayers()){
                player.setGameMode(GameMode.SPECTATOR);
                if(!Manhunt.forceStop){
                    boolean isRunner=IdentityUtils.isRunner(player);
                    if((isRunner && winner.equals("runner")) || (!isRunner && winner.equals("hunter"))){
                        player.sendMessage("§l§6你赢了！");
                        Title.Times times = Title.Times.times(
                                Ticks.duration(10),
                                Ticks.duration(70),
                                Ticks.duration(20)
                        );
                        Title winTitle = Title.title(Component.text("§l§6你赢了！"), Component.text("§6游戏结束！"), times);
                        player.showTitle(winTitle);
                    }else{
                        player.sendMessage("§l§9你输了！");
                        Title.Times times = Title.Times.times(
                                Ticks.duration(10),
                                Ticks.duration(70),
                                Ticks.duration(20)
                        );
                        Title winTitle = Title.title(Component.text("§l§9你输了！"), Component.text("§9游戏结束！"), times);
                        player.showTitle(winTitle);
                    }
                }

                double reward;
                if(IdentityUtils.isRunner(player)){
                    reward = Manhunt.runnerReward * Config.getRewardRatio();
                } else {
                    reward = Manhunt.hunterReward * Config.getRewardRatio();
                }
                if(Manhunt.getEconomy() != null && Config.getIfGiveReward()){
                    Economy econ = Manhunt.getEconomy();
                    econ.depositPlayer(player,reward);
                }


                player.sendMessage("------------[游戏信息]------------");
                long seed = Objects.requireNonNull(Bukkit.getWorld("world")).getSeed();
                player.sendMessage("§6§l世界种子：§7"+seed);
                player.sendMessage("§6§l游戏时间：§7"+Manhunt.timeFormat(Manhunt.gameSeconds));
                player.sendMessage("§6§l跑者将获得：§7"+Manhunt.runnerReward+" §6§l数量金币！");
                player.sendMessage("§6§l猎人将获得：§7"+Manhunt.hunterReward+" §6§l数量金币！");
                if(Manhunt.getEconomy()==null){
                    player.sendMessage("§7由于未安装经济插件，无法给予您 "+reward+" 金币！");
                }else if(!Config.getIfGiveReward()){
                    player.sendMessage("§7由于没有在配置文件中允许奖励机制，无法给予您 "+reward+" 金币！");
                }else{
                    if(Manhunt.forceStop){
                        player.sendMessage("§7由于游戏被强制停止，只能给予您 "+Manhunt.gameSeconds / 4 * Config.getRewardRatio()+" 金币！");
                    }
                }

                player.sendMessage(endInfo);
                player.sendMessage("---------------------------------");
                player.sendMessage("§7Thanks for playing!");
            }

            Manhunt.gameSeconds = Config.getEndingTime();

            Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(),()->{
                World voidWorld = Bukkit.getWorld(Config.getEndingRoomName());
                if(Config.getIfEnableLobby()) {
                    if (voidWorld == null) {
                        plugin.getLogger().severe("无法找到结束房间维度，请您检查 config.yml！");
                        return;
                    }
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.teleport(new Location(voidWorld, Config.getEndingRoomPosX(), Config.getEndingRoomPosY(), Config.getEndingRoomPosZ()));
                        player.getInventory().clear();
                    }
                }

            },120);
            if(Config.getIfAutoRestart()){
                Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(), Restart::restart,Config.getEndingTime()*20);
            }

        });


    }
}
