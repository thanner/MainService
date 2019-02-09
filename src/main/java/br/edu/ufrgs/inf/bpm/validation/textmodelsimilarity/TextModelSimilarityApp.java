package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity;

import br.edu.ufrgs.inf.bpm.util.Paths;
import br.edu.ufrgs.inf.bpm.validation.latex.SimilarityTMTableGenerator;
import br.edu.ufrgs.inf.bpm.validation.latex.TableGenerator;
import br.edu.ufrgs.inf.bpm.validation.similarityData.SimilarityDataTM;
import br.edu.ufrgs.inf.bpm.validation.util.FileNameHandler;
import br.edu.ufrgs.inf.bpm.validation.util.ValidationUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextModelSimilarityApp {

    public static void main(String[] args) {
        ValidationUtil.removeEmptyFiles();

        List<SimilarityDataTM> similarityDataList = new ArrayList<>();
        File folder = new File(Paths.LocalTestDataPath + Paths.dataOriginalTextPath);
        /*
        for (File originalTextFile : Objects.requireNonNull(folder.listFiles())) {
            if(ValidationUtil.isTextFile(originalTextFile)) {
                similarityDataList.add(getSimilarityData(originalTextFile));
            }
        }
        (*/
        // TEST
        similarityDataList.add(getSimilarityData(new File(Paths.LocalTestDataPath + Paths.dataOriginalTextPath + "9.6 - Exercise 5 - originalText.txt")));

        similarityDataList = SimilarityDataTM.prepareSimilarityList(similarityDataList);

        TableGenerator tableGenerator = new SimilarityTMTableGenerator(similarityDataList);
        String table = tableGenerator.generateTable();
        System.out.println(table);
    }

    private static SimilarityDataTM getSimilarityData(File fileOriginalText) {
        SimilarityDataTM similarityData = new SimilarityDataTM();
        String inputFileName = FilenameUtils.removeExtension(fileOriginalText.getName());
        similarityData.setTextName(inputFileName);

        FileNameHandler fileNameHandler = new FileNameHandler(inputFileName);
        String fileName = fileNameHandler.getIndex() + " - " + fileNameHandler.getName();

        try {
            System.out.println(inputFileName);
            String originalText = FileUtils.readFileToString(fileOriginalText, "UTF-8");
            TDefinitions originalModel = ValidationUtil.getOriginalModel(fileOriginalText);
            if (originalModel != null) {
                TDefinitions processFromOriginalText = ValidationUtil.getModelFromOriginalText(originalText, fileName);
                if (processFromOriginalText != null) {
                    if (originalText != null) {
                        SimilarityCalculator similarityCalculator0 = new SimilarityCalculator();
                        similarityData.setSimTextFromOriginalText(similarityCalculator0.calculateSimilarityProcess(originalText, originalModel));
                    }

                    String textFromText = getTextFromText(originalModel, fileName);
                    if (textFromText != null) {
                        SimilarityCalculator similarityCalculator = new SimilarityCalculator();
                        similarityData.setSimTextFromText(similarityCalculator.calculateSimilarityProcess(textFromText, originalModel));
                    }

                    String textFromModel = getTextFromModel(processFromOriginalText, fileName);
                    if (textFromModel != null) {
                        SimilarityCalculator similarityCalculator2 = new SimilarityCalculator();
                        similarityData.setSimTextFromModel(similarityCalculator2.calculateSimilarityProcess(textFromModel, originalModel));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return similarityData;
    }

    private static String getTextFromText(TDefinitions processFromOriginalText, String processName) {
        File newFile = new File(Paths.LocalTestDataPath + Paths.currentApproachTextFromOriginalTextPath + processName + ".txt");
        String pureText = ValidationUtil.getPureText(processFromOriginalText, newFile);
        if (pureText == null || pureText.isEmpty()) {
            System.out.println("\t\tTextWriter problem: Could not generate text from generated process");
        }
        return pureText;

        /*
        TextWriterClient textWriterClient = new TextWriterClient();
        try {
            String generatedProcess = JaxbWrapper.convertToXML(processFromOriginalText);
            TTextMetadata tMetaText = textWriterClient.getText(generatedProcess);
            MetaTextWrapper metaTextWrapper = new MetaTextWrapper(tMetaText);
            String pureText = metaTextWrapper.getPureText();
            File newFile = new File("src/main/others/TestData/currentApproachTextFromOriginalText/" + processName + ".txt");
            FileUtils.writeStringToFile(newFile, pureText, "UTF-8");
            return pureText;
        } catch (Exception e) {
            return null;
        }
        */
    }

    private static String getTextFromModel(TDefinitions originalModel, String processName) {
        File newFile = new File(Paths.LocalTestDataPath + Paths.currentApproachTextFromOriginalModelPath + processName + ".txt");
        String pureText = ValidationUtil.getPureText(originalModel, newFile);
        if (pureText == null || pureText.isEmpty()) {
            System.out.println("\t\tTextWriter problem: Could not generate text from generated process");
        }
        return pureText;
        /*
        TextWriterClient textWriterClient = new TextWriterClient();
        try {
            TTextMetadata tMetaText = textWriterClient.getText(JaxbWrapper.convertToXML(originalModel));
            MetaTextWrapper metaTextWrapper = new MetaTextWrapper(tMetaText);
            String pureText = metaTextWrapper.getPureText();
            File filePureText = new File("src/main/others/TestData/currentApproachTextFromOriginalModel/" + processName + ".txt");
            FileUtils.writeStringToFile(filePureText, pureText, "UTF-8");
            return pureText;
        } catch (Exception e) {
            return null;
        }
        */
    }

}
