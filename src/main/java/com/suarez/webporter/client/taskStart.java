package com.suarez.webporter.client;

//import com.adao.simulater.common.AppContextUtil;
//import com.adao.simulater.common.PropertiesUtil;
//import com.adao.simulater.pojo.TaskInfoBean;
//import com.adao.simulater.service.FreezeDayService;
//import com.adao.simulater.service.TaskService;
//import com.adao.simulater.service.impl.TaskServiceImpl;
//import com.adao.simulater.task.TaskManager;
//import com.adao.simulater.util.ComMethod;
import com.sun.javaws.security.AppContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Classname ConfigTask
 * @Description TODO
 * @Date 2020/7/21
 * @Created adao
 */
public class ConfigTask {
    private final static Logger logger = Logger.getLogger(ConfigEvent.class);

    @Autowired
    PropertiesUtil propertiesUtil;

    private JButton reportEnd;
    private JButton reportStart;
    private JComboBox codeTypeBox;

    private ConfigFrame cf = new ConfigFrame();
    private static String defaultTest = "-- 选择要执行的任务名称 --";
    private static String choseTask = null;

    private AppContextUtil appContextUtil = new AppContextUtil();
    private TaskService taskService = appContextUtil.getContext().getBean(TaskService.class);

    public JPanel buildJpanel(JPanel panel) {
        ConfigFrame cf = new ConfigFrame();

        int y0 = 550;
        List<TaskInfoBean> tasks = taskService.getAllTaskInfo();

        panel.add(cf.buildJBorder("任务配置", 5, y0, 490, 180));
        panel.add(cf.buildJLabel("选择任务：", 15, y0 + 30, 80, 25));
        codeTypeBox = buildJComboBox( tasks, 100, y0 + 30, 350, 25);
        addItemListener(codeTypeBox);
        panel.add(codeTypeBox);

        //开始上报按钮
        reportStart = cf.buildJButton("开始上报", 100, y0 + 120, 100, 25);
        addActionListener(reportStart);
        panel.add(reportStart);

        //添加停止上报按钮
        reportEnd = cf.buildJButton("停止上报", 300, y0 + 120, 100, 25);
        reportEnd.setEnabled(false);
        addActionListener(reportEnd);
        panel.add(reportEnd);
        return panel;
    }

    // 为按钮绑定监听
    private void addActionListener(JButton button) {
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (button.getText() == "开始上报") {
                            taskStart();
                        } else if (button.getText() == "停止上报") {
                            taskStop();
                        }
                    }
                });

    }

    // 为下拉事件监听器
    private void addItemListener(JComboBox comboBox) {
        comboBox.addItemListener(
                new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // 选择的下拉框选项
                            choseTask = e.getItem().toString();
                        }
                    }
                });

    }

    private static JComboBox buildJComboBox(List<TaskInfoBean> elements, int x, int y, int width, int height) {

        DefaultComboBoxModel codeTypeModel = new DefaultComboBoxModel();
        // elements 下拉框中的选项
        codeTypeModel.addElement(defaultTest);
        for (int i = 0; i < elements.size(); i++) {
            TaskInfoBean taskInfo = elements.get(i);
            String taskName = taskInfo.getTaskName();
            codeTypeModel.addElement(taskName);
        }

        JComboBox codeTypeBox = new JComboBox(codeTypeModel);
        codeTypeBox.setBounds(x, y, width, height);
        return codeTypeBox;
    }

    // 上报开始
    private void taskStart() {
        // 开始执行任务
        if (StringUtils.isEmpty(choseTask) ||  choseTask.equals(defaultTest)){
            JOptionPane.showMessageDialog(null, "选择要执行的任务名称！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {
            propertiesUtil.setConTaskEnable(true);

            TaskManager taskManager = new TaskManager();
            taskManager.init(choseTask);
            reportStart.setEnabled(false);
            reportEnd.setEnabled(true);
            logger.info("客户端连接成功，上报开始");
        }

    }

    // 上报停止
    private void taskStop() {
        propertiesUtil.setConTaskEnable(false);

        reportStart.setEnabled(true);
        reportEnd.setEnabled(false);
        logger.info("上报即将停止，需等待本次全部完成");
    }

}
