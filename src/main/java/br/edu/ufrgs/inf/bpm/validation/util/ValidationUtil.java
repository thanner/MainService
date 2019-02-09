package br.edu.ufrgs.inf.bpm.validation.util;

import br.edu.ufrgs.inf.bpm.rest.textReader.TextReaderClient;
import br.edu.ufrgs.inf.bpm.rest.textWriter.TextWriterClient;
import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;
import br.edu.ufrgs.inf.bpm.util.Paths;
import br.edu.ufrgs.inf.bpm.wrapper.JaxbWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ValidationUtil {

    private static TextWriterClient textWriterClient = new TextWriterClient();
    private static TextReaderClient textReaderClient = new TextReaderClient();
    private static boolean verifyOnlyNewFiles = true;

    private static List<File> getFolderList() {
        List<File> folderList = new ArrayList<>();

        folderList.add(new File(Paths.LocalTestDataPath + Paths.dataOriginalProcessPath));
        folderList.add(new File(Paths.LocalTestDataPath + Paths.modelFromOriginalTextPath));
        folderList.add(new File(Paths.LocalTestDataPath + Paths.currentApproachTextFromOriginalTextPath));
        folderList.add(new File(Paths.LocalTestDataPath + Paths.currentApproachModelFromOriginalTextPath));
        folderList.add(new File(Paths.LocalTestDataPath + Paths.currentApproachTextFromOriginalModelPath));
        folderList.add(new File(Paths.LocalTestDataPath + Paths.currentApproachModelFromOriginalModel));

        return folderList;
    }

    public static void removeEmptyFiles() {
        for (File folder : getFolderList()) {
            removeEmptyFiles(folder);
        }
    }

    public static void removeEmptyFiles(File folder) {
        try {
            Iterator iterator = Arrays.stream(Objects.requireNonNull(folder.listFiles())).iterator();
            while (iterator.hasNext()) {
                File file = (File) iterator.next();
                String text = FileUtils.readFileToString(file, "UTF-8");
                if (text.isEmpty()) {
                    FileUtils.forceDelete(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TDefinitions getOriginalModel(File fileOriginalText) {
        try {
            FileNameHandler fileNameHandler = new FileNameHandler(fileOriginalText.getName());
            String processName = fileNameHandler.getIndex() + " - " + fileNameHandler.getName() + " - originalProcess.bpmn";
            String originalModelPath = Paths.LocalTestDataPath + Paths.dataOriginalProcessPath + processName;
            String bpmnProcess = FileUtils.readFileToString(new File(originalModelPath), "UTF-8");
            return JaxbWrapper.convertXMLToObject(bpmnProcess);
        } catch (IOException e) {
            return null;
        }
    }

    public static TDefinitions getModelFromOriginalText(String originalText, String processName) {
        File modelFile = new File(Paths.LocalTestDataPath + Paths.modelFromOriginalTextPath + processName + ".bpmn");
        TDefinitions process = ValidationUtil.getProcess(originalText, modelFile);
        if (process == null) {
            System.out.println("\tCould not generate process from original text");
        }

        return process;
    }

    public static String getPureText(TDefinitions process, File textFile) {
        String pureText = "";
        try {
            MetaTextWrapper metaTextWrapper = null;
            if (verifyOnlyNewFiles && textFile.exists()) {
                System.out.println("\t\tText already exists.");
                pureText = FileUtils.readFileToString(textFile, "UTF-8");
            } else {
                String generatedProcess = JaxbWrapper.convertToXML(process);
                TTextMetadata tMetaText = textWriterClient.getText(generatedProcess);
                if (tMetaText != null) {
                    metaTextWrapper = new MetaTextWrapper(tMetaText);
                    pureText = metaTextWrapper.getPureText();
                    FileUtils.writeStringToFile(textFile, pureText, "UTF-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pureText;
    }

    public static TDefinitions getProcess(String pureText, File modelFile) {
        TDefinitions process = null;

        try {
            String processString;
            if (verifyOnlyNewFiles && modelFile.exists()) {
                System.out.println("\t\tProcess already exists.");
                processString = FileUtils.readFileToString(modelFile, "UTF-8");
            } else {
                processString = textReaderClient.getProcess(pureText);
                if (!processString.isEmpty()) {
                    FileUtils.writeStringToFile(modelFile, processString, "UTF-8");
                }
            }
            if (!processString.isEmpty()) {
                process = JaxbWrapper.convertXMLToObject(processString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return process;
    }

    public static boolean isTextFile(File originalTextFile) {
        return FilenameUtils.getExtension(originalTextFile.getName()).equals("txt");
    }

}
