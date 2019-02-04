/*
package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LabelTokens {

    private List<List<String>> sentenceTokensList;

    public LabelTokens(Document document) {
        generateSentenceTokensList(document);
    }

    private void generateSentenceTokensList(Document document) {
        sentenceTokensList = new ArrayList<>();
        for (Sentence sentence : document.sentences()) {
            StringReader sentenceReader = new StringReader(sentence.toString());
            List<String> sentenceTokens = new ArrayList<>();
            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(sentenceReader, new CoreLabelTokenFactory(), "");
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                sentenceTokens.add(label.toString());
            }
            sentenceTokensList.add(sentenceTokens);
        }
    }

    public List<List<String>> getSentenceTokensList() {
        return sentenceTokensList;
    }

    public String getToken(int sentenceId, int tokenId) {
        return sentenceTokensList.get(sentenceId).get(tokenId);
    }

    public void setToken(int sentenceId, int tokenId, String newToken) {
        sentenceTokensList.get(sentenceId).set(tokenId, newToken);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int sentenceId = 1;
        for (List<String> sentenceTokens : sentenceTokensList) {
            stringBuilder.append("Sentence ").append(sentenceId).append(": ");
            int id = 1;
            for (String token : sentenceTokens) {
                stringBuilder.append("[").append(id).append(" - ").append(token).append("], ");
                id++;
            }
            stringBuilder.append("\n");
            sentenceId++;
        }
        return stringBuilder.toString();
    }

}
*/