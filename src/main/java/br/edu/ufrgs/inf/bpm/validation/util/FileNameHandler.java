package br.edu.ufrgs.inf.bpm.validation.util;

import org.apache.commons.io.FilenameUtils;

public class FileNameHandler {

    private String index;
    private String name;
    private String type;
    private String fileName;

    public FileNameHandler(String fileName) {
        this.fileName = fileName;

        String[] s = fileName.split(" - ");
        if (s.length == 2) {
            this.index = "";
            this.name = s[0];
            this.type = FilenameUtils.removeExtension(s[1]);
        } else if (s.length == 3) {
            this.index = s[0];
            this.name = s[1];
            this.type = FilenameUtils.removeExtension(s[2]);
        }
    }

    public String getIndex() {
        if (index == null) {
            index = "0";
        }
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
