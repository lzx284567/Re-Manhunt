package rbtree.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static rbtree.manhunt.FangZhu.changeFangZhu;
import static rbtree.manhunt.AdvancementUtils.getAllAdvancements;
import static rbtree.manhunt.GetAllItems.getAllItemNames;
import static rbtree.manhunt.GetAllGamerules.getAllGamerules;
import static rbtree.manhunt.Manhunt.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@NullMarked
public class ManhuntCommand extends BukkitCommand {
    String[] commandtree = {"", "manhunt", "runner", "settings", "allow", "help", "disable",
            "condition", "blindtime", "life", "anchorexplotion",
            "bedexplotion", "potionarrow", "advancement", "getitem", "beat", "start", "fangzhu","stop","deny","gamerule","displayadvancement","reload"};
    Integer treelength = commandtree.length - 1;
    int[] father = {0, 0, 1, 1, 1, 1, 3, 3, 3, 3, 6, 6, 6, 7, 7, 7, 1, 1, 1, 1, 1, 6, 1, 1};
    @Nullable
    public List<String> cachedItemNames = null;
    @Nullable
    public List<String> cachedAdvancements = null;
    @Nullable
    public List<String> cachedGameruleNames = null;

    private boolean confirmStop = false;

    private final List<String> mainWorldList = List.of("world","world_nether","world_the_end","all");
    public ManhuntCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Permission perms = Manhunt.getPermissions();

