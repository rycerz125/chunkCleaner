package rycerz125.chunkcleaner.data;

import rycerz125.chunkcleaner.ChunkCleaner;
import rycerz125.chunkcleaner.utils.IntVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Region {
    public int xIndex;
    public int zIndex;
    public List<ChunkInfo> chunkInfoList;
    public Region(){
        chunkInfoList = new ArrayList<>();
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setChunkInfoList(List<ChunkInfo> chunkInfoList) {
        this.chunkInfoList = chunkInfoList;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public List<ChunkInfo> getChunkInfoList() {
        return chunkInfoList;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    public static Region loadFromFile(File file){
        if(!file.exists()) return null;
        Region region = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            region = parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IntVector intVector = ChunkCleaner.utils.getCoordsFromRegionFileName(file.getName());
        if(region != null) {
            region.setxIndex(intVector.x);
            region.setzIndex(intVector.z);
        }
        return region;
    }
    private static Region parse(BufferedReader reader) throws IOException {
        String currentLine = null;
        boolean lineExists = true;
        List<ChunkInfo> chunkInfoList = new ArrayList<>();
        while (lineExists) {
            currentLine = reader.readLine();
            if (currentLine == null) lineExists = false;
            else {
                List<String> convertedLine = Stream.of(currentLine.split(",", -1))
                        .collect(Collectors.toList());
                long generateTime = Long.parseLong(convertedLine.get(0));
                int x = Integer.parseInt(convertedLine.get(1));
                int z = Integer.parseInt(convertedLine.get(2));
                boolean whiteListed = Boolean.parseBoolean(convertedLine.get(3));
                chunkInfoList.add(new ChunkInfo(x,z,generateTime,whiteListed));
            }
        }
        Region region = new Region();
        region.chunkInfoList = chunkInfoList;
        return region;
    }
}
