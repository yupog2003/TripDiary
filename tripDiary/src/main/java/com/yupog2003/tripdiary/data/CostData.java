package com.yupog2003.tripdiary.data;

import android.support.v4.provider.DocumentFile;

public class CostData {
    public String POI;
    public int costType;
    public String costName;
    public Float costDollar;
    public DocumentFile file;

    public CostData(String POI, int costType, String costName, Float costDollar, DocumentFile file) {
        this.POI = POI;
        this.costType = costType;
        this.costName = costName;
        this.costDollar = costDollar;
        this.file = file;
    }
}
