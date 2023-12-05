package rycerz125.chunkcleaner;

import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import org.bukkit.Bukkit;
import rycerz125.chunkcleaner.data.ChunkInfo;
import rycerz125.chunkcleaner.data.mcaselector.ChunkInfoList;
import rycerz125.chunkcleaner.utils.IntVector;

import java.io.File;
import java.util.*;

public class ChunkRemover {
    public long calculateFolderWeight(File directory){
        File[] regionFiles = directory.listFiles();
        if (regionFiles == null) return 0;
        long size = 0;
        for (File file : regionFiles){
            size+= file.length();
        }
        return size;
    }
    public long removeChunksFromWorld(File mcaSelectorFile, String world){
        String regionDirectory = world + File.separator + "region" + File.separator;
        File[] regionFiles = new File(regionDirectory).listFiles();
        if (regionFiles == null) return 0;

        long beforeSize = calculateFolderWeight(new File(regionDirectory));
        long chunksRemoved = 0;
        int regionRemoved = 0;
        long currentRemoves = 0;

        ChunkInfoList chunkInfoList = ChunkInfoList.readFromCsv(mcaSelectorFile);
        boolean inverted = chunkInfoList.isInverted();

        MCAFile mcaFile = null;
        if(inverted) {
            for (File file : regionFiles) {
                IntVector intVector = ChunkCleaner.utils.getCoordsFromRegionFileName(file.getName());
                if (!chunkInfoListContainsRegion(intVector.x, intVector.z, chunkInfoList.getChunkInfoList())) {
                    regionRemoved++;
                    file.delete();
                } else {
                    //wczytaj region, usun z regionu to czego nie masz, zapisz region
                    mcaFile = loadMcaFile(file);
                    currentRemoves = removeChunksFromRegion(mcaFile, -1,file.getName(),chunkInfoList);
                    chunksRemoved += currentRemoves;
                    if(currentRemoves != 0)
                        saveMcaFile(mcaFile, file);
                }

            }
        }
        else{
            for (File file : regionFiles) {
                IntVector intVector = ChunkCleaner.utils.getCoordsFromRegionFileName(file.getName());
                if (chunkInfoListContainsRegion(intVector.x, intVector.z, chunkInfoList.getChunkInfoList())) {
                    //wczytaj region, usun z regionu to co masz wspolne, zapisz region
                    mcaFile = loadMcaFile(file);
                    currentRemoves = removeChunksFromRegion(mcaFile, -1, file.getName(), chunkInfoList);
                    chunksRemoved += currentRemoves;
                    if(currentRemoves != 0)
                        saveMcaFile(mcaFile, file);
                }
            }
        }
        long afterSize = calculateFolderWeight(new File(regionDirectory));
        Bukkit.getLogger().info(beforeSize + " bytes  - " + (beforeSize-afterSize)  + " bytes  = " + afterSize);
//        System.out.println(beforeSize + " bytes  - " + (beforeSize-afterSize)  + " bytes  = " + afterSize);
        return chunksRemoved + 1000000000L *regionRemoved;

    }

