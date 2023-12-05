package rycerz125.chunkcleaner.utils;

import static java.lang.Math.abs;

public class IntVector {
    public int x;
    public int z;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntVector intVector)) return false;
        return intVector.x == this.x && intVector.z == this.z;
    }

    public boolean isCloseTo(IntVector intVector, int distance) {
        return abs(intVector.x - x) < distance && abs(intVector.z - z) < distance;
    }
    public IntVector(int x, int z){
        this.x = x;
        this.z = z;
    }
    public IntVector(){}
}
