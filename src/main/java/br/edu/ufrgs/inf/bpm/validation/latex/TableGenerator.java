package br.edu.ufrgs.inf.bpm.validation.latex;

import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.List;

public class TableGenerator {

    private String caption;
    private String label;
    private List<String> headerList;
    private List<List<String>> body;
    private int maxElementsByTable = 30;
    private StringBuilder latexTable = new StringBuilder();

    public TableGenerator(String caption, String label, List<String> headerList, List<List<String>> body) {
        this.caption = caption;
        this.label = label;
        this.headerList = headerList;
        this.body = body;
    }

    public String generateTable() {
        if (isValidTable()) {
            for (int idTable = 0; idTable * maxElementsByTable < body.size(); idTable++) {
                generatePreTable(idTable + 1);
                generateHeader();
                generateBody(idTable * maxElementsByTable);
                generatePosTable();
            }
        }
        return latexTable.toString();
    }

    private boolean isValidTable() {
        int headerSize = headerList.size();
        for (List<String> line : body) {
            if (line.size() != headerSize) {
                return false;
            }
        }
        return true;
    }

    private void generatePreTable(int idTable) {
        latexTable.append("\\begin{table}[htb]");
        latexTable.append(getLineSeparator());
        latexTable.append("\\centering");
        latexTable.append(getLineSeparator());
        latexTable.append("\\caption{").append(caption).append(" ").append(idTable).append(".} ");
        latexTable.append("\\label{").append(label).append("-").append(idTable).append("}");
        latexTable.append(getLineSeparator());
        latexTable.append("\\begin{tabular}{|").append(StringUtils.repeat("p{\\dimexpr 0.2\\linewidth-2\\tabcolsep}|", headerList.size())).append("}");
        latexTable.append(getLineSeparator());
        latexTable.append("\\hline");
        latexTable.append(getLineSeparator());
    }

    private void generateHeader() {
        generateLine(headerList);
    }

    private void generateBody(int idElement) {
        for (int idCurrentLine = idElement; idCurrentLine < body.size() && idCurrentLine < idElement + maxElementsByTable; idCurrentLine++) {
            List<String> line = body.get(idCurrentLine);
            generateLine(line);
        }
    }

    private void generateLine(List lineList) {
        Iterator iterator = lineList.iterator();
        while (iterator.hasNext()) {
            String lineValue = (String) iterator.next();
            latexTable.append(lineValue);
            if (iterator.hasNext()) {
                latexTable.append(" & ");
            }
        }
        latexTable.append(" \\\\ \\hline");
        latexTable.append(getLineSeparator());
    }

    private void generatePosTable() {
        latexTable.append("\\end{tabular}");
        latexTable.append(getLineSeparator());
        latexTable.append("\\end{table}");
        latexTable.append(getLineSeparator());
        latexTable.append(getLineSeparator());
    }

    private String getLineSeparator() {
        return System.getProperty("line.separator");
    }

}
