package br.edu.ufrgs.inf.bpm.validation.similarityData;

import br.edu.ufrgs.inf.bpm.util.VersionNumberComparator;
import br.edu.ufrgs.inf.bpm.validation.util.FileNameHandler;

import java.util.List;
import java.util.stream.Collectors;

public class SimilarityDataTM extends SimilarityData {

    private double simTextFromOriginalText = 0.0;
    private double simTextFromText = 0.0;
    private double simTextFromModel = 0.0;

    public static List<SimilarityDataTM> prepareSimilarityList(List<SimilarityDataTM> similarityDataList) {
        List<SimilarityDataTM> newSimilarityDataList;
        newSimilarityDataList = similarityDataList.stream().sorted((a, b) -> new VersionNumberComparator().compare(new FileNameHandler(a.getTextName()).getIndex(), new FileNameHandler(b.getTextName()).getIndex())).collect(Collectors.toList());
        newSimilarityDataList.forEach(a -> a.setTextName(new FileNameHandler(a.getTextName()).getName()));
        return newSimilarityDataList;
    }

    public double getSimTextFromOriginalText() {
        return simTextFromOriginalText;
    }

    public void setSimTextFromOriginalText(double simTextFromOriginalText) {
        this.simTextFromOriginalText = simTextFromOriginalText;
    }

    public double getSimTextFromText() {
        return simTextFromText;
    }

    public void setSimTextFromText(double simTextFromText) {
        this.simTextFromText = simTextFromText;
    }

    public double getSimTextFromModel() {
        return simTextFromModel;
    }

    public void setSimTextFromModel(double simTextFromModel) {
        this.simTextFromModel = simTextFromModel;
    }

}
