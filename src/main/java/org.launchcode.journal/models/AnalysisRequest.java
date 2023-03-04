package org.launchcode.journal.models;

import java.util.ArrayList;

public class AnalysisRequest {
    private ArrayList<Integer> entryIds;
    private String analysisType;

    public ArrayList<Integer> getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(ArrayList<Integer> entryIds) {
        this.entryIds = entryIds;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }
}

