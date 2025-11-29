package rbtree.manhunt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static rbtree.manhunt.GameEnd.gameEnd;

public class PlayerInventoryScanner{
    public static void goalItemCheck(Player player) {
        PlayerInventory inv = player.getInventory();
        if(IdentityUtils.isRunner(player) && Manhunt.endMethod == 3 && Manhunt.gamePhase == 1){
            for(int slot = 0; slot <= 40; slot++){
                ItemStack item = inv.getItem(slot);
                if(item == null || item.isEmpty()) continue;
                if(item.getType().equals(Manhunt.goalItem)){
                    gameEnd("runner",player.getName());
                }
            }
        }
    }
}