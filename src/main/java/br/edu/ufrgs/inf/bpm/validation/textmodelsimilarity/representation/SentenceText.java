package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation;

public class SentenceText extends Representation {

    public SentenceText(String sentence) {
        setValue(sentence);
    }

    public String toString() {
        return this.getValue();
    }

}
