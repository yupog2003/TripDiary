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

    public String getPOI() {
        return POI;
    }

    public void setPOI(String POI) {
        this.POI = POI;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public String getCostName() {
        return costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public Float getCostDollar() {
        return costDollar;
    }

    public void setCostDollar(Float costDollar) {
        this.costDollar = costDollar;
    }

    public DocumentFile getFile() {
        return file;
    }

    public void setFile(DocumentFile file) {
        this.file = file;
    }
}
