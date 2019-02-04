package br.edu.ufrgs.inf.bpm.validation.latex;

import br.edu.ufrgs.inf.bpm.validation.similarityData.SimilarityDataGED;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SimilarityGEDTableGenerator extends TableGenerator {

    private static DecimalFormat decimalFormat = new DecimalFormat("00.00\\%");

    public SimilarityGEDTableGenerator(List<SimilarityDataGED> similarityDataList) {
        super("Similarity", "tab:similarity", getHeader(), getBody(similarityDataList));
    }

    private static List<String> getHeader() {
        List<String> headerList = new ArrayList<>();
        headerList.add("\\textbf{$\\mathbf{Pd_{ID}}$}");
        headerList.add("Original Text");
        headerList.add("Generated from Text");
        //headerList.add("Original Text");
        headerList.add("Generated from Model");
        //headerList.add("Original Model");
        return headerList;
    }

    private static List<List<String>> getBody(List<SimilarityDataGED> similarityDataList) {
        List<List<String>> body = new ArrayList<>();

        for (SimilarityDataGED similarityData : similarityDataList) {
            List<String> bodyLine = new ArrayList<>();

            bodyLine.add(similarityData.getTextName());
            bodyLine.add(decimalFormat.format(similarityData.getSimilarityOriginalText()));
            bodyLine.add(decimalFormat.format(similarityData.getSimilarityCurrentApproachFromText()));
            //bodyLine.add(decimalFormat.format(similarityData.getSimilarityOriginalApproachFromText()));
            bodyLine.add(decimalFormat.format(similarityData.getSimilarityCurrentApproachFromModel()));
            //bodyLine.add(decimalFormat.format(similarityData.getSimilarityOriginalApproachFromModel()));

            body.add(bodyLine);
        }
        return body;
    }

}
