package com.suarez.webporter.client;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JTextField urlTextField;
    private JTextField usernameTextField;
    private JTextField passwordTextField;

    public ConfigFrame cf = new ConfigFrame();
    public JPanel buildJpanel(JPanel panel) {
        String url = propertiesUtil.getConUrl();
        String username = propertiesUtil.getConUserName();
        String password = propertiesUtil.getConPassWord();

        int y0 = 0;

        panel.add(cf.buildJBorder("数据库配置", 5, y0 + 10, 490, 180));

        // URL
        panel.add(cf.buildJLabel("URL：", 15, y0 + 40, 80, 25));
        urlTextField = cf.buildJTextField(urlTextField, url, "dbUrl", 20, 100, y0 + 40, 350, 25);
        panel.add(urlTextField);
        // 用户名
        panel.add(cf.buildJLabel("用户名：", 15, y0 + 70, 80, 25));
        usernameTextField = cf.buildJTextField(usernameTextField, username, "dbUserName", 20, 100, y0 + 70, 165, 25);
        panel.add(usernameTextField);

        // 密码
        panel.add(cf.buildJLabel("密码：", 15, y0 + 100, 80, 25));
        passwordTextField = cf.buildJTextField(passwordTextField, password, "dbPassWord", 20, 100, y0 + 100, 165, 25);
        panel.add(passwordTextField);

        // 添加按钮，绑定事件监听
        JButton saveButton = cf.buildJButton("保存", 100, y0 + 140, 80, 25);
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
        String url = propertiesUtil.getConUrl();
        String username = propertiesUtil.getConUserName();
        String password = propertiesUtil.getConPassWord();
        String textUrl = urlTextField.getText().toString();
        String textUsername = usernameTextField.getText().toString();
        String textPassword = passwordTextField.getText().toString();
        if (StringUtils.isEmpty(textUrl)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入数据库URL！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (StringUtils.isEmpty(textUsername)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入用户名！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (StringUtils.isEmpty(textPassword)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入密码！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (url.equals(textUrl) && textUsername.equals(username) && textPassword.equals(password) ) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "配置参数未更改！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {

            propertiesUtil.setConUrl(textUrl);
            propertiesUtil.setConUserName(textUsername);
            propertiesUtil.setConPassWord(textPassword);

        }
    }
}