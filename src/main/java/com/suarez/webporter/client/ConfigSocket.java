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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Classname ConfigSocket
 * @Date 2020/7/20
 * @Created YinTao
 */
public class ConfigSocket {
    private final static Logger logger = Logger.getLogger(ConfigDB.class);
    private JTextField serverAddrField;
    private JTextField serverPortField;

    @Autowired
    PropertiesUtil propertiesUtil;

    private ConfigFrame cf = new ConfigFrame();
    public JPanel buildJpanel(JPanel panel) {
        ConfigFrame cf = new ConfigFrame();
        String serverAddr =  propertiesUtil.getConServerAddress();
        String serverPort = String.valueOf(propertiesUtil.getConServerPort());

        int y0 = 200;

        /**
         * 服务器配置
         */
        //添加服务器配置区
        panel.add(cf.buildJBorder("Socket服务配置", 5, y0, 490,150));
        //添加服务器地址标签
        panel.add(cf.buildJLabel("服务器地址：", 15, y0 + 30, 80, 25));
        serverAddrField = cf.buildJTextField(serverAddrField, serverAddr, "serverAddr", 20, 100, y0 + 30, 165, 25);
        panel.add(serverAddrField);
        //添加服务器端口
        panel.add(cf.buildJLabel("服务器端口：", 15, y0 + 60, 80, 25));
        serverPortField = cf.buildJTextField(serverPortField, serverPort, "serverPort", 20, 100, y0 + 60, 165, 25);
        panel.add(serverPortField);

        //添加保存按钮
        JButton linkOrBreak = cf.buildJButton("保存", 100, y0 + 100, 80, 25);
        addActionListener(linkOrBreak);
        panel.add(linkOrBreak);
        // 添加测试按钮
        JButton saveButton = cf.buildJButton("测试", 200, y0 + 100, 80, 25);
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

                        if(saveButton.getText()=="保存")
                        {
                            activeEvent();
                        }
                        else if(saveButton.getText()=="测试")
                        {
                            testLink();
                        }

                    }
                });

    }

    // save event
    private void activeEvent() {
        String serverAddr =  propertiesUtil.getConServerAddress();
        String serverPort = String.valueOf(propertiesUtil.getConServerPort());
        String serverAddrText = serverAddrField.getText().toString();
        String serverPortText = serverPortField.getText().toString();
        if (StringUtils.isEmpty(serverAddrText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入Socker服务地址！", "提示", JOptionPane.INFORMATION_MESSAGE);
//        } else if (!ComMethod.isIp(serverAddrText)) {
//            Toolkit.getDefaultToolkit().beep();
//            JOptionPane.showMessageDialog(null, "Socker 服务地址格式错误！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
          else if (StringUtils.isEmpty(serverPortText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入Socker服务端口！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (Integer.valueOf(serverPortText) < 1 || Integer.valueOf(serverPortText) > 65535) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "端口配置错误，或超出范围[1,65535]", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (!Pattern.matches("^\\d{1,5}$", serverPortText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Socker端口配置错误,请输入数字5位数字", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (serverAddr.equals(serverAddrText) && serverPort.equals(serverPortText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "配置参数未更改！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {

            propertiesUtil.setConServerAddress(serverAddrText);
            propertiesUtil.setConServerPort(Integer.valueOf(serverPortText));

        }
    }
    // 测试连接
    private void testLink() {
        String serverAddrText = serverAddrField.getText().toString();
        String serverPortText = serverPortField.getText().toString();
        if (StringUtils.isEmpty(serverAddrText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入Socker服务地址！", "提示", JOptionPane.INFORMATION_MESSAGE);
//        } else if (!ComMethod.isIp(serverAddrText)) {
//            Toolkit.getDefaultToolkit().beep();
//            JOptionPane.showMessageDialog(null, "Socker服务地址格式错误！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (StringUtils.isEmpty(serverPortText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "请输入Socker服务端口！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (Integer.valueOf(serverPortText) < 1 || Integer.valueOf(serverPortText) > 65535) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "端口配置超出范围[1,65535]", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (!Pattern.matches("^\\d{1,5}$", serverPortText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Socker端口配置错误,请输入数字5位数字", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {

            try {
                SocketAddress address = new InetSocketAddress(serverAddrText, Integer.parseInt(serverPortText));
                Socket socket = new Socket();
                socket.connect(address, 2 * 1000);
                socket.close();
                JOptionPane.showMessageDialog(null, "测试连接正常", "提示", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "测试连接异常", "提示", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }
}
