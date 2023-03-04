package org.launchcode.journal.models;

public class AnalysisResult {
    private String title;
    private String result;
    private String analysisType;


    public AnalysisResult(String title, String result, String analysisType) {
        this.title = title;
        this.result = result;
        this.analysisType = analysisType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }
}

