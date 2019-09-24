package utn.tdm.meegos.util;

import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.File;

public class FileUtil
{
    public static void insertStringAtEndInFile(final String path, final String lineToBeInserted) throws Exception {
        insertStringAtEndInFile(new File(path), lineToBeInserted);
    }
    
    public static ArrayList<String> getLines(final String path) throws Exception {
        return (ArrayList<String>)getLines(new File(path));
    }
    
    public static ArrayList<String> getLines(final File inFile) throws Exception {
        final ArrayList<String> lines = new ArrayList<String>();
        final FileInputStream fis = new FileInputStream(inFile);
        final BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String thisLine = "";
        while ((thisLine = in.readLine()) != null) {
            thisLine = thisLine.trim();
            if (thisLine.length() != 0) {
                lines.add(thisLine);
            }
        }
        return lines;
    }
    
    public static void insertStringAtEndInFile(final File inFile, String lineToBeInserted) throws Exception {
        lineToBeInserted = String.valueOf(lineToBeInserted) + "\n";
        final RandomAccessFile file = new RandomAccessFile(inFile, "rw");
        file.seek(file.length());
        file.write(lineToBeInserted.getBytes());
        file.close();
    }
}
