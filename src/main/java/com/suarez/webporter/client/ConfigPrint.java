package com.suarez.webporter.client;


import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;

import javax.swing.*;
import java.awt.*;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;

public class ConfigPrint {

//    JTextArea logTextArea;
    JTable table;
    public ConfigFrame cf = new ConfigFrame();

    public JPanel buildJpanel(JPanel panel) {

        int y0 = 0;
        Object[][] tableData =
                {
                        new Object[]{"队名(万博)" , 29 , "女","队名(万博)" , 29 , "女","队名(万博)" , 29 , "女", 29 , "女"},
                        new Object[]{"队名(万博)" , 29 , "女","队名(万博)" , 29 , "女","队名(万博)" , 29 , "女", 29 , "女"},
                        new Object[]{"队名(万博)" , 29 , "女","队名(万博)" , 29 , "女","队名(万博)" , 29 , "女", 29 , "女"},
                        new Object[]{"队名(万博)" , 29 , "女","队名(万博)" , 29 , "女","队名(万博)" , 29 , "女", 29 , "女"},
                        new Object[]{"队名(万博)" , 29 , "女","队名(万博)" , 29 , "女","队名(万博)" , 29 , "女", 29 , "女"}
                };
        //定义一维数据作为列标题
        Object[] columnTitle = {"队名(万博)" , "盘口" , "大小球", "赔率", "金额", "-","队名(Bet)" , "盘口" , "大小球", "赔率", "金额"};

        panel.add(cf.buildJBorder("日志", 0, y0 + 10, 1152, 720));
        table = new MyTable(tableData , columnTitle);
        table.setRowHeight(40);

//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //将JTable对象放在JScrollPane中，并将该JScrollPane放在窗口中显示出来
//        panel.add(new JScrollPane(table));
//        panel.pack();
//        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jf.setVisible(true);

        JScrollPane logTextArea =  new JScrollPane(table);
        panel.add(logTextArea, BorderLayout.CENTER);
        logTextArea.setBounds(0, y0 + 40, 1152, 680);
//        logTextArea.getViewport().add(logTextArea);
//        panel.add(JScrollPane);

//        Logger root = Logger.getRootLogger();
//        try {
//            Appender appender = root.getAppender("WriterAppender");
//            PipedReader reader = new PipedReader();
//            Writer writer = new PipedWriter(reader);
//
//            ((WriterAppender) appender).setWriter(writer);
//
//            Thread t = new Appendered(reader, logTextArea, logScrollPane);
//            t.start();
//
//        } catch (Exception e) {
//        }
        return panel;
    }

}
