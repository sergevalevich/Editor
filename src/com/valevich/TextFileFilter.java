package com.valevich;

import javax.swing.filechooser.FileFilter;

class TextFileFilter extends FileFilter {
    private String ext;

    TextFileFilter(String ext) {
        this.ext = ext;
    }

    public boolean accept(java.io.File file) {
        return file.isDirectory() || (file.getName().endsWith(ext));
    }

    public String getDescription() {
        return "*" + ext;
    }
}
