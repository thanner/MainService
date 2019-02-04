package br.edu.ufrgs.inf.bpm.validation.latex;

import br.edu.ufrgs.inf.bpm.validation.similarityData.SimilarityDataTM;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SimilarityTMTableGenerator extends TableGenerator {
    private static DecimalFormat decimalFormat = new DecimalFormat("00.00\\%");

    public SimilarityTMTableGenerator(List<SimilarityDataTM> similarityDataList) {
        super("Similarity", "tab:similarity", getHeader(), getBody(similarityDataList));
    }

    private static List<String> getHeader() {
        List<String> headerList = new ArrayList<>();
        headerList.add("Text name");
        headerList.add("Original Text");
        headerList.add("Based Original Text");
        headerList.add("Based Original Model");

        return headerList;
    }

    private static List<List<String>> getBody(List<SimilarityDataTM> similarityDataList) {
        List<List<String>> body = new ArrayList<>();

        for (SimilarityDataTM similarityData : similarityDataList) {
            List<String> bodyLine = new ArrayList<>();

            bodyLine.add(similarityData.getTextName());
            bodyLine.add(decimalFormat.format(similarityData.getSimTextFromOriginalText()));
            bodyLine.add(decimalFormat.format(similarityData.getSimTextFromText()));
            bodyLine.add(decimalFormat.format(similarityData.getSimTextFromModel()));

            body.add(bodyLine);
        }
        return body;
    }
}