        if (perms != null && sender instanceof Player && !perms.playerHas(null,(Player) sender,"manhunt.cmd")) {
            sendMessage(sender, "你没有执行此命令的权限！", ERROR_COLOR);
            return false;
        }
        int len = args.length;
        if (len == 0) {
            sendMessage(sender, "请输入 /manhunt help 以查看帮助", INFO_COLOR);
            return false;
        }
        String op1 = args[0];
        if (op1.equals("runner")) {
            if (len == 1) {
                sendMessage(sender, "用处：如玩家非跑者，将某玩家添加进跑者列表", USAGE_COLOR);
                sendMessage(sender, "用处：否则在跑者列表中删除该玩家", USAGE_COLOR);
                sendMessage(sender, "用法：/manhunt runner 玩家名", USAGE_COLOR);
                return false;
            }

            String op2 = args[1];
            Player runner = Bukkit.getPlayer(op2);
            if(runner == null){
                sendMessage(sender,"该玩家不存在！",ERROR_COLOR);
                return false;
            }
            if (IdentityUtils.isRunner(runner)) {
                IdentityUtils.setIdentity(runner,1);
                sendToAllPlayers( "已将 " + op2 + " 从跑者列表中删除！", SUCCESS_COLOR);
                return true;
            }

            IdentityUtils.setIdentity(runner,0);
            sendToAllPlayers( "已将 " + op2 + " 设置为跑者！", SUCCESS_COLOR);
            return true;
        } else if (op1.equals("settings")) {
            if (len == 1) {
                sendMessage(sender, "用处：设定游戏规则（禁用，通关条件等）", USAGE_COLOR);
                return false;
            }
            String op2 = args[1];
            if (op2.equals("disable")) {
                if (len < 4) {
                    sendMessage(sender, "用处：决定是否禁用床爆炸、重生锚爆炸和药水箭（默认都未被禁用）", USAGE_COLOR);
                    sendMessage(sender,"用法举例：/manhunt settings disable bedexplotion true",USAGE_COLOR);
                    return false;
                }
                String op3 = args[2];
                String op4 = args[3];
                boolean value;
                if(op4.equals("true")){
                    value = true;
                }else if(op4.equals("false")){
                    value = false;
                }else{
                    sendMessage(sender,"布尔值输入错误！",ERROR_COLOR);
                    return false;
                }

                if (op3.equals("anchorexplotion")) {
                    disableAnchor = value;
                    if (disableAnchor) sendToAllPlayers( "重生锚爆炸已被禁用！", SUCCESS_COLOR);
                    else sendToAllPlayers( "已取消对重生锚爆炸的禁用！", SUCCESS_COLOR);
                } else if (op3.equals("bedexplotion")) {
                    disableBed = value;
                    if (disableBed) sendToAllPlayers( "床爆炸已被禁用！", SUCCESS_COLOR);
                    else sendToAllPlayers( "已取消对床爆炸的禁用！", SUCCESS_COLOR);
                } else if (op3.equals("potionarrow")) {
                    disablePotionArrow = value;
                    if (disablePotionArrow) sendToAllPlayers( "药水箭已被禁用！现在药水箭不能被射出", SUCCESS_COLOR);
                    else sendToAllPlayers("已取消对药水箭的禁用！", SUCCESS_COLOR);
                } else if (op3.equals("displayadvancement")) {
                    displayAdvancement = !value;
                    if (displayAdvancement) sendToAllPlayers( "现在获得进度会被显示！", SUCCESS_COLOR);
                    else sendToAllPlayers( "现在获得进度不会被显示！", SUCCESS_COLOR);
                } else {
                    sendMessage(sender, "输入错误！", ERROR_COLOR);
                    return false;
                }

            } else if (op2.equals("condition")) {
                if (len == 2) {
                    sendMessage(sender, "用处：设定跑者通关条件", USAGE_COLOR);
                    sendMessage(sender, "条件有三种类型：获得某进度，获得某物品，通关（这里指击败末影龙后跳入传送门）", USAGE_COLOR);
                    return false;
                }
                if (gamePhase == 1) {
                    sendMessage(sender, "游戏已开始，无法改变结束条件！", ERROR_COLOR);
                    return false;
                }
                String op3 = args[2];

                if (op3.equals("advancement")) {
                    if (len == 3) {
                        sendMessage(sender, "用法：/manhunt settings condition advancement 进度名", USAGE_COLOR);
                        return false;
                    }
                    String op4 = args[3];
                    if (cachedAdvancements != null && !cachedAdvancements.contains(op4)) {
                        sendMessage(sender, "输入的不是进度名！", ERROR_COLOR);
                        return false;
                    }
                    endMethod = 2;
                    goalAdvancement = op4.replace("_", " ");
                    sendToAllPlayers( "现在跑者赢得游戏的条件是获得 " + Manhunt.getAdvancementChinese(Manhunt.goalAdvancement) + " 进度！", STATUS_COLOR);
                } else if (op3.equals("getitem")) {
                    if (len == 3) {
                        sendMessage(sender, "用法：/manhunt settings condition getitem 物品名", USAGE_COLOR);
                        return false;
                    }
                    String op4 = args[3];
                    if (cachedItemNames != null && !cachedItemNames.contains(op4)) {
                        sendMessage(sender, "输入的不是物品名！", ERROR_COLOR);
                        return false;
                    }
                    endMethod = 3;
                    goalItem = Material.matchMaterial(op4);
                    Component bang = Component.text("！").color(NamedTextColor.WHITE);
                    Component p1 = Component.text("现在跑者赢得游戏的条件是 获得 ").color(NamedTextColor.AQUA);
                    Component materialComponent = Manhunt.getMaterialComponent(op4);
                    Component p2 = Objects.requireNonNullElseGet(materialComponent, () -> Component.text("无").color(NamedTextColor.AQUA));
                    sendToAllPlayers(p1.append(p2.append(bang)));
                } else if (op3.equals("beat")) {
                    endMethod = 1;
                    sendToAllPlayers( "现在跑者赢得游戏的条件是击败末影龙后跳入末地传送门！", STATUS_COLOR);
                } else {
                    sendMessage(sender, "输入错误！", ERROR_COLOR);
                    return false;
                }

            } else if (op2.equals("blindtime")) {
                if (gamePhase == 1) {
                    sendMessage(sender, "游戏已开始，无法改变失明时间！", ERROR_COLOR);
                    return false;
                }
                if (len == 2) {
                    sendMessage(sender, "用处：游戏开始后，所有猎人将有一段时间处于失明状态且不能干任何事，请输入这段时间的长度！（单位为秒，非负整数）", USAGE_COLOR);
                    sendMessage(sender, "用法：/manhunt settings blindtime 猎人失明时间！（时间为0代表不失明）", USAGE_COLOR);
                    return false;
                }
                String op3 = args[2];
                if (op3.matches("-?[0-9]+")) {
                    int time = Integer.parseInt(op3);
                    if (time < 0) {
                        sendMessage(sender, "时间需为非负整数", ERROR_COLOR);
                    } else {
                        huntersBlindTime = time;
                        sendToAllPlayers( "猎人失明时间已设置为 " + time + " 秒！", SUCCESS_COLOR);
                    }
                } else {
                    sendMessage(sender, "时间需为非负整数", ERROR_COLOR);
                }
            } else if (op2.equals("life")) {
                if (len < 4) {
                    sendMessage(sender, "用处：指定某人有几条命（跑者默认为一条，猎人默认无限条）",USAGE_COLOR);
                    sendMessage(sender,"如某人血量为两条，代表他能复活一次", USAGE_COLOR);

                    sendMessage(sender, "建议搭配 gamerule 等使用！", USAGE_COLOR);
                    sendMessage(sender, "用法：/manhunt settings life 玩家名 命数！", USAGE_COLOR);
                    return false;
                }
                String op3 = args[2];
                String op4 = args[3];
                if (op4.matches("-?[0-9]+")) {
                    int lifes = Integer.parseInt(op4);
                    String tmp = Integer.toString(lifes - 1);
                    if (lifes <= 0) {
                        sendMessage(sender, "生命需为正整数。", ERROR_COLOR);
                    } else {
                        life.put(op3,lifes);
                        sendToAllPlayers( "已将 " + op3 + " 的生命设置为 " + op4 + " 条（即他能复活 " + tmp + " 次！）", SUCCESS_COLOR);
                    }
                } else {
                    sendMessage(sender, "复活次数需为正整数。", ERROR_COLOR);
                }
            } else {
                sendMessage(sender, "输入不正确！", ERROR_COLOR);
                return false;
            }
        } else if (op1.equals("fangzhu")) {
            if (len == 1) {
                sendMessage(sender, "用处：转让房主", USAGE_COLOR);
                sendMessage(sender, "用法：/manhunt fangzhu 玩家名", USAGE_COLOR);
                return false;
            }
            if(perms == null){
                sendMessage(sender,"腐竹没有装 Vault 插件，无法使用房主功能！",ERROR_COLOR);
                return false;
            }
            String op2 = args[1];
            if (op2.equalsIgnoreCase(fangZhu)) {
                sendMessage(sender, "此玩家已经是房主！", ERROR_COLOR);
                return false;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playername = player.getName();
                if (playername.equals(op2)) {
                    player.sendMessage("§b§l您被 "+fangZhu+" 转让为了房主！");
                    changeFangZhu(op2);
                    sendToAllPlayers( "房主已被转让为 " + op2 + "！", SUCCESS_COLOR);
                    return true;
                }
            }
            sendMessage(sender, "不存在此玩家！", ERROR_COLOR);
            return false;
        } else if(op1.equals("allow")){
            if (len == 1) {
                sendMessage(sender, "用处：游戏开始后允许某玩家进入", USAGE_COLOR);
                sendMessage(sender, "用法：/manhunt allow 玩家名", USAGE_COLOR);
                return true;
            }
            String op2 = args[1];
            if(gamePhase < 1 && !ifBaned.contains(op2)){
                sendMessage(sender,"输入无效：目前游戏尚未开始，玩家可自由进入。",STATUS_COLOR);
            }else if(ifAllowVisit.contains(op2)){
                sendMessage(sender,"输入无效：此玩家已经有进入权限。",STATUS_COLOR);
            }else if(!PendingRequests.contains(op2)){
                sendMessage(sender,"此玩家还未请求过加入服务器！",ERROR_COLOR);
            }else{
                PendingRequests.remove(op2);
                ifBaned.remove(op2);
                if(!ifAllowVisit.contains(op2)) ifAllowVisit.add(op2);
                sendToAllPlayers("玩家 "+op2+" 已被允许进入服务器。",SUCCESS_COLOR);
            }
        } else if(op1.equals("deny")){
            if (len == 1) {
                sendMessage(sender, "用处：禁止某玩家进入", USAGE_COLOR);
                sendMessage(sender, "用法：/manhunt deny 玩家名", USAGE_COLOR);
                return true;
            }
            String op2 = args[1];
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playername = player.getName();
                if (playername.equals(op2) && ifAllowVisit.contains(playername)) {
                    Component kickComponent = Component.text("您被房主取消了进入权限。").color(NamedTextColor.RED);
                    player.kick(kickComponent);
                    sendMessage(sender,"玩家 "+op2+" 已被踢出。",SUCCESS_COLOR);
                    break;
                }
            }
            if(ifAllowVisit.contains(op2)){
                ifAllowVisit.remove(op2);
                if(!ifBaned.contains(op2)) ifBaned.add(op2);
                sendToAllPlayers("玩家 "+op2+" 已被取消进入权限。",SUCCESS_COLOR);
            }else{
                sendMessage(sender,"输入无效：此玩家本就没有进入权限，或此玩家从未尝试进入过房间。",STATUS_COLOR);
            }
            PendingRequests.remove(op2);
        } else if(op1.equals("gamerule")){
            if (len != 4) {
                sendMessage(sender, "用处：修改某世界的游戏规则", USAGE_COLOR);
                sendMessage(sender, "用法：/manhunt gamerule 世界 规则条目 值", USAGE_COLOR);
                sendMessage(sender, "用法：世界填all则对所有世界生效", USAGE_COLOR);
                return true;
            }

            String worldInput = args[1];
            String ruleName = args[2];
            String valueInput = args[3];

            GameRule<?> gamerule = GameRule.getByName(ruleName);
            if (gamerule == null) {
                sendMessage(sender, "不存在此游戏规则！", ERROR_COLOR);
                return false;
            }

            Object parsedValue;
            try {
                if (gamerule.getType() == Boolean.class) {
                    if ("true".equalsIgnoreCase(valueInput)) {
                        parsedValue = true;
                    } else if ("false".equalsIgnoreCase(valueInput)) {
                        parsedValue = false;
                    } else {
                        sendMessage(sender,"布尔值输入错误！",ERROR_COLOR);
                        return false;
                    }
                } else {
                    parsedValue = Integer.parseInt(valueInput);
                }
            } catch (NumberFormatException e) {
                sendMessage(sender, "请输入有效数字", ERROR_COLOR);
                return false;
            }

            if ("all".equalsIgnoreCase(worldInput)) {
                String[] mainWorlds = {"world","world_nether","world_the_end"};
                for (String worldName : mainWorlds) {
                    World world =  Objects.requireNonNull(Bukkit.getWorld(worldName));

                    if (gamerule.getType() == Boolean.class) {
                       world.setGameRule((GameRule<Boolean>) gamerule, (Boolean) parsedValue);
                    } else {
                        world.setGameRule((GameRule<Integer>) gamerule, (Integer) parsedValue);
                    }
                }
                Manhunt.changedGamerule.add("在所有世界中 "+ruleName+" 为 "+parsedValue);
                sendToAllPlayers("成功为所有世界将规则 " + ruleName + " 设置为 " + parsedValue, SUCCESS_COLOR);
            } else {
                World targetWorld = Bukkit.getWorld(worldInput);
                if (targetWorld == null) {
                    sendMessage(sender, "不存在此世界: " + worldInput, ERROR_COLOR);
                    return false;
                }
                if (gamerule.getType() == Boolean.class) {
                    targetWorld.setGameRule((GameRule<Boolean>) gamerule, (Boolean) parsedValue);
                } else {
                    targetWorld.setGameRule((GameRule<Integer>) gamerule, (Integer) parsedValue);
                }
                Manhunt.changedGamerule.add("在世界 "+worldInput+" 中 "+ruleName+" 为 "+parsedValue);
                sendToAllPlayers( "成功为世界 " + worldInput + " 将规则 " + ruleName + " 设置为 " + parsedValue, SUCCESS_COLOR);
            }

            return true;

        } else if(op1.equals("stop")){
            if(gamePhase != 1){
                sendMessage(sender,"游戏还未开始！",ERROR_COLOR);
                return false;
            }
            if(!confirmStop){
                sendMessage(sender,"您确认要强制停止游戏吗？",STATUS_COLOR);
                sendMessage(sender,"如确认，请再输入一遍此指令。",USAGE_COLOR);
                confirmStop = true;
            }else{
                sendMessage(sender,"执行成功！",SUCCESS_COLOR);
                confirmStop = false;
                forceStop = true;
                GameEnd.gameEnd("hunter","无");
            }
        }else if (op1.equals("start")) {
            if (gamePhase == 1) {
                sendMessage(sender, "游戏已开始！", ERROR_COLOR);
                return false;
            }
            if (gamePhase == 2) {
                sendMessage(sender, "游戏已经结束，请在新房间重新开始游戏！", ERROR_COLOR);
                return false;
            }
            if(IdentityUtils.onlineRunnersCount()==0){
                sendMessage(sender,"目前无在线跑者，无法开始游戏！",ERROR_COLOR);
                return false;
            }
            if(endMethod == 0){
                sendMessage(sender,"未设置跑者通关方法，无法开始游戏！",ERROR_COLOR);
                return false;
            }
            if(IdentityUtils.onlineHuntersCount() == 0 ){
                sendMessage(sender,"目前猎人全部下线 或 没有猎人，无法开始游戏！",ERROR_COLOR);
                return false;
            }
            sendMessage(sender, "猎人游戏，启动！", SUCCESS_COLOR);
            GameStart.gameStart();
        } else if (op1.equals("reload")) {
            Config.loadConfig();
            sendMessage(sender,"配置重载完毕。", STATUS_COLOR);
        }else{
            sendMessage(sender, "输入未知，请输入 /manhunt help 以查看帮助！", ERROR_COLOR);
            return false;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Permission perms = Manhunt.getPermissions();
        if (Manhunt.getPermissions() != null && sender instanceof Player && !perms.playerHas(null,(Player) sender,"manhunt.cmd"))  return List.of();
        int len = args.length;

        if ((len == 2 && args[0].equals("runner")) ||
                (len == 3 && args[0].equals("settings") && args[1].equals("life")) ||
                (len == 2 && args[0].equals("fangzhu"))) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(adv -> adv.toLowerCase().startsWith(args[len - 1].toLowerCase()))
                    .toList();//玩家列表
        } else if (len == 4 && args[0].equalsIgnoreCase("settings") && args[1].equalsIgnoreCase("condition")) {
            if (args[2].equalsIgnoreCase("advancement")) {
                if (cachedAdvancements == null) {
                    cachedAdvancements = getAllAdvancements();
                }

                return cachedAdvancements.stream()
                        .filter(adv -> adv.toLowerCase().startsWith(args[len - 1].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args[2].equalsIgnoreCase("getitem")) {
                if (cachedItemNames == null) {
                    cachedItemNames = getAllItemNames();
                }

                return cachedItemNames.stream()
                        .filter(adv -> adv.toLowerCase().startsWith(args[len - 1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else if (len == 2 && args[0].equalsIgnoreCase("gamerule")){
            return mainWorldList;
        } else if (len == 3 &&
                args[0].equalsIgnoreCase("gamerule") &&
                mainWorldList.contains(args[1])){

            if (cachedGameruleNames == null) cachedGameruleNames = getAllGamerules();
            return cachedGameruleNames.stream()
                    .filter(adv -> adv.toLowerCase().startsWith(args[len - 1].toLowerCase()))
                    .collect(Collectors.toList());

        } else if (len == 4 &&
                args[0].equalsIgnoreCase("gamerule") &&
                mainWorldList.contains(args[1]) &&
                cachedGameruleNames != null &&
                cachedGameruleNames.contains(args[2]) &&
                Objects.requireNonNull(GameRule.getByName(args[2])).getType() == Boolean.class){

            return List.of("true","false");
        } else if (len == 4 &&
                args[0].equalsIgnoreCase("settings") &&
                args[1].equalsIgnoreCase("disable")){
            return List.of("true","false");
        } else if (len == 2 && args[0].equalsIgnoreCase("allow")){
            return PendingRequests;
        } else {
            //默认返回：
            if (len >= 1) {
                List<String> tabcomplete = new ArrayList<>();
                String lstarg;

                if (len == 1) lstarg = "manhunt";//如输入第一个参数，上一个参数是命令名字
                else lstarg = args[len - 2];//上一个参数
                String nowarg = args[len - 1];//正在输入的参数

                int tmpPlace = 0;
                for (int i = 1; i <= treelength; i++) {
                    if (commandtree[i].equals(lstarg)) {
                        tmpPlace = i;
                        break;
                    }
                }
                if (tmpPlace == 0) return List.of();
                for (int i = 1; i <= treelength; i++) {
                    if (father[i] == tmpPlace) tabcomplete.add(commandtree[i]);
                }

                return tabcomplete.stream()
                        .filter(adv -> adv.toLowerCase().startsWith(nowarg.toLowerCase()))
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }

        }
        return List.of();
    }
}