package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation;

public class Activity extends Representation {

    private SentenceText referenceSentence;
    private double similarityReference = 0.0;

    public Activity(String value) {
        setValue(value);
    }

    public SentenceText getReferenceSentence() {
        return referenceSentence;
    }

    public void setReferenceSentence(SentenceText referenceSentence) {
        this.referenceSentence = referenceSentence;
    }

    public double getSimilarityReference() {
        return similarityReference;
    }

    public void setSimilarityReference(double similarityReference) {
        this.similarityReference = similarityReference;
    }

    public String toString() {
        return this.getValue();
    }

}
