package com.suarez.webporter.client;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

public class MyTable extends JTable {                       // 实现自己的表格类
    // 重写JTable类的构造方法
    public MyTable(Object[][] rowData, Object[] columnNames) {//Vector rowData, Vector columnNames
        super(rowData,columnNames);                      // 调用父类的构造方法
    }
    // 重写JTable类的构造方法
    public MyTable(Vector rowData, Vector columnNames) {//Vector rowData, Vector columnNames
        super(rowData,columnNames);                      // 调用父类的构造方法
    }
    // 重写JTable类的getTableHeader()方法
    public JTableHeader getTableHeader() {                  // 定义表格头
        JTableHeader tableHeader = super.getTableHeader();  // 获得表格头对象
        tableHeader.setReorderingAllowed(false);            // 设置表格列不可重排
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader
                .getDefaultRenderer();                      // 获得表格头的单元格对象              hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);     // 设置列名居中显示
        return tableHeader;
    }
    // 重写JTable类的getDefaultRenderer(Class<?> columnClass)方法
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) { // 定义单元格
        DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super
                .getDefaultRenderer(columnClass);                       // 获得表格的单元格对象
        cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);     // 设置单元格内容居中显示
        return cr;
    }
    // 重写JTable类的isCellEditable(int row, int column)方法
    // 表格不可编辑
    public boolean isCellEditable(int row, int column) {
       if(1==column||6==column){
           return false;
       }else{
           return true;
       }
    }

    /**
     * 设置表格的某一行的背景色
     * @param table
     */
    public static void setOneRowBackgroundColor(JTable table, int rowIndex,
                                                Color color) {
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table,
                                                               Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    if (row == rowIndex) {
                        setBackground(color);
                        setForeground(Color.WHITE);
                    } else if (row > rowIndex) {
                        setBackground(Color.BLACK);
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(Color.BLACK);
                        setForeground(Color.WHITE);
                    }

                    return super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                }
            };
            int columnCount = table.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
