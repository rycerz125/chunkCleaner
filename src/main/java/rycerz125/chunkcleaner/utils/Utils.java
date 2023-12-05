package rycerz125.chunkcleaner.utils;

import rycerz125.chunkcleaner.utils.IntVector;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Utils {
    public IntVector getCoordsFromRegionFileName(String fileName){
        fileName = fileName.replace('.', ',');
        List<String> elements = Arrays.asList(fileName.split(","));
        IntVector intVector = new IntVector();
        intVector.x = Integer.parseInt(elements.get(1));
        intVector.z = Integer.parseInt(elements.get(2));
        return intVector;
    }


}
