package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LexicalizedParserManager {
    private static LexicalizedParser lexicalizedParser;

    static {
        lexicalizedParser = LexicalizedParser.loadModel();
    }

    public static Tree parse(List<HasWord> tokenList) {
        return lexicalizedParser.parse(tokenList);
    }

    public static void removePOSTagList(Tree tree, List<String> POSTagList) {
        Iterator iterator = tree.getChildrenAsList().iterator();

        int childId = 0;
        while (iterator.hasNext()) {
            Tree child = (Tree) iterator.next();
            if (POSTagList.contains(child.label().value())) {
                child.parent(tree).removeChild(childId);
            } else {
                childId++;
                removePOSTagList(child, POSTagList);
            }
        }
    }

    public static List<String> getWordList(Tree tree) {
        List<String> wordList = new ArrayList<>();
        for (Tree leaf : tree.getLeaves()) {
            wordList.add(leaf.value());
        }
        return wordList;
    }

    public static boolean isTermOnPOSTag(Tree tree, List<String> POSTagList, String term) {
        boolean isTermOnPOSTag = false;

        Iterator iterator = tree.getChildrenAsList().iterator();

        while (iterator.hasNext() && !isTermOnPOSTag) {
            Tree child = (Tree) iterator.next();
            String termChild = child.label().value();
            if (term.equalsIgnoreCase(term) && POSTagList.contains(termChild)) {
                return true;
            }
            isTermOnPOSTag = isTermOnPOSTag(child, POSTagList, term);
        }
        return isTermOnPOSTag;
    }

}
