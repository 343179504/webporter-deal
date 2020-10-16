package com.suarez.webporter.client;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel sy;

    public ConfigFrame cf = new ConfigFrame();
    public JPanel buildJpanel(JPanel panel) {
        String xqje = propertiesUtil.getConUrl();
        String dqpl = "";
        String xqpl = "";


        int y0 = 0;

        panel.add(cf.buildJBorder("计算器", 1170, y0 + 10, 400, 250));

        // URL
        panel.add(cf.buildJLabel("小球金额：", 1185, y0 + 40, 80, 25));
        xqjeTextField = cf.buildJTextField(xqjeTextField, xqje, "xqje", 20, 1270, y0 + 40, 165, 25);
        panel.add(xqjeTextField);

        panel.add(cf.buildJLabel("小球赔率：", 1185, y0 + 70, 80, 25));
        xqplTextField = cf.buildJTextField(xqplTextField, xqpl, "xqpl", 20, 1270, y0 + 70, 165, 25);
        panel.add(xqplTextField);
        // 用户名
        panel.add(cf.buildJLabel("大球赔率：", 1185, y0 + 100, 80, 25));
        dqplTextField = cf.buildJTextField(dqplTextField, dqpl, "dqpl", 20, 1270, y0 + 100, 165, 25);
        panel.add(dqplTextField);

        // 密码
        panel.add(cf.buildJLabel("大球金额：", 1185, y0 + 130, 180, 25));
        dqje = cf.buildJLabel("暂无", 1270, y0 + 130, 80, 25);
        panel.add(dqje);

        panel.add(cf.buildJLabel("最大收益：", 1185, y0 + 160, 180, 25));
        sy = cf.buildJLabel("暂无", 1270, y0 + 160, 80, 25);
        panel.add(sy);

        // 添加按钮，绑定事件监听
        JButton saveButton = cf.buildJButton("开始计算", 1185, y0 + 210, 100, 25);
        addActionListener(saveButton);
        panel.add(saveButton);

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
        //小球金额
        String xqje = xqjeTextField.getText().toString();
        //大球赔率
        String dqpl = dqplTextField.getText().toString();
        //小球赔率
        String xqpl = xqplTextField.getText().toString();

        DecimalFormat df = new DecimalFormat("0.0");//格式化小数

        float sy_tmp = 0;
        if (!StringUtils.isEmpty(dqpl)) {
            float dqje_tmp = Float.parseFloat(xqje)/Float.parseFloat(dqpl);
            if(!StringUtils.isEmpty(xqpl)){
                sy_tmp = (Float.parseFloat(xqje)*Float.parseFloat(xqpl))-dqje_tmp;
//                sy_tmp = Float.parseFloat(xqje)/Float.parseFloat(dqpl);
                sy.setText(df.format(sy_tmp));
            }
            String dqjefw="";
            if(sy_tmp<0){
                 dqjefw=" - ";
            }else{
                 dqjefw = df.format(dqje_tmp)+" 至 "+df.format(Float.parseFloat(xqje)*Float.parseFloat(xqpl));

            }
            dqje.setText(dqjefw);//返回的是String类型
        }else{
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入大球赔率！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}