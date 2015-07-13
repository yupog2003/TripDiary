package com.yupog2003.tripdiary.data;

import java.io.File;

public class CostData {
    public String POI;
    public int costType;
    public String costName;
    public Float costDollar;
    public File file;

    public CostData(String POI, int costType, String costName, Float costDollar, File file) {
        this.POI = POI;
        this.costType = costType;
        this.costName = costName;
        this.costDollar = costDollar;
        this.file = file;
    }
}
