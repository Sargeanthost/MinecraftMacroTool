package ext;

import java.awt.EventQueue;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;

public class TextAreaOutputStream
        extends OutputStream {

    private byte[] oneByte;
    private Appender appender;                                                   // most recent action

    public TextAreaOutputStream(JTextArea txtArea) {

        this(txtArea, 1000);
    }

    public TextAreaOutputStream(JTextArea txtArea, int maxLine) {
        if (maxLine < 1) {
            throw new IllegalArgumentException(
                    "TextAreaOutputStream maximum lines must be positive (value=" + maxLine + ")");
        }
        oneByte = new byte[1];
        appender = new Appender(txtArea, maxLine);
    }

    /**
     * Clear the current console text area.
     */
    public synchronized void clear() {
        if (appender != null) {
            appender.clear();
        }
    }
    public synchronized void close() {
        appender = null;
    }

    public synchronized void flush() {
    }

    public synchronized void write(int val) {
        oneByte[0] = (byte) val;
        write(oneByte, 0, 1);
    }

    public synchronized void write(byte[] ba) {
        write(ba, 0, ba.length);
    }

    public synchronized void write(byte[] ba, int str, int len) {
        if (appender != null) {
            appender.append(bytesToString(ba, str, len));
        }
    }


    static private String bytesToString(byte[] ba, int str, int len) {
        return new String(ba, str, len, StandardCharsets.UTF_8);
    }

    static class Appender
            implements Runnable {
        private final JTextArea textArea;
        private final int maxLines;
        private final LinkedList<Integer> lengthOfLines;
        private final List<String> valuesToAppend;

        private int currentLineLength;
        private boolean clear;
        private boolean queue;

        Appender(JTextArea textArea, int maxLine) {
            this.textArea = textArea;
            maxLines = maxLine;
            lengthOfLines = new LinkedList<>();
            valuesToAppend = new ArrayList<>();

            currentLineLength = 0;
            clear = false;
            queue = true;
        }

        synchronized void append(String val) {
            valuesToAppend.add(val);
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        synchronized void clear() {
            clear = true;
            currentLineLength = 0;
            lengthOfLines.clear();
            valuesToAppend.clear();
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        //! MUST BE THE ONLY METHOD THAT TOUCHES textArea!
        public synchronized void run() {
            if (clear) {
                textArea.setText("");
            }
            for (String val : valuesToAppend) {
                currentLineLength += val.length();
                if (val.endsWith(EOL1) || val.endsWith(EOL2)) {
                    if (lengthOfLines.size() >= maxLines) {
                        textArea.replaceRange("", 0, lengthOfLines.removeFirst());
                    }
                    lengthOfLines.addLast(currentLineLength);
                    currentLineLength = 0;
                }
                textArea.append(val);
            }
            valuesToAppend.clear();
            clear = false;
            queue = true;
        }

        static private final String EOL1 = "\n";
        static private final String EOL2 = System.getProperty("line.separator", EOL1);
    }

}