package rycerz125.chunkcleaner.data.mcaselector;

import net.querz.mca.MCAUtil;
import rycerz125.chunkcleaner.data.ChunkInfo;
import rycerz125.chunkcleaner.data.Region;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChunkInfoList {
    List<ChunkInfo> chunkInfoList;
    boolean inverted;

    public List<ChunkInfo> getChunkInfoList() {
        return chunkInfoList;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setChunkInfoList(List<ChunkInfo> chunkInfoList) {
        this.chunkInfoList = chunkInfoList;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public static ChunkInfoList readFromCsv(File file){
        ChunkInfoList chunkInfoList = new ChunkInfoList();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            chunkInfoList = parseFile(reader);
        }catch (Exception e){
            e.printStackTrace();
        }
        return chunkInfoList;
    }
    public static void mergeAllCsvToOne(File directory, String outputFileName) throws IOException {

        File output = new File(directory.toString() +File.separator+ outputFileName);
        System.out.println(output.toString());
        if(directory.exists())
        for(File file : directory.listFiles()){
            Path filePath = Path.of(file.toString());
            String content = Files.readString(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
            writer.write(content);
            writer.close();
        }

    }
    private static ChunkInfoList parseFile(BufferedReader reader) throws IOException {
        String currentLine = null;
        boolean lineExists = true;
        List<ChunkInfo> chunkInfoList = new ArrayList<>();
        ChunkInfoList chunkInfoListObject = new ChunkInfoList();
        while (lineExists) {
            currentLine = reader.readLine();
            if (currentLine == null) lineExists = false;
            else {
                if(currentLine.equals("inverted")){
                    chunkInfoListObject.inverted = true;
                    continue;
                }
                List<String> convertedLine = Stream.of(currentLine.split(";", -1))
                        .collect(Collectors.toList());
                int xRegion = Integer.parseInt(convertedLine.get(0));
                int zRegion = Integer.parseInt(convertedLine.get(1));
                if(convertedLine.size() > 2){
                    int xChunk = Integer.parseInt(convertedLine.get(2));
                    int zChunk = Integer.parseInt(convertedLine.get(3));
                    ChunkInfo chunkInfo =
                            new ChunkInfo(xChunk, zChunk, 0, false);
                    chunkInfo.setxRegionIndex(xRegion);
                    chunkInfo.setzRegionIndex(zRegion);
                    chunkInfoList.add(chunkInfo);
                } else {
                    for (int i = 0; i < 32; i++) {
                        for (int j = 0; j < 32; j++) {
                            int x = MCAUtil.regionToChunk(xRegion) + i;
                            int z = MCAUtil.regionToChunk(zRegion) + j;
                            ChunkInfo chunkInfo =
                                    new ChunkInfo(x, z, 0, false);
                            chunkInfo.setxRegionIndex(xRegion);
                            chunkInfo.setzRegionIndex(zRegion);
                            chunkInfoList.add(chunkInfo);
                        }
                    }
                }

            }
        }
        chunkInfoListObject.chunkInfoList = chunkInfoList;
        return chunkInfoListObject;
    }
}
