package br.edu.ufrgs.inf.bpm.validation.util;

import br.edu.ufrgs.inf.bpm.textmetadata.TSentence;
import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;

public class MetaTextWrapper {

    private final TTextMetadata metaText;

    public MetaTextWrapper(TTextMetadata metaText) {
        this.metaText = metaText;
    }

    public String getPureText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TSentence tSentence : metaText.getText().getSentenceList()) {
            stringBuilder.append(tSentence.getValue()).append(" ");
        }
        return stringBuilder.toString();
    }

}
