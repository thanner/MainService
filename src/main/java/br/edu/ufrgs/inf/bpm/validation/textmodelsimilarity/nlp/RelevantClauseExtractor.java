package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class RelevantClauseExtractor {

    // ADD 2 new: ADVP ("NEXT... / THEN...") and PP ("IN THE MEANTIME / AT THE SAME TIME)
    public static void extractRelevantClause(Document document) {
        for (int sentenceId = 0; sentenceId < document.sentences().size(); sentenceId++) {
            Sentence sentence = document.sentences().get(sentenceId);
            DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(sentence.toString()));
            for (List<HasWord> tokenList : tokenizer) {
                Tree tree = LexicalizedParserManager.parse(tokenList);
                List<String> POSTagList = Arrays.asList("SBAR", "ADVP", "PP");
                LexicalizedParserManager.removePOSTagList(tree, POSTagList);
                List<String> wordList = LexicalizedParserManager.getWordList(tree);
                document.sentences().set(sentenceId, new Sentence(wordList));
            }
        }
    }

}
