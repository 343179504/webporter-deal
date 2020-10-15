package com.suarez.webporter.client;


import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;

import javax.swing.*;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;

public class ConfigPrint {

    JTextArea logTextArea;

    public ConfigFrame cf = new ConfigFrame();

    public JPanel buildJpanel(JPanel panel) {

        int y0 = 0;

        panel.add(cf.buildJBorder("日志", 500, y0 + 10, 630, 720));

        logTextArea = new JTextArea();
        panel.add(logTextArea);

        JScrollPane logScrollPane = new JScrollPane();
        logScrollPane.setBounds(510, y0 + 40, 610, 680);
        logScrollPane.getViewport().add(logTextArea);
        panel.add(logScrollPane);

        Logger root = Logger.getRootLogger();
        try {
            Appender appender = root.getAppender("WriterAppender");
            PipedReader reader = new PipedReader();
            Writer writer = new PipedWriter(reader);

            ((WriterAppender) appender).setWriter(writer);

            Thread t = new Appendered(reader, logTextArea, logScrollPane);
            t.start();

        } catch (Exception e) {
        }
        return panel;
    }

}
