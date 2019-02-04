package br.edu.ufrgs.inf.bpm.validation.textmodelsimilarity.nlp.coreference.term;

import java.util.HashSet;
import java.util.Set;

public class RelatedTerms {

    private Set<Term> termSet;

    public RelatedTerms() {
        termSet = new HashSet<>();
    }

    public Set<Term> getTermSet() {
        return termSet;
    }

    public void addTerm(Term term) {
        termSet.add(term);
    }

}
