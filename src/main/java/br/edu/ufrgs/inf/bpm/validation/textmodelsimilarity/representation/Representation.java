package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Representation {

    private String value;
    private List<String> bagOfWords = new ArrayList<>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;

        StringReader sentenceReader = new StringReader(value);
        List<String> sentenceTokens = new ArrayList<>();
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(sentenceReader, new CoreLabelTokenFactory(), "");
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            sentenceTokens.add(label.toString());
        }
        bagOfWords = sentenceTokens;
    }

    public List<String> getBagOfWords() {
        return bagOfWords;
    }

}
