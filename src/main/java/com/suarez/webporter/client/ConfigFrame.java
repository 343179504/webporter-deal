package com.suarez.webporter.client;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ConfigFrame {
    private final static Logger logger = Logger.getLogger(ConfigFrame.class);

    public static void show() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel = new ConfigDB().buildJpanel(panel);
        panel = new ConfigSocket().buildJpanel(panel);
        panel = new ConfigEvent().buildJpanel(panel);
        panel = new ConfigTask().buildJpanel(panel);
        panel = new ConfigPrint().buildJpanel(panel);
        buildFrame(panel);
    }

    protected static JButton buildJButton(String name, int x, int y, int width, int height) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, height);
        return button;
    }

    // 文本框
    protected JTextField buildJTextField(JTextField jtf, String value, String name, int columns, int x, int y, int width, int height) {
        jtf = new JTextField(columns);
        jtf.setText(value);
        jtf.setName(name);
        jtf.setBounds(x, y, width, height);
        logger.info(name + "======" + value);
        return jtf;
    }

    protected static JLabel buildJLabel(String name, int x, int y, int width, int height) {
        JLabel label = new JLabel(name);
        label.setBounds(x, y, width, height);

        return label;
    }

    protected JLabel buildJBorder(String name, int x, int y, int width, int height) {
        JLabel label = new JLabel();
        label.setBounds(x, y, width, height);
        label.setBorder(BorderFactory.createTitledBorder(name));
        return label;
    }

    protected static void saveResult(String msg) {
        if ("true".equals(msg)) {
            JOptionPane.showMessageDialog(null, "保存成功！", "提示", JOptionPane.PLAIN_MESSAGE);
        } else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "保存异常！", "提示", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void buildFrame(JComponent component) {
        JFrame frame = new JFrame("Simulater Terminal");
        component.setBounds(0, 0, 1152, 864);
        frame.add(component);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(BorderLayout.CENTER, component);
        // 设置窗口最小尺寸
        frame.setMinimumSize(new Dimension(1152, 864));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
    }


}