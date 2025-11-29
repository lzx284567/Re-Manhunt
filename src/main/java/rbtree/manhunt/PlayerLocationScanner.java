package rbtree.manhunt;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import static rbtree.manhunt.GameEnd.gameEnd;

public class PlayerLocationScanner {
    public static void playerLocationScanner(Player player){
        if(IdentityUtils.isHunter(player)) return;
        if(Manhunt.endMethod != 1) return;
        if(player.getWorld().getEnvironment() != World.Environment.THE_END) return;
        Block playerAt = player.getLocation().getBlock();
        if(playerAt.getType() == Material.END_PORTAL){
            gameEnd("runner",player.getName());
        }
    }
}
