package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.List;

public class TaggerManager {

    private static MaxentTagger maxentTagger;

    static {
        maxentTagger = new MaxentTagger(MaxentTagger.DEFAULT_JAR_PATH);
    }

    public static List<TaggedWord> tagSentence(List<HasWord> tokenList) {
        return maxentTagger.tagSentence(tokenList);
    }

}
