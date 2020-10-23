package com.suarez.webporter.client;


import com.suarez.webporter.driver.BetDriver;
import com.suarez.webporter.driver.NwbDriver;
import com.suarez.webporter.driver.WbDriver;
import com.suarez.webporter.util.SpringBeanUtil;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.springframework.util.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        columnTitle.add("状态");
        columnTitle.add("队名(万博)");
        columnTitle.add("盘口");
        columnTitle.add("大小球");
        columnTitle.add("赔率");
        columnTitle.add("金额");
        Vector dataVector = new Vector();
        panel.add(cf.buildJBorder("日志", 0, y0 + 10, 1000, 360));
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
           public Component getTableCellRendererComponent(JTable table, Object value,
                 boolean isSelected, boolean hasFocus, int row, int column) {
                 Component cell = super.getTableCellRendererComponent
                  (table, value, isSelected, hasFocus, row, column);
                  if(column==5 && cell.isBackgroundSet()||column==0 && cell.isBackgroundSet())//设置变色的单元格
                     cell.setForeground(Color.red);
                  else
                    cell.setForeground(Color.BLACK);
                return cell;
            }
        };
        table = new MyTable(dataVector, columnTitle);
        table.setRowHeight(40);
        table.setDefaultRenderer(Object.class, tcr);
        table.setSelectionBackground(new Color(154, 154, 154));
//        table.getColumnModel().getColumn(11).setCellRenderer(new MyButtonRender());//设置列按钮
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int rowI = table.rowAtPoint(e.getPoint());// 得到table的行号
                int columnI = table.columnAtPoint(e.getPoint());// 得到table的列号
                System.out.println("rowI"+rowI+" "+"columnI"+columnI);

                if (rowI > -1&&columnI>-1){
                    int selectedRow = table.getSelectedRow();//获得选中行的索引
                    if(0==columnI){//点击bet队名
                        //获取选中球队万博数据：
                        String name = (String) table.getModel().getValueAt(selectedRow,0);
                        String pankou = (String) table.getModel().getValueAt(selectedRow,1);
                        String daxiaoqiu = (String) table.getModel().getValueAt(selectedRow,2);
                        BetDriver betDriver = (BetDriver) SpringBeanUtil.getBean("betDriver");
                        String[] nameArray = name.split("_");
                        String name_z = nameArray[0];
                        betDriver.focusOn(name_z,pankou,daxiaoqiu);
                    }
                    if(5==columnI){//点击wb队名
                        //获取选中球队万博数据：
                        String name = (String) table.getModel().getValueAt(selectedRow,5);
                        String pankou = (String) table.getModel().getValueAt(selectedRow,6);
                        String daxiaoqiu = (String) table.getModel().getValueAt(selectedRow,7);
                        NwbDriver nwbDriver = (NwbDriver) SpringBeanUtil.getBean("nwbDriver");
                        String[] nameArray = name.split("_");
                        String name_z = nameArray[0];
                        nwbDriver.focusOn(name_z,pankou,daxiaoqiu);
                    }

                }
            }
        });

//        JButton wbButton = cf.buildJButton("万博", 1030, y0 + 210, 80, 25);
//        wbButton.addActionListener(new ActionListener(){//添加事件
//            public void actionPerformed(ActionEvent e){
//                int selectedRow = table.getSelectedRow();//获得选中行的索引
//                if(selectedRow!= -1)   //是否存在选中行
//                {
//                    //获取选中球队万博数据：
//                    String name = (String) table.getModel().getValueAt(selectedRow,5);
//                    String pankou = (String) table.getModel().getValueAt(selectedRow,6);
//                    String daxiaoqiu = (String) table.getModel().getValueAt(selectedRow,7);
//                    NwbDriver nwbDriver = (NwbDriver) SpringBeanUtil.getBean("nwbDriver");
//                    String[] nameArray = name.split("_");
//                    String name_z = nameArray[0];
//                    nwbDriver.focusOn(name_z,pankou,daxiaoqiu);
//
//                }
//            }
//        });
//        panel.add(wbButton);
//        JButton betButton = cf.buildJButton("Bet", 1115, y0 + 210, 80, 25);
//        betButton.addActionListener(new ActionListener(){//添加事件
//            public void actionPerformed(ActionEvent e){
//                int selectedRow = table.getSelectedRow();//获得选中行的索引
//                if(selectedRow!= -1)   //是否存在选中行
//                {
//                    //获取选中球队万博数据：
//                    String name = (String) table.getModel().getValueAt(selectedRow,0);
//                    String pankou = (String) table.getModel().getValueAt(selectedRow,1);
//                    String daxiaoqiu = (String) table.getModel().getValueAt(selectedRow,2);
//                    BetDriver betDriver = (BetDriver) SpringBeanUtil.getBean("betDriver");
//                    String[] nameArray = name.split("_");
//                    String name_z = nameArray[0];
//                    betDriver.focusOn(name_z,pankou,daxiaoqiu);
//
//                }
//            }
//        });
//        panel.add(betButton);
        JScrollPane logTextArea = new JScrollPane(table);

        // 添加按钮，绑定事件监听
        JButton clearButton = cf.buildJButton("清除", 1030, y0 + 210, 80, 25);
        addActionListener(clearButton);

        panel.add(clearButton);

        panel.add(logTextArea, BorderLayout.CENTER);
        logTextArea.setBounds(20, y0 + 30, 960, 320);

        try {
            Thread t = new Appendered(table);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
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
