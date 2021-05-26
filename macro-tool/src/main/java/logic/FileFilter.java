package logic;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {

    private final String fileExtension;

    public FileFilter(String fileExtension){
        this.fileExtension = fileExtension;
    }

    @Override
    public boolean accept(File dir, String fileName) {
        return !fileName.startsWith(".") && fileName.endsWith(this.fileExtension);
    }
}
