package rbtree.manhunt;

import org.bukkit.GameRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAllGamerules {
    public static List<String> getAllGamerules(){
        List<String> ret = new ArrayList<>();
        List<GameRule <?>> gameRules= Arrays.stream(GameRule.values()).toList();
        for(GameRule <?> gamerule : gameRules){
            ret.add(gamerule.getName());
        }
        return ret;
    }
}
