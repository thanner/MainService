package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StopWord {

    // Remove conjunctions, determiners and prepositions
    public static void removeStopWords(Document document) {
        Iterator iterator = document.sentences().iterator();
        int sentenceId = 0;
        while (iterator.hasNext()) {
            Sentence sentence = (Sentence) iterator.next();
            String text = removeStopWords(sentence);
            if (!text.isEmpty()) {
                document.sentences().set(sentenceId, new Sentence(text));
                sentenceId++;
            } else {
                iterator.remove();
            }
        }
    }

    public static String removeStopWords(Sentence sentence) {
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(sentence.toString()));
        for (List<HasWord> tokenList : tokenizer) {
            Tree tree = LexicalizedParserManager.parse(tokenList);
            List<String> POSTagList = Arrays.asList("CC", "IN", "DT");
            LexicalizedParserManager.removePOSTagList(tree, POSTagList);
            List<String> wordList = LexicalizedParserManager.getWordList(tree);
            StringBuilder text = new StringBuilder();
            for (String word : wordList) {
                text.append(word).append(" ");
            }
            return text.toString();
        }
        return sentence.text();
    }

}
