package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.coreference;

import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.TaggerManager;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.coreference.term.RelatedTerms;
import br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.coreference.term.Term;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Coreference {

    private static StanfordCoreNLP pipeline;
    private static List<String> posTags = Arrays.asList("PRP", "DT");

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref");
        pipeline = new StanfordCoreNLP(props);
    }

    public static void makeCoreference(Document document) {
        List<RelatedTerms> relatedTermsList = generateRelatedTermList(document);
        updateDocument(document, relatedTermsList);
    }

    private static List<RelatedTerms> generateRelatedTermList(Document document) {
        String text = document.text();
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        List<RelatedTerms> relatedTermsList = new ArrayList<>();
        for (CorefChain cc : annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
            RelatedTerms relatedTerms = new RelatedTerms();
            for (CorefChain.CorefMention m : cc.getMentionsInTextualOrder()) {
                Term term = new Term(m.mentionSpan, m.sentNum - 1, m.startIndex - 1, m.endIndex - 1);
                relatedTerms.addTerm(term);
            }
            relatedTermsList.add(relatedTerms);
        }
        return relatedTermsList;
    }

    private static void updateDocument(Document document, List<RelatedTerms> relatedTermsList) {
        for (int sentenceId = 0; sentenceId < document.sentences().size(); sentenceId++) {
            Sentence sentence = document.sentences().get(sentenceId);
            for (RelatedTerms relatedTerms : relatedTermsList) {
                for (Term term : relatedTerms.getTermSet()) {
                    if (term.getSentenceId() == sentenceId) {
                        if (isPronounOrDeterminer(sentence, term)) {
                            Term newTerm = getNewTerm(document, sentenceId, relatedTerms);
                            if (newTerm != null) {
                                String newSentenceString = sentence.toString().replace(term.getLabel(), newTerm.getLabel());
                                document.sentences().set(sentenceId, new Sentence(newSentenceString));
                            }
                        }
                    }
                }
            }
        }
    }

    private static Term getNewTerm(Document document, int sentenceId, RelatedTerms relatedTerms) {
        Term newTerm = null;
        int currentTermDistance = Integer.MAX_VALUE;
        for (Term term : relatedTerms.getTermSet()) {
            if (!isPronounOrDeterminer(document.sentences().get(term.getSentenceId()), term)) {
                int termDistance = Math.abs(sentenceId - term.getSentenceId());
                boolean isPredecessor = sentenceId > term.getSentenceId();
                if (termDistance < currentTermDistance || (termDistance == currentTermDistance && isPredecessor)) {
                    newTerm = term;
                    currentTermDistance = termDistance;
                }
            }
        }
        return newTerm;
    }

    private static boolean isPronounOrDeterminer(Sentence sentence, Term term) {
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(sentence.toString()));
        for (List<HasWord> tokenList : tokenizer) {
            List<TaggedWord> taggedWordList = TaggerManager.tagSentence(tokenList);
            TaggedWord taggedWord = taggedWordList.get(term.getStartIndex());
            if (taggedWord.value().equalsIgnoreCase(term.getLabel()) && posTags.contains(taggedWord.tag())) {
                return true;
            }
        }
        return false;
    }

}

