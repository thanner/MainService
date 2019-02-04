package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity;

import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.Lemmatization;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.Punctuation;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.RelevantClauseExtractor;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.StopWord;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.coreference.Coreference;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation.Activity;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation.SentenceText;
import br.edu.ufrgs.inf.bpm.wrapper.BpmnWrapper;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import org.omg.spec.bpmn._20100524.model.TActivity;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.util.ArrayList;
import java.util.List;

public class TokensPreparation {

    public static List<SentenceText> prepareText(String input) {
        Document document = new Document(input);

        Punctuation.removeDoubleQuote(document);

        //printDocument(document, "Original");

        // Coreference Resolution
        Coreference.makeCoreference(document);
        //printDocument(document, "Coreference");

        // Relevant clause extraction
        RelevantClauseExtractor.extractRelevantClause(document);
        //printDocument(document, "Clause extraction");

        // Remove punctuation
        Punctuation.removePunctuation(document);
        //printDocument(document, "Remove punctuation");

        // Remove stop words
        StopWord.removeStopWords(document);
        //printDocument(document, "Stop word");

        // Lemmatization
        Lemmatization.applyLemma(document);
        //printDocument(document, "Lemmatization");

        List<SentenceText> sentenceList = new ArrayList<>();
        for (Sentence sentence : document.sentences()) {
            sentenceList.add(new SentenceText(sentence.toString()));
        }

        return sentenceList;
    }

    public static List<Activity> prepareModel(TDefinitions definitions) {
        List<Activity> activityList = new ArrayList<>();

        BpmnWrapper bpmnWrapper = new BpmnWrapper(definitions);
        List<TActivity> tActivities = bpmnWrapper.getActivityList();
        for (TActivity activity : tActivities) {
            String label = activity.getName();
            if (label != null && !label.trim().isEmpty()) {
                Sentence sentence = new Sentence(label.trim());

                sentence = Punctuation.removeDoubleQuote(sentence);
                String textWithoutPunctuation = Punctuation.removePunctuation(sentence);
                if (!textWithoutPunctuation.isEmpty()) {
                    sentence = new Sentence(textWithoutPunctuation);
                    String textWithoutStopWords = StopWord.removeStopWords(sentence);
                    if (!textWithoutStopWords.isEmpty()) {
                        sentence = new Sentence(textWithoutStopWords);
                        sentence = Lemmatization.applyLemma(sentence);
                        activityList.add(new Activity(sentence.toString()));
                    }
                }
            }
        }

        return activityList;
    }

    private static void printDocument(Document document, String name) {
        System.out.println(name);
        for (Sentence sentence : document.sentences()) {
            System.out.println("\t" + sentence.toString());
        }
    }

}