    public List<ChunkInfo> getCommonChunkInfoList(int xRegion, int zRegion, List<ChunkInfo> chunkInfoList){
        List<ChunkInfo> common = new ArrayList<>();
        for (ChunkInfo chunkInfo : chunkInfoList){
            if(chunkInfo.getxRegionIndex() == xRegion && chunkInfo.getzRegionIndex() == zRegion)
                common.add(chunkInfo);
        }
        return common;
    }
    public boolean chunkInfoListContainsRegion(int xRegion, int zRegion, List<ChunkInfo> chunkInfoList){
        for (ChunkInfo chunkInfo : chunkInfoList){
            if(chunkInfo.getxRegionIndex() == xRegion && chunkInfo.getzRegionIndex() == zRegion)
                return true;
        }
        return false;
    }
    public long removeChunksFromWorld(String world, long sinceDate){
        String regionDirectory = world + File.separator + "region" + File.separator;
        File[] regionFiles = new File(regionDirectory).listFiles();
        if (regionFiles == null) return 0;
        long chunksRemoved = 0;
        long currentRemoves = 0;
        MCAFile mcaFile;
        for(File file : regionFiles){
            mcaFile = loadMcaFile(file);
            currentRemoves = removeChunksFromRegion(world,mcaFile, sinceDate, file.getName(),false);
            chunksRemoved += currentRemoves;
            if(currentRemoves != 0)
                saveMcaFile(mcaFile, file);
        }
        return chunksRemoved;
    }
    private MCAFile loadMcaFile(File file){
        MCAFile mcaFile = null;
        try {
            mcaFile = MCAUtil.read(file);
            Bukkit.getLogger().info("Odczytano " + file.getName());
//            System.out.println("Odczytano " + file.getName());
        } catch (Exception e) {
            //e.printStackTrace();
            Bukkit.getLogger().info("BLAD Odczytu " + file.getName());
//            System.out.println("BLAD Odczytu " + file.getName());
        }
        return mcaFile;
    }
    private long removeChunksFromRegion(MCAFile mcaFile, long sinceDate,
                                        List<ChunkInfo> relatedChunkInfoList, boolean inverted){
        if(mcaFile==null) return 0;
        long removes = 0;
        boolean weHaveInfo = relatedChunkInfoList != null;
        Set<Integer> notemptychunkIdList = new HashSet<>();
        for(int i = 0; i<1023; i++){
            if(mcaFile.getChunk(i) != null) {
                notemptychunkIdList.add(i);
            }
        }
//        System.out.println("Not empty chunks in " +fileName + notEmptyChunks);
        Set<Integer> storedInfoId = new HashSet<>();

        if(weHaveInfo)
        for(ChunkInfo chunkInfo : relatedChunkInfoList){
            int id = MCAFile.getChunkIndex(chunkInfo.getX(), chunkInfo.getZ());
            storedInfoId.add(id);
        }
        for(int i = 0; i<1023; i++){
            if(inverted)
                if(!storedInfoId.contains(i)) {
                    mcaFile.setChunk(i, null);
                    removes++;
                }
            if(!inverted)
                if(storedInfoId.contains(i)) {
                    mcaFile.setChunk(i, null);
                    removes++;
                }
        }

//        if(weHaveInfo) {
//                for (ChunkInfo chunkInfo : relatedChunkInfoList) {
//
//                    if (chunkInfo.getGenerateTime() > sinceDate) {
//                        if (mcaFile.getChunk(chunkInfo.getX(), chunkInfo.getZ()) == null) continue;
//                        mcaFile.setChunk(chunkInfo.getX(), chunkInfo.getZ(), null);
//
//                        removes++;
//                    }
//                }
//        }

        Bukkit.getLogger().info("ilosc rzeczywistych notnull chunków: " + notemptychunkIdList.size());
        Bukkit.getLogger().info("ilosc naszych zebranych: " + storedInfoId.size());
//        System.out.println("ilosc rzeczywistych notnull chunków: " + notemptychunkIdList.size());
//        System.out.println("ilosc naszych zebranych: " + storedInfoId.size());

        storedInfoId.retainAll(notemptychunkIdList);
        Bukkit.getLogger().info("zbieznosc id: " + storedInfoId.size());
        Bukkit.getLogger().info("usuniecia: "+removes + "\n\n");
//        System.out.println("zbieznosc id: " + storedInfoId.size());
//        System.out.println("usuniecia: "+removes + "\n\n");
        return removes;
    }
    private long removeChunksFromRegion(MCAFile mcaFile, long sinceDate, String fileName, ChunkInfoList chunkInfoList){
        IntVector intVector = ChunkCleaner.utils.getCoordsFromRegionFileName(fileName);
        List<ChunkInfo> commonChunkInfoList = getCommonChunkInfoList(intVector.x, intVector.z, chunkInfoList.getChunkInfoList());
        return removeChunksFromRegion(mcaFile, sinceDate, commonChunkInfoList, chunkInfoList.isInverted());
    }
    private long removeChunksFromRegion(String world, MCAFile mcaFile, long sinceDate, String fileName, boolean inverted){
        List<ChunkInfo> chunkInfoList = getChunkInfoOfRegion(world, fileName);
        return removeChunksFromRegion(mcaFile,sinceDate,chunkInfoList, inverted);
    }
    private void saveMcaFile(MCAFile mcaFile, File file){
        try {
            mcaFile.cleanupPalettesAndBlockStates();
        } catch (Exception e) {
            //e.printStackTrace();
            Bukkit.getLogger().info("BLAD cleanupPalletes " + file.getName());
//            System.out.println("BLAD cleanupPalletes " + file.getName());
        }
        try {
            MCAUtil.write(mcaFile, file);
            Bukkit.getLogger().info("Zapisano " + file.getName());
//            System.out.println("Zapisano " + file.getName());
        } catch (Exception e) {
            //e.printStackTrace();
            Bukkit.getLogger().info("BLAD Zapisu " + file.getName());
//            System.out.println("BLAD Zapisu " + file.getName());
        }
    }
    private List<ChunkInfo> getChunkInfoOfRegion(String world, String regionFileName){
        IntVector intVector = ChunkCleaner.utils.getCoordsFromRegionFileName(regionFileName);
        return ChunkCleaner.worldDataService.getExistingDataOfRegion(world, intVector.x, intVector.z);
    }

}
