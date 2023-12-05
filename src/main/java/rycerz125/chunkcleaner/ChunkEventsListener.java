package rycerz125.chunkcleaner;

import net.querz.mca.MCAUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import rycerz125.chunkcleaner.utils.IntVector;

import java.util.Locale;

public class ChunkEventsListener implements Listener {
    @EventHandler
    public void onGenerateChunk(ChunkLoadEvent event){
//        if(!event.isNewChunk()) return;
//        Bukkit.getLogger().info("NOWY CHUNK: " +"--Inhabitet time:"+ event.getChunk().getInhabitedTime()
//        + "--X:" + event.getChunk().getX() + "--Z: " +event.getChunk().getZ());
//
//        int x = event.getChunk().getX();
//        int z = event.getChunk().getZ();
//        long generateTime = System.currentTimeMillis();
//        String world = event.getWorld().getName();
//
//        ChunkCleaner.worldDataService.saveChunkInfo(world, x, z, generateTime);

    }
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        Location location = event.getBlockPlaced().getLocation();
        Bukkit.getLogger().info("Postawienie bloku na chunku: " + location);
        String world = location.getWorld().getName();
        int xChunk = MCAUtil.blockToChunk(location.getBlockX());
        int zChunk = MCAUtil.blockToChunk(location.getBlockZ());
        if(!ChunkCleaner.worldDataService.chunkModifiedInCurrentSession(world, new IntVector(xChunk,zChunk)))
            ChunkCleaner.worldDataService.saveChunkInfo(world, xChunk, zChunk,
                System.currentTimeMillis(), true);
    }
}
