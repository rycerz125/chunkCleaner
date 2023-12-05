package rycerz125.chunkcleaner.data;

import java.util.ArrayList;
import java.util.List;

public class World {
    String name;
    public List<Region> regions;
    public World(String name){
        this.name = name;
        regions = new ArrayList<>();
    }
    public Region getRegionByIndexes(int xRegionIndex, int zRegionIndex){
        for(Region region : regions){
            if(region.xIndex == xRegionIndex && region.zIndex == zRegionIndex)
                return region;
        }
        Region region = new Region();
        region.xIndex = xRegionIndex;
        region.zIndex = zRegionIndex;
        regions.add(region);
        return region;
    }
}
