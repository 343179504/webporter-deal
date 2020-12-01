package com.suarez.webporter.client;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ConfigDB
 * @Date 2020/7/17
 * @Created adao
 */
public class ConfigDB {
    private final static Logger logger = Logger.getLogger(ConfigDB.class);

    @Autowired
    PropertiesUtil propertiesUtil;

    private JTextField xqjeTextField;
    private JTextField xqplTextField;
    private JTextField dqplTextField;
    private JLabel dqje;
    private JLabel xqjeJLabel;
    private JLabel sy;

    public ConfigFrame cf = new ConfigFrame();

    public JPanel buildJpanel(JPanel panel) {
        String xqje = propertiesUtil.getConUrl();
        String dqpl = "";
        String xqpl = "";


        int y0 = 0;

        panel.add(cf.buildJBorder("计算器", 1015, y0 + 10, 300, 280));

        // URL
        panel.add(cf.buildJLabel("金额：", 1030, y0 + 40, 80, 25));
        xqjeTextField = cf.buildJTextField(xqjeTextField, xqje, "xqje", 20, 1100, y0 + 40, 80, 25);
        xqjeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = null;
                try {
                    text = e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                activeEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        panel.add(xqjeTextField);

        panel.add(cf.buildJLabel("小球赔率：", 1030, y0 + 70, 80, 25));
        xqplTextField = cf.buildJTextField(xqplTextField, xqpl, "xqpl", 20, 1100, y0 + 70, 80, 25);
        xqplTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = null;
                try {
                    text = e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                activeEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        panel.add(xqplTextField);
        // 用户名
        panel.add(cf.buildJLabel("大球赔率：", 1030, y0 + 100, 80, 25));
        dqplTextField = cf.buildJTextField(dqplTextField, dqpl, "dqpl", 20, 1100, y0 + 100, 80, 25);
        panel.add(dqplTextField);
        dqplTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = null;
                try {
                    text = e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                activeEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        // 密码
        panel.add(cf.buildJLabel("大球金额：", 1030, y0 + 130, 180, 25));
        dqje = cf.buildJLabel("暂无", 1100, y0 + 130, 200, 25);
        panel.add(cf.buildJLabel("小球金额：", 1030, y0 + 160, 180, 25));
        xqjeJLabel = cf.buildJLabel("暂无", 1100, y0 + 160, 200, 25);
        panel.add(dqje);
        panel.add(xqjeJLabel);

        panel.add(cf.buildJLabel("最大收益：", 1030, y0 + 190, 180, 25));
        sy = cf.buildJLabel("暂无", 1100, y0 + 190, 80, 25);
        panel.add(sy);

        return panel;
    }

    // save event
    private void activeEvent() {
        try {//小球金额
            String xqje = xqjeTextField.getText().toString();
            //大球赔率
            String dqpl = dqplTextField.getText().toString();
            //小球赔率
            String xqpl = xqplTextField.getText().toString();

            DecimalFormat df = new DecimalFormat("0.0");//格式化小数

            float sy_tmp = 0;
            if (!StringUtils.isEmpty(dqpl) && !StringUtils.isEmpty(xqpl)) {
                float dqje_tmp = Float.parseFloat(xqje) / Float.parseFloat(dqpl);
                if (!StringUtils.isEmpty(xqpl)) {
                    sy_tmp = (Float.parseFloat(xqje) * Float.parseFloat(xqpl)) - dqje_tmp;
//                sy_tmp = Float.parseFloat(xqje)/Float.parseFloat(dqpl);
                    sy.setText(df.format(sy_tmp));
                }
                String dqjefw = "";
                String xqjefw = "";
                if (sy_tmp < 0) {
                    dqjefw = " - ";
                } else {
                    dqjefw = "投小:" + df.format(dqje_tmp) + " < 投入 < " + df.format(Float.parseFloat(xqje) * Float.parseFloat(xqpl));
                    //以小球金额作为大球金额
                    String dqje = xqje;
                    float xqje_tmp = Float.parseFloat(dqje) / Float.parseFloat(xqpl);
                    xqjefw = "投大:" + df.format(xqje_tmp) + " < 投入 < " + df.format(Float.parseFloat(dqje) * Float.parseFloat(dqpl));
                }
                dqje.setText(dqjefw);//返回的是String类型
                xqjeJLabel.setText(xqjefw);//返回的是String类型
            } else {
                //Toolkit.getDefaultToolkit().beep();
                //JOptionPane.showMessageDialog(null, "请输入大球赔率！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {

        }
    }
}