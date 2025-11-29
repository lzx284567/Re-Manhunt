package rbtree.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.Bukkit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class PapiExpansions extends PlaceholderExpansion {
    public PapiExpansions(Manhunt plugin) {}

    @Override
    @NotNull
    public String getAuthor() {
        return "rbtree"; //
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "manhunt"; //
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.1"; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equalsIgnoreCase("runnerscount")){
            return String.valueOf(IdentityUtils.onlineRunnersCount());
        }

        if(params.startsWith("allow_")){
            String playername = params.subSequence(10,params.length()).toString();
            if(!Manhunt.ifAllowVisit.contains(playername)) return "false";
            return "true";
        }

        if(params.startsWith("loop_player_name_")){
            try{
                int index = Integer.parseInt(params.substring(17));
                if(index > Bukkit.getOnlinePlayers().size()) return null;
                UUID requestPlayerUUID = Manhunt.onlinePlayersUUID.get(index);
                Player requestPlayer = Bukkit.getPlayer(requestPlayerUUID);
                if(requestPlayer == null){
                    return "请求错误！";
                }
                String requestPlayerName = requestPlayer.getName();
                if(Manhunt.life.containsKey(requestPlayerName)){
                    return requestPlayerName+"§6§l（"+Manhunt.life.get(requestPlayerName)+"命）";
                }else{
                    return requestPlayerName;
                }

            } catch (Exception e){
                return null;
            }
        }

        if(params.startsWith("loop_runner_name_")){
            try{
                int index = Integer.parseInt(params.substring(17));
                if(index > IdentityUtils.onlineRunnersCount()) return null;
                UUID requestRunnerUUID = IdentityUtils.getRunnerFromIndex(index);
                if(requestRunnerUUID == null){
                    return "请求错误：找不到uuid";
                }
                Player requestRunner = Bukkit.getPlayer(requestRunnerUUID);
                if(requestRunner == null){
                    return "错误：无法用uuid找到玩家";
                }
                return requestRunner.getName()+"「"+Manhunt.life.get(requestRunner.getName())+"命」";
            } catch (Exception e){
                return null;
            }
        }

        if (params.startsWith("runner_")){
            String name = params.substring(7);
            Player requestPlayer = Bukkit.getPlayer(name);
            if(requestPlayer == null){
                return "错误：该玩家不存在！";
            }
            if(IdentityUtils.isRunner(requestPlayer)){
                return "true";
            }else{
                return "false";
            }
        }

        if(params.equalsIgnoreCase("tracking")){
            if(player.isOnline() && player.getPlayer() != null){
                if (!Manhunt.tracking.containsKey(player.getPlayer().getUniqueId()) || Manhunt.onlinePlayersUUID.size() == 1){
                    return "&c&l无";
                }else{
                    UUID requestUUID = Manhunt.tracking.get(player.getPlayer().getUniqueId());
                    Player requestPlayer = Bukkit.getPlayer(requestUUID);
                    if(requestPlayer == null) return "错误：无法获取玩家名！";
                    String playerName = requestPlayer.getName();
                    return "&a&l"+playerName;
                }

            }else{
                return "[ERROR]";
            }

        }
        if (params.equalsIgnoreCase("playersinroom")) {
            return Integer.toString(Bukkit.getOnlinePlayers().size());
        }

        if (params.equalsIgnoreCase("fangzhu")) {
            return Manhunt.fangZhu;
        }
        if (params.equalsIgnoreCase("endmethod")){
            if(Manhunt.endMethod == 0){
                return "§c暂未设置";
            }else if(Manhunt.endMethod == 1){
                return "§b通关！";
            }else if(Manhunt.endMethod == 2){
                return "进度 §b"+Manhunt.getAdvancementChinese(Manhunt.goalAdvancement);
            }else{
                Component materialComponent = Objects.requireNonNullElseGet(Manhunt.getMaterialComponent((Manhunt.goalItem.name().toLowerCase())), () -> net.kyori.adventure.text.Component.text("无"));
                Component resolvedComponent = GlobalTranslator.render(materialComponent, Locale.SIMPLIFIED_CHINESE);
                return "物品 §b"+ PlainTextComponentSerializer.plainText().serialize(resolvedComponent);
            }
        }

        if (params.equalsIgnoreCase("time")) {
            if(Manhunt.gamePhase == 1) return "§e游戏时间：§e§l"+Manhunt.timeFormat(Manhunt.gameSeconds);
            else return "§3剩余时间：§3§l"+Manhunt.timeFormat(Manhunt.gameSeconds);
        }

        if (params.equalsIgnoreCase("disabled_info")){
            if(Manhunt.disableAnchor || Manhunt.disableBed || Manhunt.disablePotionArrow){
                String info="§e禁 ";
                if(Manhunt.disableAnchor) info+="§c§l重生锚炸 ";
                if(Manhunt.disableBed) info+="§c§l床炸 ";
                if(Manhunt.disablePotionArrow) info+="§c§l药水箭 ";
                return info;
            }else{
                return "没有被禁用的行为";
            }
        }

        return null;
    }

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return null;
    }
}