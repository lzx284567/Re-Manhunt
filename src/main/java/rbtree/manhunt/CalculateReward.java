package rbtree.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class CalculateReward {
    private static int getDifficulty(){
        int difficulty;
        if(Manhunt.endMethod == 1){
            difficulty = DifficultyMap.getDifficultyMap().get("FREE THE END");
        }else if(Manhunt.endMethod == 2){
            difficulty = DifficultyMap.getDifficultyMap().get(Manhunt.goalAdvancement.toUpperCase());
        }else{
            if(Manhunt.goalItem == null){
                difficulty = 0;
            }else{
                ItemStack goalItem = ItemStack.of(Manhunt.goalItem);
                ItemMeta goalMeta = goalItem.getItemMeta();
                if(goalMeta == null || !goalMeta.hasRarity()){
                    difficulty = 2;
                }else{
                    ItemRarity goalRarity = goalMeta.getRarity();
                    if(goalRarity.equals(ItemRarity.COMMON)){
                        difficulty = 2;
                    }else if(goalRarity.equals(ItemRarity.UNCOMMON)){
                        difficulty = 20;
                    }else if(goalRarity.equals(ItemRarity.RARE)){
                        difficulty = 80;
                    }else{
                        difficulty = 100;
                    }
                }
            }
        }
        //Bukkit.getConsoleSender().sendMessage("difficulty = "+difficulty);
        return difficulty;
    }

    public static double getGameDifficulty(){
        double gameDifficulty = 1;
        if(Objects.requireNonNull(Bukkit.getWorld("world")).getDifficulty() == Difficulty.HARD){
            gameDifficulty += 0.08;
        }
        if(Objects.requireNonNull(Bukkit.getWorld("world")).getDifficulty() == Difficulty.EASY){
            gameDifficulty -= 0.08;
        }
        if(Objects.requireNonNull(Bukkit.getWorld("world_nether")).getDifficulty() == Difficulty.HARD){
            gameDifficulty += 0.1;
        }
        if(Objects.requireNonNull(Bukkit.getWorld("world_nether")).getDifficulty() == Difficulty.EASY){
            gameDifficulty -= 0.1;
        }
        if(Objects.requireNonNull(Bukkit.getWorld("world_the_end")).getDifficulty() == Difficulty.HARD){
            gameDifficulty += 0.03;
        }
        if(Objects.requireNonNull(Bukkit.getWorld("world_the_end")).getDifficulty() == Difficulty.EASY){
            gameDifficulty -= 0.03;
        }
        return gameDifficulty;
    }
    public static int calculateRunnerReward(String winner){
        int gameMinutes = Manhunt.gameSeconds / 60;
        int baseReward = 15 * gameMinutes;
        int extraReward;
        int runnersCount = IdentityUtils.onlineRunnersCount();
        int difficulty = getDifficulty();
        double gameDifficulty = getGameDifficulty();
        if(Manhunt.forceStop || gameMinutes < 20){
            return baseReward;
        }
        if(winner.equals("runner")){
            extraReward = Math.min(Math.max(900,1800 / runnersCount) * (difficulty / gameMinutes),20000);
        }else{
            extraReward = (int)(0.5 * baseReward);
        }
        return (int)((baseReward + extraReward) * gameDifficulty);
    }

    public static int calculateHunterReward(String winner){
        int gameMinutes = Manhunt.gameSeconds / 60;
        int baseReward = 15 * gameMinutes;
        int extraReward;
        int huntersCount = IdentityUtils.onlineHuntersCount();
        int difficulty = getDifficulty();
        double gameDifficulty = (getGameDifficulty() - 1) / 2 + 1;
        double hasRunnerDifficulty = 1;

        if(Manhunt.forceStop || gameMinutes < 20 || (winner.equals("runner"))){
            return baseReward;
        }

        extraReward = Math.min(Math.max(300, 1200 / huntersCount) * (difficulty / gameMinutes), 1500);
        if(IdentityUtils.onlineRunnersCount() == 0){
            hasRunnerDifficulty = 0.5;
        }
        return (int)((baseReward + extraReward) * gameDifficulty * hasRunnerDifficulty);
    }
}
