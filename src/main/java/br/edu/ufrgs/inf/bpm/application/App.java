package br.edu.ufrgs.inf.bpm.application;

import br.edu.ufrgs.inf.bpm.converter.StructuredXmlGenerator;
import br.edu.ufrgs.inf.bpm.rest.processVerification.ProcessVerificationClient;
import br.edu.ufrgs.inf.bpm.rest.textReader.TextReaderClient;
import br.edu.ufrgs.inf.bpm.rest.textWriter.TextWriterClient;
import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;
import br.edu.ufrgs.inf.bpm.verificationmessages.TBpmnVerification;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class App {

    private static String textPath = "src/main/others/testData/dissertation-example.txt";
    private static String bpmnPath = "src/main/others/testData/bpmn/diagram2.bpmn";

    public static void main(String[] args) {
        String processBpmn = "";
        try {
            processBpmn = FileUtils.readFileToString(new File(bpmnPath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        processBpmn = textReader();

        TTextMetadata processText = textWriter(processBpmn);

        TBpmnVerification verificationElements = bpmnVerification(processBpmn);

        StructuredXmlGenerator structuredXmlGenerator = new StructuredXmlGenerator();
        String xml = structuredXmlGenerator.generateStructuredXml(processText);

        System.out.println("XML");
        System.out.println(xml);
    }

    public static String textReader() {
        TextReaderClient textReaderClient = new TextReaderClient();
        String text = null;
        String processBpmn = "";
        try {
            text = FileUtils.readFileToString(new File(textPath), StandardCharsets.UTF_8);
            processBpmn = textReaderClient.getProcess(text);
            System.out.println("Representation reader: Process BPMN");
            System.out.println(processBpmn);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processBpmn;
    }

    public static TTextMetadata textWriter(String processBpmn) {
        TextWriterClient textWriterClient = new TextWriterClient();
        TTextMetadata processText = new TTextMetadata();
        try {
            processText = textWriterClient.getText(processBpmn);
            System.out.println("Representation writer: Representation");
            System.out.println(processText);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processText;
    }

    public static TBpmnVerification bpmnVerification(String processBpmn) {
        ProcessVerificationClient processVerificationClient = new ProcessVerificationClient();
        TBpmnVerification verificationElements = new TBpmnVerification();
        try {
            verificationElements = processVerificationClient.getVerification(processBpmn);
            System.out.println("BPMN verification: Verifications");
            System.out.println(verificationElements);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verificationElements;
    }

}
