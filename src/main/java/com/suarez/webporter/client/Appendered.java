package com.suarez.webporter.client;

import com.suarez.webporter.core.app;
import com.suarez.webporter.util.SpringBeanUtil;

import javax.swing.*;
import java.io.PipedReader;
import java.util.Scanner;

public class Appendered extends Thread {
    PipedReader reader;
    JTextArea textArea;
    JScrollPane scroll;

    public Appendered(PipedReader reader,JTextArea textArea, JScrollPane scroll) {
        this.reader = reader;
        this.textArea=textArea;
        this.scroll=scroll;

    }

    public void run() {
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNext()) {
            // System.out.println(scanner.nextLine());
            textArea.append(scanner.nextLine());
            textArea.append("\n");
            //使垂直滚动条自动向下滚动
            scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
        }

        app app = (com.suarez.webporter.core.app) SpringBeanUtil.getBean("app");
        app.begin();
    }
}