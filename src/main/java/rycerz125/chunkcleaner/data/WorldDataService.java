package rycerz125.chunkcleaner.data;

import net.querz.mca.MCAUtil;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import rycerz125.chunkcleaner.ChunkCleaner;
import rycerz125.chunkcleaner.utils.Graph;
import rycerz125.chunkcleaner.utils.IntVector;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

public class WorldDataService {

    public static final String DATA_PATH = "dataBase" + File.separator + "chunkCleaner" + File.separator;

    List<World> worlds;
    HashMap<String, List<IntVector>> chunksOfWorlds;
    public WorldDataService(){
        chunksOfWorlds = new HashMap<>();
        worlds = new ArrayList<>();
    }

    public List<ChunkInfo> getExistingDataOfRegion(String world, int xIndex, int zIndex){
        File file = new File(getMcaTxtFileDirectory(world,xIndex,zIndex));
        if(!file.exists()) return null;
        Region region = Region.loadFromFile(file);
        if(region != null) {
            return region.chunkInfoList;
        }
        return null;
    }
    public List<ChunkInfo> loadRegionFromYamlFile(File file){
        Yaml yaml = new Yaml(new Constructor(Region.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(String.valueOf(file));
        Region region = yaml.load(inputStream);
        return region.chunkInfoList;
    }
    public static String getMcaTxtFileDirectory(String world, int x, int z){
        return DATA_PATH + world +
                File.separator + MCAUtil.createNameFromRegionLocation(x,z) + ".txt";
    }
    public boolean chunkModifiedInCurrentSession(String worldName, IntVector chunkIndexes){
        if(chunksOfWorlds.containsKey(worldName))
            return chunksOfWorlds.get(worldName).contains(chunkIndexes);
        return false;
    }
    public void setChunkModified(String worldName, IntVector chunkIndexes){
        if(!chunksOfWorlds.containsKey(worldName)){
            chunksOfWorlds.put(worldName, new ArrayList<>());
        }
        chunksOfWorlds.get(worldName).add(chunkIndexes);
    }
    public void saveChunkInfo(String world, int xChunkIndex, int zChunkIndex, long generateTime, boolean whiteListed){
        //Yaml yaml = new Yaml();
        //StringWriter writer = new StringWriter();
        setChunkModified(world, new IntVector(xChunkIndex, zChunkIndex));

        Region region = getRegionByCoords(world, MCAUtil.chunkToRegion( xChunkIndex),MCAUtil.chunkToRegion(zChunkIndex));
        region.chunkInfoList.add(new ChunkInfo(xChunkIndex,zChunkIndex,generateTime));
        //yaml.dump(region, writer);
        String directory = getMcaTxtFileDirectory(world, region.xIndex, region.zIndex);


        writeChunkInfoToFile(new ChunkInfo(xChunkIndex,zChunkIndex,generateTime), directory, whiteListed);
        //writeRegionToFile(writer.toString(), directory);
        ///Bukkit.getLogger().info("Zapisano do pliku: " + directory);
    }

    public void saveChunkInfo(String world, int xChunkIndex, int zChunkIndex, long generateTime){
        saveChunkInfo(world,xChunkIndex,zChunkIndex,generateTime, false);
    }
    private void writeChunkInfoToFile(ChunkInfo chunkInfo,String directory , boolean whiteListed){
        createFileIfNonExists(directory);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directory,true));
            chunkInfo.writeToFile(bufferedWriter, whiteListed);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeRegionToFile(String content, String directory){
        createFileIfNonExists(directory);
        try {
            FileWriter fileWriter= new FileWriter(directory);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createFileIfNonExists(String directory){
        File file = new File(directory);
        if(!file.exists()) FileCreator.createFile(directory);
    }

    public World getWorldByName(String name){
        for(World world : worlds){
            if(world.name.equals(name))
                return world;
        }
        World world = new World(name);
        worlds.add(world);
        return world;
    }
    public Region getRegionByCoords(String worldName, int x, int z){
        World world = getWorldByName(worldName);
        return world.getRegionByIndexes(x, z);
    }

    public List<IntVector> getAllRegionIndexesOfDirectory(File directory){
        List<IntVector> regionIndexes = new ArrayList<>();
        if(directory != null)
        for(File file : directory.listFiles()){
            regionIndexes.add(ChunkCleaner.utils.getCoordsFromRegionFileName(file.getName()));
        }
        return regionIndexes;
    }
    public List<IntVector> reduceRegionList(List<IntVector> regions, int connectionDistance){
        List<IntVector> reducedRegions = new ArrayList<>();

        Graph graph = createGraph(regions, connectionDistance);
        ArrayList<ArrayList<Integer>> intLists = graph.getFinalList();

        for(ArrayList<Integer> list : intLists){
            reducedRegions.add(regions.get(list.get(0)));
        }

        return reducedRegions;
    }
    private Graph createGraph(List<IntVector> regions, int connectionDistance){
        Graph graph = new Graph(regions.size());

        for (int i = 0; i < regions.size(); i++) {
            for(int j = i+1; j < regions.size(); j++){
                if(regions.get(i).isCloseTo(regions.get(j), connectionDistance))
                    graph.addEdge(i,j);
            }
        }
        return graph;
    }

}
