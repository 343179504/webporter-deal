package com.suarez.webporter.client;


import com.suarez.webporter.util.SpringBeanUtil;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.springframework.util.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Vector;

public class ConfigPrint {

    //    JTextArea logTextArea;
    JTable table;
    public ConfigFrame cf = new ConfigFrame();
    DefaultTableCellRenderer tcr=null;
    int m;
    int n;

    public JPanel buildJpanel(JPanel panel) {

        int y0 = 0;
        //定义一维数据作为列标题
        Vector columnTitle = new Vector<>();
        columnTitle.add("队名(Bet)");
        columnTitle.add("盘口");
        columnTitle.add("大小球");
        columnTitle.add("赔率");
        columnTitle.add("金额");
        columnTitle.add("-");
        columnTitle.add("队名(万博)");
        columnTitle.add("盘口");
        columnTitle.add("大小球");
        columnTitle.add("赔率");
        columnTitle.add("金额");
        Vector dataVector = new Vector();
        panel.add(cf.buildJBorder("日志", 0, y0 + 10, 1152, 720));
        tcr = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                JTextField   text   =   new   JTextField(value.toString());
                if(column==2||column==8)  {

                    text.setBackground(Color.RED);
                    text.setForeground(Color.BLACK);
                }   else   {
//                    text.setBackground(Color.WHITE);
//                    text.setForeground(Color.CYAN);
                }
                return   text;

            }
        };

        table = new MyTable(dataVector, columnTitle);
        table.setRowHeight(40);
        table.setDefaultRenderer(Object.class, tcr);

        JScrollPane logTextArea = new JScrollPane(table);

        // 添加按钮，绑定事件监听
        JButton clearButton = cf.buildJButton("清除数据", 1305, y0 + 210, 100, 25);
        addActionListener(clearButton);

        panel.add(clearButton);

//        // 添加按钮，绑定事件监听
//        JButton clearButton = cf.buildJButton("", 1305, y0 + 210, 100, 25);
//        addActionListener(clearButton);
//        {
//            SpringBeanUtil.getBean("wb..");
//        }
//
//        panel.add(clearButton);
        panel.add(logTextArea, BorderLayout.CENTER);
        logTextArea.setBounds(20, y0 + 30, 1100, 680);
//        logTextArea.getViewport().add(logTextArea);
//        panel.add(JScrollPane);

        try {
            Thread t = new Appendered(table);
            t.start();

        } catch (Exception e) {
        }
        return panel;
    }

    // 为按钮绑定监听
    private void addActionListener(JButton saveButton) {
        saveButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        activeEvent();
                    }
                });

    }

    // save event
    private void activeEvent() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowCount = tableModel.getRowCount();
        if (rowCount > 0) {
            for (int i = rowCount-1; i >=0; i--) {
                tableModel.removeRow(i);
            }
        }
    }
}
