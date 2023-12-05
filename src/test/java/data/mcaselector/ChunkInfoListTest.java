package data.mcaselector;

import org.testng.annotations.Test;
import rycerz125.chunkcleaner.data.mcaselector.ChunkInfoList;

import java.io.File;
import java.io.IOException;

public class ChunkInfoListTest {
    @Test
    void mergeTest(){
        try {
            ChunkInfoList.mergeAllCsvToOne(new File("D:\\842866.wom\\dataBase\\ChunkCleaner\\parker_original1.8"), "parker_original1.8.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
