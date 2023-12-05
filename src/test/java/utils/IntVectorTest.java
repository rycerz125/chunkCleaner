package utils;

import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;
import rycerz125.chunkcleaner.utils.IntVector;

public class IntVectorTest {
    @Test
    void equalsTest(){
        IntVector intVector = new IntVector();
        intVector.x = 5;
        intVector.z = 6;
        IntVector intVector2 = new IntVector();
        intVector2.x = 5;
        intVector2.z = 6;
        IntVector intVector3 = new IntVector();
        intVector3.x = 5;
        intVector3.z = 7;

        Assertions.assertTrue(intVector.equals(intVector2));
        Assertions.assertFalse(intVector.equals(intVector3));
    }


}
