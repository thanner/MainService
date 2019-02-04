package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.util.List;

public class Lemmatization {

    public static void applyLemma(Document document) {
        for (int sentenceId = 0; sentenceId < document.sentences().size(); sentenceId++) {
            Sentence sentence = document.sentences().get(sentenceId);
            document.sentences().set(sentenceId, applyLemma(sentence));
        }
    }

    public static Sentence applyLemma(Sentence sentence) {
        List<String> lemmas = sentence.lemmas();
        return new Sentence(lemmas);
    }

}
