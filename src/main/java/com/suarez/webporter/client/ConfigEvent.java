package com.suarez.webporter.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ConfigTask
 * @Date 2020/7/20
 * @Created adao
 */
public class ConfigEvent {
    private final static Logger logger = Logger.getLogger(ConfigEvent.class);

    @Autowired
    PropertiesUtil propertiesUtil;

    private JTextField eventPeriodUnitField;
    private JTextField eventPeriodMinField;
    private JTextField eventPeriodMaxField;

    private ConfigFrame cf = new ConfigFrame();

    public JPanel buildJpanel(JPanel panel) {
        ConfigFrame cf = new ConfigFrame();
        String eventPeriodUnit = String.valueOf(propertiesUtil.getConEventPeriodUnit());
        String eventPeriodMin = String.valueOf(propertiesUtil.getConEventPeriodMin());
        String eventPeriodMax = String.valueOf(propertiesUtil.getConEventPeriodMax());

        int y0 = 360;

        //添加服务器配置区
        panel.add(cf.buildJBorder("事件配置", 5, y0, 490, 180));
        //事件周期基数unit
        panel.add(cf.buildJLabel("周期基数：", 15, y0 + 30, 80, 25));
        eventPeriodUnitField = cf.buildJTextField(eventPeriodUnitField, eventPeriodUnit, "serverAddr", 20, 100, y0 + 30, 165, 25);
        panel.add(eventPeriodUnitField);
        //事件上报周期范围最小数
        panel.add(cf.buildJLabel("周期最小数：", 15, y0 + 60, 80, 25));
        eventPeriodMinField = cf.buildJTextField(eventPeriodMinField, eventPeriodMin, "serverPort", 20, 100, y0 + 60, 165, 25);
        panel.add(eventPeriodMinField);

        //事件上报周期范围最大数
        panel.add(cf.buildJLabel("周期最大数：", 15, y0 + 90, 80, 25));
        eventPeriodMaxField = cf.buildJTextField(eventPeriodMaxField, eventPeriodMax, "serverPort", 20, 100, y0 + 90, 165, 25);
        panel.add(eventPeriodMaxField);

        // 添加按钮，绑定事件监听
        JButton saveButton = cf.buildJButton("保存", 100, y0 + 130, 80, 25);
        addActionListener(saveButton);
        panel.add(saveButton);

        return panel;
    }

    // 为按钮绑定监听
    private void addActionListener(JButton button) {
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        save();
                    }
                });

    }

    // save event
    private void save() {
        String eventPeriodUnit = String.valueOf(propertiesUtil.getConEventPeriodUnit());
        String eventPeriodMin = String.valueOf(propertiesUtil.getConEventPeriodMin());
        String eventPeriodMax = String.valueOf(propertiesUtil.getConEventPeriodMax());
        String eventPeriodUnitText = eventPeriodUnitField.getText().toString();
        String eventPeriodMinText = eventPeriodMinField.getText().toString();
        String eventPeriodMaxText = eventPeriodMaxField.getText().toString();
        if (StringUtils.isEmpty(eventPeriodUnitText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入事件上报周期基数（毫秒）！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (StringUtils.isEmpty(eventPeriodMinText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入事件上报周期随机最小数！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (StringUtils.isEmpty(eventPeriodMaxText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入事件上报周期随机最大数！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (Integer.valueOf(eventPeriodMinText) >= Integer.valueOf(eventPeriodMaxText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "最小数要小于最大数，请修改！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (eventPeriodUnit.equals(eventPeriodUnitText) && eventPeriodMin.equals(eventPeriodMinText) && eventPeriodMax.equals(eventPeriodMaxText) ) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "配置参数未更改！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {
            propertiesUtil.setConEventPeriodUnit(Long.valueOf(eventPeriodUnitText));
            propertiesUtil.setConEventPeriodMin(Integer.valueOf(eventPeriodMinText));
            propertiesUtil.setConEventPeriodMax(Integer.valueOf(eventPeriodMaxText));

        }
    }

}