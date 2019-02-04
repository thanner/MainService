package br.edu.ufrgs.inf.bpm.validation.similarityData;

import br.edu.ufrgs.inf.bpm.util.VersionNumberComparator;
import br.edu.ufrgs.inf.bpm.validation.util.FileNameHandler;

import java.util.List;
import java.util.stream.Collectors;

public class SimilarityDataGED extends SimilarityData {

    private double similarityOriginalText = 0.0;
    private double similarityCurrentApproachFromText = 0.0;
    private double similarityCurrentApproachFromModel = 0.0;
    private double similarityOriginalApproachFromText = 0.0;
    private double similarityOriginalApproachFromModel = 0.0;

    public double getSimilarityOriginalText() {
        return similarityOriginalText;
    }

    public void setSimilarityOriginalText(double similarityOriginalText) {
        this.similarityOriginalText = similarityOriginalText;
    }

    public double getSimilarityCurrentApproachFromText() {
        return similarityCurrentApproachFromText;
    }

    public void setSimilarityCurrentApproachFromText(double similarityCurrentApproachFromText) {
        this.similarityCurrentApproachFromText = similarityCurrentApproachFromText;
    }

    public double getSimilarityCurrentApproachFromModel() {
        return similarityCurrentApproachFromModel;
    }

    public void setSimilarityCurrentApproachFromModel(double similarityCurrentApproachFromModel) {
        this.similarityCurrentApproachFromModel = similarityCurrentApproachFromModel;
    }

    public double getSimilarityOriginalApproachFromText() {
        return similarityOriginalApproachFromText;
    }

    public void setSimilarityOriginalApproachFromText(double similarityOriginalApproachFromText) {
        this.similarityOriginalApproachFromText = similarityOriginalApproachFromText;
    }

    public double getSimilarityOriginalApproachFromModel() {
        return similarityOriginalApproachFromModel;
    }

    public void setSimilarityOriginalApproachFromModel(double similarityOriginalApproachFromModel) {
        this.similarityOriginalApproachFromModel = similarityOriginalApproachFromModel;
    }

    public static List<SimilarityDataGED> prepareSimilarityList(List<SimilarityDataGED> similarityDataList) {
        List<SimilarityDataGED> newSimilarityDataList;
        newSimilarityDataList = similarityDataList.stream().sorted((a, b) -> new VersionNumberComparator().compare(new FileNameHandler(a.getTextName()).getIndex(), new FileNameHandler(b.getTextName()).getIndex())).collect(Collectors.toList());
        newSimilarityDataList.forEach(a -> a.setTextName(new FileNameHandler(a.getTextName()).getName()));
        return newSimilarityDataList;
    }

}
