package rycerz125.chunkcleaner.data;

import java.io.BufferedWriter;
import java.io.IOException;

public class ChunkInfo {
    private int X;
    private int Z;
    private long generateTime;
    public boolean whiteListed;
    private int xRegionIndex;
    private int zRegionIndex;

    public ChunkInfo(int X, int Z, long generateTime){
        this.X = X;
        this.Z = Z;
        this.generateTime = generateTime;
        this.whiteListed = false;
    }
    public ChunkInfo(int X, int Z, long generateTime, boolean whiteListed){
        this.X = X;
        this.Z = Z;
        this.generateTime = generateTime;
        this.whiteListed = whiteListed;
    }
    public void writeToFile(BufferedWriter writer, boolean whiteListed) throws IOException {
        writer.write(generateTime+"," +X+"," +Z+","+whiteListed+"\n");
    }

    public int getxRegionIndex() {
        return xRegionIndex;
    }

    public void setxRegionIndex(int xRegionIndex) {
        this.xRegionIndex = xRegionIndex;
    }

    public void setzRegionIndex(int zRegionIndex) {
        this.zRegionIndex = zRegionIndex;
    }

    public int getzRegionIndex() {
        return zRegionIndex;
    }

    public int getX() {
        return X;
    }

    public int getZ() {
        return Z;
    }

    public long getGenerateTime() {
        return generateTime;
    }

    public void setX(int x) {
        X = x;
    }

    public void setZ(int z) {
        Z = z;
    }

    public void setGenerateTime(long generateTime) {
        this.generateTime = generateTime;
    }
}
