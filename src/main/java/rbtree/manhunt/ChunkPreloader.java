package rbtree.manhunt;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkPreloader{
    public static void preloadChunks(World world) {
        Location center = world.getSpawnLocation();
        int centerX = center.getBlockX() / 16;
        int centerZ = center.getBlockZ() / 16;
        final int[] currentChunk = {0};
        final int radius = Config.getPreloadChunkRadius();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(Manhunt.gamePhase == 1){
                    Manhunt.getManhunt().getLogger().info("检测到游戏开始，已经停止预加载区块！");
                    cancel();
                }
                for(int i = currentChunk[0]; i < currentChunk[0] + 1; i++){
                    int chunkX = -radius + (i / (2*radius+1));
                    int chunkZ = -radius + (i % (2*radius+1));
                    if(chunkX > radius){
                       cancel();
                    }
                    if (world.isChunkLoaded(chunkX+centerX, chunkZ+centerZ)) continue;
                    world.loadChunk(chunkX+centerX, chunkZ+centerZ, true);
                }
                currentChunk[0] += 1;
            }
        }.runTaskTimer(Manhunt.getManhunt(), 20,Config.getTickPerChunk());

    }
}
