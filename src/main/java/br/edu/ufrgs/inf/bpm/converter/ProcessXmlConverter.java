package br.edu.ufrgs.inf.bpm.converter;

public class ProcessXmlConverter {

    public static String convertToHtml(String text) {
        return text.replace("<text>", "")
                .replace("</text>", "")
                .replace("<algo>", "")
                .replace("</algo>", "")
                .replace("<bulletpoint/>", "- ")
                .replace("<space/>", " ")
                .replace("<tab/>", "\t")
                .replace("<newline/>", "\n");
    }

    public static String convertToColoredHtml(String text) {
        return text.replace("<text>", "")
                .replace("</text>", "")
                .replace("<algo>", "<span style=\"color:blue;font-weight:bold\">")
                .replace("</algo>", "</span>")
                .replace("<bulletpoint/>", "- ")
                .replace("<space/>", " ")
                .replace("<tab/>", "\t")
                .replace("<newline/>", "\n");
    }

}
