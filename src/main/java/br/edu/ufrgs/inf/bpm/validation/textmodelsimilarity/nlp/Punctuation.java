package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.util.Iterator;

public class Punctuation {

    public static void removeDoubleQuote(Document document) {
        for (int sentenceId = 0; sentenceId < document.sentences().size(); sentenceId++) {
            Sentence sentence = document.sentences().get(sentenceId);
            document.sentences().set(sentenceId, removeDoubleQuote(sentence));
        }
    }

    public static Sentence removeDoubleQuote(Sentence sentence) {
        String words = sentence.toString().replaceAll("[\"]", "");
        return new Sentence(words);
    }

    public static void removePunctuation(Document document) {
        Iterator iterator = document.sentences().iterator();
        int sentenceId = 0;
        while (iterator.hasNext()) {
            Sentence sentence = (Sentence) iterator.next();
            String text = removePunctuation(sentence);
            if (!text.isEmpty()) {
                document.sentences().set(sentenceId, new Sentence(text));
                sentenceId++;
            } else {
                iterator.remove();
            }
        }
    }

    public static String removePunctuation(Sentence sentence) {
        String words = sentence.toString().replaceAll("[!?,.]", "");
        return words;
    }

}
