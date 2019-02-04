package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.coreference.term;

public class Term {

    private String label;
    private int sentenceId;
    private int startIndex;
    private int endIndex;

    public Term(String label, int sentenceId, int startIndex, int endIndex) {
        this.label = label;
        this.sentenceId = sentenceId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

}
