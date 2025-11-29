package rbtree.manhunt;

import java.util.*;

import static rbtree.manhunt.Manhunt.*;
public class Reset {
    public static void reset(){
        life = new HashMap<>();
        ifAllowVisit = new Vector<>();
        ifBaned = new Vector<>();
        ifJoined = new Vector<>();
        lastLoginTime = new HashMap<>();
        noPlayerJoinedFlag = true;
        endMethod = 0;
        goalAdvancement = null;
        goalItem = null;
        changedGamerule = new Vector<>();
        deathKeepInventoryMap = new HashMap<>();
        identity = new HashMap<>();
        onlinePlayersUUID = new ArrayList<>();
        gamePhase = -1;
        fangZhu = null;
        forceStop = false;
        noRunnerStop = false;
        tracking = new HashMap<>();
        hunterReward = 0;
        runnerReward = 0;
        Config.loadConfig();
    }
}
