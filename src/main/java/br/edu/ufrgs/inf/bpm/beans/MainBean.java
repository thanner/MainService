package br.edu.ufrgs.inf.bpm.beans;

import br.edu.ufrgs.inf.bpm.converter.StructuredXmlGenerator;
import br.edu.ufrgs.inf.bpm.rest.processVerification.ProcessVerificationClient;
import br.edu.ufrgs.inf.bpm.rest.textReader.TextReaderClient;
import br.edu.ufrgs.inf.bpm.rest.textWriter.TextWriterClient;
import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;
import br.edu.ufrgs.inf.bpm.verificationmessages.TBpmnVerification;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

@ManagedBean
@SessionScoped
public class MainBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String unstructuredText;
    private String unstructuredMetaText;
    private String structuredText;
    private String errorsXml;
    private String bpmnFile;
    private Part file;

    private boolean fileType;

    public boolean getFileType() {
        return fileType;
    }

    public void setFileType(boolean fileType) {
        this.fileType = fileType;
    }

    public String getUnstructuredText() {
        return unstructuredText;
    }

    public void setUnstructuredText(String unstructuredText) {
        this.unstructuredText = unstructuredText;
    }

    public String getUnstructuredMetaText() {
        return unstructuredMetaText;
    }

    public void setUnstructuredMetaText(String unstructuredMetaText) {
        this.unstructuredMetaText = unstructuredMetaText;
    }

    public String getStructuredText() {
        return structuredText;
    }

    public void setStructuredText(String structuredText) {
        this.structuredText = structuredText;
    }

    public String getErrorsXml() {
        return errorsXml;
    }

    public void setErrorsXml(String errorsXml) {
        this.errorsXml = errorsXml;
    }

    public String getBpmnFile() {
        return bpmnFile;
    }

    public void setBpmnFile(String bpmnFile) {
        this.bpmnFile = bpmnFile;
    }

    public void clean() {
        this.unstructuredText = "";
        this.unstructuredMetaText = "";
        this.structuredText = "";
        this.bpmnFile = "";
        this.file = null;
        this.fileType = false;
    }

    public void convert() throws Exception {
        if (fileType) {
            convertBpmnFile();
        } else {
            convertText();
        }
    }

    public void convertText() throws Exception {
        if (!this.unstructuredText.isEmpty()) {
            TextReaderClient textToProcessClient = new TextReaderClient();
            this.bpmnFile = textToProcessClient.getProcess(this.unstructuredText);

            generateUnstructuredMetaText();
            generateStructuredText();
        } else {
            // FacesContext context = FacesContext.getCurrentInstance();
            // context.addMessage(null, new FacesMessage("Representation is empty."));
            setEmptyUnstructuredText();
        }
    }

    public void convertBpmnFile() throws Exception {
        if (!this.unstructuredText.isEmpty()) {
            this.bpmnFile = this.unstructuredText;

            setEmptyUnstructuredMetaText();
            generateStructuredText();
        } else {
            // FacesContext context = FacesContext.getCurrentInstance();
            // context.addMessage(null, new FacesMessage("BPMN file is empty."));
            setEmptyUnstructuredText();
        }
    }

    private void generateUnstructuredMetaText() throws Exception {
        TextReaderClient textToProcessClient = new TextReaderClient();
        TTextMetadata metaText = textToProcessClient.getMetaText(this.unstructuredText);

        StructuredXmlGenerator structuredXmlGenerator = new StructuredXmlGenerator();
        this.unstructuredMetaText = structuredXmlGenerator.generateStructuredXml(metaText);
    }

    private void generateStructuredText() throws Exception {
        TextWriterClient processToTextClient = new TextWriterClient();
        TTextMetadata metaText = processToTextClient.getText(this.bpmnFile);

        ProcessVerificationClient processVerificationClient = new ProcessVerificationClient();
        TBpmnVerification verificationElements = processVerificationClient.getVerification(this.bpmnFile);

        StructuredXmlGenerator structuredXmlGenerator = new StructuredXmlGenerator();
        this.structuredText = structuredXmlGenerator.generateStructuredXml(metaText);
        this.errorsXml = structuredXmlGenerator.generateVerificationMessages(verificationElements);
    }

    private void setEmptyUnstructuredMetaText() {
        this.unstructuredMetaText = "<text><sentence value=\"The original text does not exist.\"></sentence></text>";
    }

    private void setEmptyUnstructuredText() {
        this.unstructuredMetaText = "<text><sentence value=\"The original source does not exist.\"></sentence></text>";
        this.structuredText = "<text><sentence value=\"The original source does not exist.\"></sentence></text>";
        this.bpmnFile = "Empty source.";
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void cleanUpload() {
        this.structuredText = "";
        this.bpmnFile = "";
    }

    public void upload() {
        cleanUpload();
        if (fileType) {
            uploadBpmnFile();
        } else {
            uploadFile();
        }
    }

    public void uploadFile() {
        try {
            this.unstructuredText = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadBpmnFile() {
        try {
            this.unstructuredText = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
            this.bpmnFile = this.unstructuredText;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: AJAX
    public String getShowText() {
        //check if null?
        if ("".equals(unstructuredText) || unstructuredText == null) {
            return "";
        } else {
            return "Ajax message : Welcome " + unstructuredText;
        }
    }

}