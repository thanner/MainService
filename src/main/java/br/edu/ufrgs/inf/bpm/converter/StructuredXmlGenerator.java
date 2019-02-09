package br.edu.ufrgs.inf.bpm.converter;

import br.edu.ufrgs.inf.bpm.textmetadata.*;
import br.edu.ufrgs.inf.bpm.verificationmessages.TBpmnVerification;
import br.edu.ufrgs.inf.bpm.verificationmessages.TMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructuredXmlGenerator {

    TTextMetadata tMetaText;

    public String generateStructuredXml(TTextMetadata tMetaText) {
        this.tMetaText = tMetaText;
        StringBuilder structuredXml = new StringBuilder();

        structuredXml.append("<text>");
        if (tMetaText != null) {
            for (TSentence sentence : tMetaText.getText().getSentenceList()) {
                StringBuilder setenceXML = new StringBuilder();

                String sentenceText = sentence.getValue().trim().replaceAll("\"", "&quot;");

                int level = 0;
                if (sentence.getSnippetList().size() > 0) {
                    level = sentence.getSnippetList().get(0).getLevel();
                }

                setenceXML.append("<sentence ")
                        .append("level=\"").append(level).append("\" ")
                        .append("isLateral=\"").append(sentence.isNewSplitPath()).append("\" ")
                        .append("value=\"").append(sentenceText).append(" \" ") // &quot;
                        .append(">")
                        .append(generateStructuredSnippet(sentence.getSnippetList()))
                        .append("</sentence>");

                structuredXml.append(setenceXML);
            }
        }
        structuredXml.append("</text>");

        return structuredXml.toString();
    }

    private String generateStructuredSnippet(List<TSnippet> tSnippetList) {
        StringBuilder snippetXML = new StringBuilder();
        for (TSnippet tSnippet : tSnippetList) {
            snippetXML.append("<snippet ")
                    .append("processElementId=\"").append(tSnippet.getProcessElementId()).append("\" ")
                    .append("processElementType=\"").append(tSnippet.getProcessElementType()).append("\" ")
                    .append("resource=\"").append(getResourceNameById(tSnippet.getResourceId())).append("\" ")
                    .append("resourceId=\"").append(tSnippet.getResourceId()).append("\" ")
                    .append("process=\"").append(getProcessNameByResourceId(tSnippet.getResourceId())).append("\" ")
                    .append("processId=\"").append(getProcessIdByResourceId(tSnippet.getResourceId())).append("\" ")
                    .append("startIndex=\"").append(tSnippet.getStartIndex()).append("\" ")
                    .append("endIndex=\"").append(tSnippet.getEndIndex()).append("\" ")
                    .append("/>");

        }

        return snippetXML.toString();
    }

    private String getResourceNameById(String resourceId) {
        for (TProcess tProcess : tMetaText.getProcessList()) {
            for (TResource currentResource : tProcess.getResourceList()) {
                if (currentResource.getId().equals(resourceId)) {
                    return currentResource.getName();
                }
            }
        }
        return "";
    }

    private String getProcessIdByResourceId(String resourceId) {
        for (TProcess currentProcess : tMetaText.getProcessList()) {
            for (TResource currentResource : currentProcess.getResourceList()) {
                if (currentResource.getId().equals(resourceId)) {
                    return currentProcess.getId();
                }
            }
        }
        return "";
    }

    private String getProcessNameByResourceId(String resourceId) {
        for (TProcess currentProcess : tMetaText.getProcessList()) {
            for (TResource currentResource : currentProcess.getResourceList()) {
                if (currentResource.getId().equals(resourceId)) {
                    return currentProcess.getName();
                }
            }
        }
        return "";
    }

    public String generateVerificationMessages(TBpmnVerification verificationElements) {
        StringBuilder errorsXML = new StringBuilder();

        Map<String, List<TMessage>> errorsMap = createErrorsMap(verificationElements);
        errorsXML.append("<verificationMessages>");
        for (Map.Entry<String, List<TMessage>> elementError : errorsMap.entrySet()) {
            errorsXML.append("<element processElementId=\"").append(elementError.getKey()).append("\"> ");
            for (TMessage tMessage : elementError.getValue()) {
                String description = tMessage.getDescription().replaceAll("\"", "-"); // &quot;
                errorsXML.append("<message ")
                        .append("description=\"").append(description).append("\" ")
                        .append("source=\"").append(tMessage.getSource()).append("\" ")
                        .append("messageType=\"").append(tMessage.getMessageType()).append("\"")
                        .append("/>");
            }
            errorsXML.append("</element>");
        }

        errorsXML.append("</verificationMessages>");

        return errorsXML.toString();
    }

    private Map<String, List<TMessage>> createErrorsMap(TBpmnVerification verificationElements) {
        Map<String, List<TMessage>> errorsMap = new HashMap<>();
        if (verificationElements != null) {
            for (TMessage message : verificationElements.getMessageList()) {
                if (!errorsMap.containsKey(message.getProcessElementId())) {
                    errorsMap.put(message.getProcessElementId(), new ArrayList<>());
                }
                errorsMap.get(message.getProcessElementId()).add(message);
            }
        }
        return errorsMap;
    }

}
