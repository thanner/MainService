package br.edu.ufrgs.inf.bpm.validation.grapheditdistance;

import br.edu.ufrgs.inf.bpm.util.Paths;
import br.edu.ufrgs.inf.bpm.validation.latex.SimilarityGEDTableGenerator;
import br.edu.ufrgs.inf.bpm.validation.latex.TableGenerator;
import br.edu.ufrgs.inf.bpm.validation.similarityData.SimilarityDataGED;
import br.edu.ufrgs.inf.bpm.validation.util.FileNameHandler;
import br.edu.ufrgs.inf.bpm.validation.util.ValidationUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.processmining.analysis.epc.similarity.ActivityContextFragment;
import org.processmining.analysis.graphmatching.graph.SimpleGraph;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimilarityGEDApp {

    // Not generated:
    // 1.1 - Bicycle manufacturing - originalText
    // 10.2 - Process B3 - originalText.txt
    // 10.3 - Process B4 - originalText
    // 11.15 - Exercise 4.8 Model the following process. - originalText (TextReader problem)

    public static void main(String[] args) {
        ValidationUtil.removeEmptyFiles();

        configureSystem();
        List<SimilarityDataGED> similarityDataList = new ArrayList<>();
        File folder = new File(Paths.LocalTestDataPath + Paths.dataOriginalTextPath);
        for (File originalTextFile : Objects.requireNonNull(folder.listFiles())) {
            if (ValidationUtil.isTextFile(originalTextFile)) {
                similarityDataList.add(getSimilarityData(originalTextFile));
            }
        }

        //similarityDataList.add(getSimilarityData(new File("src/main/others/TestData/originalText/11.15 - Exercise 4.8 Model the following process. - originalText.txt")));


        // Subprocess
        //similarityDataList.add(getSimilarityData(new File("src/main/others/TestData/originalText/11.1 - Example 3.2 Invoice checking process. - originalText.txt")));
        //similarityDataList.add(getSimilarityData(new File("src/main/others/TestData/originalText/11.11 - Exercise 4.4 Model the following process fragment. - originalText.txt")));

        // TEST
        //similarityDataList.add(getSimilarityData(new File("/Users/thanner/IdeaProjects/bpmparser/src/main/others/TestData/originalText/11.19 - Exercise 4.16 Model the following business process snippet. - originalText.txt")));

        similarityDataList = SimilarityDataGED.prepareSimilarityList(similarityDataList);
        similarityDataList.add(getSimilarityTotal(similarityDataList));

        TableGenerator tableGenerator = new SimilarityGEDTableGenerator(similarityDataList);
        String table = tableGenerator.generateTable();
        System.out.println(table);
    }

    private static SimilarityDataGED getSimilarityData(File fileOriginalText) {
        SimilarityDataGED similarityData = new SimilarityDataGED();
        String inputFileName = FilenameUtils.removeExtension(fileOriginalText.getName());
        similarityData.setTextName(inputFileName);

        FileNameHandler fileNameHandler = new FileNameHandler(inputFileName);
        String fileName = fileNameHandler.getIndex() + " - " + fileNameHandler.getName();

        try {
            System.out.println(inputFileName);
            String originalText = FileUtils.readFileToString(fileOriginalText, "UTF-8");
            if (originalText != null && !originalText.isEmpty()) {
                TDefinitions originalModel = ValidationUtil.getOriginalModel(fileOriginalText);
                if (originalModel != null) {

                    TDefinitions processFromOriginalText = ValidationUtil.getModelFromOriginalText(originalText, fileName);
                    similarityData.setSimilarityOriginalText(calculateSimilarity(originalModel, processFromOriginalText, true));

                    TDefinitions modelCurrentApproachFromText = getModelCurrentApproachFromText(processFromOriginalText, fileName);
                    similarityData.setSimilarityCurrentApproachFromText(calculateSimilarity(originalModel, modelCurrentApproachFromText, true));

                    TDefinitions modelCurrentApproachFromModel = getModelCurrentApproachFromModel(originalModel, fileName);
                    similarityData.setSimilarityCurrentApproachFromModel(calculateSimilarity(originalModel, modelCurrentApproachFromModel, true));

                    /**
                     TDefinitions modelOriginalApproachFromText = new TDefinitions();
                     similarityData.setSimilarityOriginalApproachFromText(calculateSimilarity(originalModel, modelOriginalApproachFromText));

                     TDefinitions modelOriginalApproachFromModel = new TDefinitions();
                     similarityData.setSimilarityOriginalApproachFromModel(calculateSimilarity(originalModel, modelOriginalApproachFromModel));
                     **/
                } else {
                    System.out.println("\tOriginal model is null");
                }
            } else {
                System.out.println("\tOriginal text is null or empty");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return similarityData;
    }

    private static TDefinitions getModelCurrentApproachFromText(TDefinitions processFromOriginalText, String processName) {
        File textFile = new File(Paths.LocalTestDataPath + Paths.currentApproachTextFromOriginalTextPath + processName + ".txt");
        String pureText = ValidationUtil.getPureText(processFromOriginalText, textFile);
        if (pureText.isEmpty()) {
            System.out.println("\t\tTextWriter problem: Could not generate text from generated process (1 - getModelCurrentApproachFromText)");
        }

        File modelFile = new File(Paths.LocalTestDataPath + Paths.currentApproachModelFromOriginalTextPath + processName + ".bpmn");
        TDefinitions process = ValidationUtil.getProcess(pureText, modelFile);
        if (process == null) {
            System.out.println("\t\tTextReader problem: Could not generate process from current approach text (1 - getModelCurrentApproachFromText)");
        }

        return process;
    }

    private static TDefinitions getModelCurrentApproachFromModel(TDefinitions originalModel, String processName) {
        File textFile = new File(Paths.LocalTestDataPath + Paths.currentApproachTextFromOriginalModelPath + processName + ".txt");
        String pureText = ValidationUtil.getPureText(originalModel, textFile);
        if (pureText.isEmpty()) {
            System.out.println("\t\tTextWriter problem: Could not generate text from original model (2 - getModelCurrentApproachFromModel)");
        }

        File modelFile = new File(Paths.LocalTestDataPath + Paths.currentApproachModelFromOriginalModel + processName + ".bpmn");
        TDefinitions process = ValidationUtil.getProcess(pureText, modelFile);
        if (process == null) {
            System.out.println("\t\tTextReader problem: Could not generate process from current approach text (2 - getModelCurrentApproachFromModel)");
        }

        return process;
    }

    public static double calculateSimilarity(TDefinitions modelOriginal, TDefinitions modelGenerated, boolean needRefine) {
        if (modelOriginal == null || modelGenerated == null) {
            return 0.0;
        } else {
            if (needRefine) {
                modelOriginal = ProcessModelRefine.refineProcess(modelOriginal);
                modelGenerated = ProcessModelRefine.refineProcess(modelGenerated);
            }

            double similarity = 0.0;
            try {
                ActivityContextBuilder builderOriginal = new ActivityContextBuilder();
                List<ActivityContextFragment> listOriginal = builderOriginal.convertWithContext(modelOriginal);
                Map<TFlowNode, Integer> idMapModelOriginal = builderOriginal.getIdMap();

                ActivityContextBuilder builderGenerated = new ActivityContextBuilder();
                List<ActivityContextFragment> listGenerated = builderGenerated.convertWithContext(modelGenerated);
                Map<TFlowNode, Integer> idMapModelGenerated = builderGenerated.getIdMap();

                /*
                SimilarityCalculator similarityCalculator = new SimilarityCalculator(0, 1, 0, 0.5);
                SimilarityCalculator contextCalculator = new SimilarityCalculator(0, 0, 0, 0);
                similarityCalculator.setContextSimilarityCalculator(contextCalculator);

                //System.out.println("Test 1");
                //printStructuralMapping(idMapModelOriginal, idMapModelGenerated);

                double[] simMatrix = similarityCalculator.getSimilarityMatrix(listOriginal, listGenerated);
                int[] bestMappingArray = similarityCalculator.getBestPossibleMapping(simMatrix, listOriginal, listGenerated);

                idMapModelGenerated = makeMapping(idMapModelGenerated, bestMappingArray);

                //System.out.println("Test 2");
                //printStructuralMapping(idMapModelOriginal, idMapModelGenerated);
                */

                SimilarityHandler similarityHandler = new SimilarityHandler();
                SimpleGraph simpleGraphOriginal = similarityHandler.generateSimpleGraph(modelOriginal, idMapModelOriginal);
                //System.out.println("=============================== Original");
                //System.out.println(simpleGraphOriginal);
                SimpleGraph simpleGraphGenerated = similarityHandler.generateSimpleGraph(modelGenerated, idMapModelGenerated);
                //System.out.println("=============================== Generated");
                //System.out.println(simpleGraphGenerated);

                similarityHandler.compute(simpleGraphOriginal, simpleGraphGenerated);
                similarity = similarityHandler.getSimilarity();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return similarity;
        }
    }

    public static void configureSystem() {
        // No artigo: 0.1, 0.4, 0.9
        // Acho que o outro artigo: 0.1, 0.2, 0.8
        // Tem também 1, 1, 1
        // 0.1, 0.1, 0.8 apresentou excelentes resultados

        //Bons resulttados: 0.1.0.2,0.9

        configureSystem(0.1, 0.2, 0.8, 0.001);
    }

    public static void configureSystem(double weightSkippedVertices, double weightskippedEdges, double weightsubstitutedVertices, double notConsideredCutOffSimilarity) {
        SimilarityHandler.setWeightskippedVertices(weightSkippedVertices);
        SimilarityHandler.setWeightskippedEdges(weightskippedEdges);
        SimilarityHandler.setWeightsubstitutedVertices(weightsubstitutedVertices);
        SimilarityHandler.setNotConsideredCutOffSimilarity(notConsideredCutOffSimilarity);
    }

    public static String intToString(int num, int digits) {
        StringBuilder output = new StringBuilder(Integer.toString(num));
        while (output.length() < digits) output.insert(0, "0");
        return output.toString();
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private static SimilarityDataGED getSimilarityTotal(List<SimilarityDataGED> similarityDataList) {
        SimilarityDataGED similarityData = new SimilarityDataGED();
        similarityData.setTextName("Total");

        double similarityOriginalText = 0.0;
        double similarityCurrentApproachFromModel = 0.0;
        double similarityCurrentApproachFromText = 0.0;
        double similarityOriginalApproachFromModel = 0.0;
        double similarityOriginalApproachFromText = 0.0;

        for (SimilarityDataGED s : similarityDataList) {
            similarityOriginalText += s.getSimilarityOriginalText();
            similarityCurrentApproachFromModel += s.getSimilarityCurrentApproachFromModel();
            similarityCurrentApproachFromText += s.getSimilarityCurrentApproachFromText();
            similarityOriginalApproachFromModel += s.getSimilarityOriginalApproachFromModel();
            similarityOriginalApproachFromText += s.getSimilarityOriginalApproachFromText();
        }

        int amountElements = similarityDataList.size();

        similarityData.setSimilarityOriginalText(similarityOriginalText / amountElements);
        similarityData.setSimilarityCurrentApproachFromModel(similarityCurrentApproachFromModel / amountElements);
        similarityData.setSimilarityCurrentApproachFromText(similarityCurrentApproachFromText / amountElements);
        similarityData.setSimilarityOriginalApproachFromModel(similarityOriginalApproachFromModel / amountElements);
        similarityData.setSimilarityOriginalApproachFromText(similarityOriginalApproachFromText / amountElements);

        return similarityData;
    }

    private static Map<TFlowNode, Integer> makeMapping(Map<TFlowNode, Integer> idMapModelGenerated, int[] bestMappingArray) {
        Map<TFlowNode, Integer> newIdMapModelGenerated = new HashMap<>();
        for (int index = 0; index < bestMappingArray.length; index++) {
            int originalModelId = index;
            int generateModelId = bestMappingArray[index];
            Set<TFlowNode> keySet = getKeysByValue(idMapModelGenerated, generateModelId);
            for (TFlowNode tFlowNode : keySet) {
                newIdMapModelGenerated.put(tFlowNode, originalModelId);
            }
        }

        int i = bestMappingArray.length;
        for (Map.Entry<TFlowNode, Integer> entry : idMapModelGenerated.entrySet()) {
            if (!newIdMapModelGenerated.containsKey(entry.getKey())) {
                newIdMapModelGenerated.put(entry.getKey(), i++);
            }
        }
        return newIdMapModelGenerated;
    }

    private void printStructuralMapping(Map<TFlowNode, Integer> idMapModelOriginal, Map<TFlowNode, Integer> idMapModelGenerated) {
        System.out.println("Structural - Mapping:");
        List<String> strings = new ArrayList<>();
        for (Map.Entry<TFlowNode, Integer> entry : idMapModelOriginal.entrySet()) {
            Set<TFlowNode> elementsModelGenerated = getKeysByValue(idMapModelGenerated, entry.getValue());
            String labelOriginal = entry.getKey().getName();
            for (TFlowNode tFlowNode : elementsModelGenerated) {
                strings.add(intToString(entry.getValue(), 2) + ": " + labelOriginal + " -> " + tFlowNode.getName());
            }
        }
        Collections.sort(strings);
        strings.forEach(System.out::println);
        System.out.println("===============");
    }

    private void printGraphEditDistanceMapping(SimilarityHandler similarityHandler) {
        System.out.println("Graph Edit Distance - Mapping:");
        System.out.println(similarityHandler.getMapping());
        System.out.println("---------------------");
    }

}
