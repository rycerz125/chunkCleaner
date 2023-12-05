import org.testng.annotations.Test;
import rycerz125.chunkcleaner.ChunkCleaner;
import rycerz125.chunkcleaner.ChunkRemover;
import org.junit.jupiter.api.Assertions;
import rycerz125.chunkcleaner.utils.IntVector;
import rycerz125.chunkcleaner.utils.Utils;
import rycerz125.chunkcleaner.data.WorldDataService;

import java.io.File;

public class ChunkRemoverTest {
    @Test
    void getCoordsFromFileNameTest(){
        ChunkRemover chunkRemover = new ChunkRemover();
        ChunkCleaner.utils = new Utils();
        IntVector intVector = ChunkCleaner.utils.getCoordsFromRegionFileName("r.-1.10.mca");
        Assertions.assertEquals(-1, intVector.x);
        Assertions.assertEquals(10, intVector.z);
    }
    @Test
    void removeChunksTest(){
        ChunkCleaner.worldDataService = new WorldDataService();
        ChunkCleaner.utils = new Utils();
        ChunkRemover chunkRemover = new ChunkRemover();
        long removes = chunkRemover.removeChunksFromWorld("world", 0L);
        System.out.println(removes);
    }
    @Test
    void removeChunkByCsvFile(){
        ChunkCleaner.worldDataService = new WorldDataService();
        ChunkCleaner.utils = new Utils();
        ChunkRemover chunkRemover = new ChunkRemover();
        long removes = chunkRemover.removeChunksFromWorld(new File("yaml\\kwadrat25x25.csv"),"world");
        System.out.println("usunieto lacznie " +removes);
    }
    @Test
    void removeChunkByCsvFileInvert(){
        ChunkCleaner.worldDataService = new WorldDataService();
        ChunkCleaner.utils = new Utils();
        ChunkRemover chunkRemover = new ChunkRemover();
        long removes = chunkRemover.removeChunksFromWorld(new File("D:\\842866.wom\\dataBase\\ChunkCleaner\\Bigjampers\\Bigjampers.csv"),"world");
        System.out.println("usunieto lacznie " +removes);
    }

}
