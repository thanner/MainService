package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity;

import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation.Activity;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation.Representation;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.representation.SentenceText;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.util.*;

public class SimilarityCalculator {

    private ILexicalDatabase db;
    private RelatednessCalculator lin;
    private Map<String, Double> idfMap;

    public SimilarityCalculator() {
        db = new NictWordNet();
        lin = new Lin(db);
        idfMap = new HashMap<>();
    }

    public double calculateSimilarityProcess(String text, TDefinitions definitions) {
        List<SentenceText> sentenceList = TokensPreparation.prepareText(text);
        List<Activity> activityList = TokensPreparation.prepareModel(definitions);

        idfMap = makeIDFMap(activityList, sentenceList);

        // Reports to the activities which sentence has the highest similarity
        for (Activity activity : activityList) {
            for (SentenceText sentence : sentenceList) {
                double similarity = calculateSimilarity(activity, sentence);
                if (similarity > activity.getSimilarityReference()) {
                    activity.setSimilarityReference(similarity);
                    activity.setReferenceSentence(sentence);
                }
            }
        }

        // Calculates the similarity of the process from the similarity of the list of activities
        double similarityProcess = 0.0;
        for (Activity activity : activityList) {
            similarityProcess += activity.getSimilarityReference();
        }
        similarityProcess = similarityProcess / activityList.size();

        return similarityProcess;
    }

    private Map<String, Double> makeIDFMap(List<Activity> activityList, List<SentenceText> sentenceList) {
        int totalDocuments = activityList.size() + sentenceList.size();

        Map<String, Integer> amountMap = new HashMap<>();
        populateMapWithActivityList(activityList, amountMap);
        populateMapWithSentenceList(sentenceList, amountMap);

        Map<String, Double> idfMap = new HashMap<>();
        for (Map.Entry<String, Integer> amountMapEntry : amountMap.entrySet()) {
            idfMap.put(amountMapEntry.getKey(), Math.log((double) totalDocuments / amountMapEntry.getValue()));
        }

        return idfMap;
    }

    private void populateMapWithActivityList(List<Activity> activityList, Map<String, Integer> amountMap) {
        Set<String> usedWords = new HashSet<>();
        for (Activity representation : activityList) {
            populateMap(amountMap, usedWords, representation.getBagOfWords());
        }
    }

    private void populateMapWithSentenceList(List<SentenceText> sentenceList, Map<String, Integer> amountMap) {
        Set<String> usedWords = new HashSet<>();
        for (SentenceText representation : sentenceList) {
            populateMap(amountMap, usedWords, representation.getBagOfWords());
        }
    }

    private void populateMap(Map<String, Integer> amountMap, Set<String> usedWords, List<String> bagOfWords) {
        for (String word : bagOfWords) {
            if (!usedWords.contains(word)) {
                usedWords.add(word);
                if (amountMap.containsKey(word)) {
                    amountMap.put(word, amountMap.get(word) + 1);
                } else {
                    amountMap.put(word, 1);
                }
            }
        }
    }

    private double calculateSimilarity(Activity activity, SentenceText sentence) {
        Double similarityActivity = getNumerator(activity, sentence) / getDenominator(activity);
        similarityActivity = !similarityActivity.isNaN() ? similarityActivity : 1.0;
        Double similaritySentence = getNumerator(sentence, activity) / getDenominator(sentence);
        similaritySentence = !similaritySentence.isNaN() ? similaritySentence : 1.0;

        return 0.5 * (similarityActivity + similaritySentence);
    }

    private double getNumerator(Representation representation1, Representation representation2) {
        double numerator = 0.0;
        for (String word : representation1.getBagOfWords()) {
            double maxSemanticSimilarityWord = calculateMaxSemanticSimilarity(word, representation2.getBagOfWords());
            double idf = getIDF(word);
            numerator += (maxSemanticSimilarityWord * idf);
        }
        return numerator;
    }

    private double getDenominator(Representation representation1) {
        double denominator = 0.0;
        for (String word : representation1.getBagOfWords()) {
            denominator += getIDF(word);
        }
        return denominator;
    }

    private double calculateMaxSemanticSimilarity(String word, List<String> bagOfWords) {
        double maxSimilarity = 0.0;
        for (String word2 : bagOfWords) {
            double similarity = calculateSemanticSimilarity(word, word2);
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
            }
        }
        return maxSimilarity;
    }

    private double calculateSemanticSimilarity(String word1, String word2) {
        POS posWord1 = POS.n;
        POS posWord2 = POS.n;
        double similarity = 0.0;

        WS4JConfiguration.getInstance().setMFS(true);

        List<Concept> synsets1 = (List<Concept>) db.getAllConcepts(word1, posWord1.name());
        List<Concept> synsets2 = (List<Concept>) db.getAllConcepts(word2, posWord2.name());

        for (Concept synset1 : synsets1) {
            for (Concept synset2 : synsets2) {
                Relatedness relatedness = lin.calcRelatednessOfSynset(synset1, synset2);
                double score = relatedness.getScore();
                if (score > similarity) {
                    similarity = score;
                }
            }
        }

        if (similarity == -1D) {
            similarity = 0.0;
        }

        return similarity;
    }

    private double getIDF(String word) {
        return idfMap.get(word);
    }

}
