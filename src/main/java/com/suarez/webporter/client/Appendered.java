package com.suarez.webporter.client;

import com.suarez.webporter.deal.*;
import com.suarez.webporter.deal.bet_wb.Bet_Wb_Info;
import com.suarez.webporter.util.SpringBeanUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.io.PipedReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Appendered extends Thread {
    PipedReader reader;
    JTextArea textArea;
    JTable table;
    JScrollPane scroll;
    Music music = new Music();

    public Appendered(JTable table) {
        this.table = table;
    }

    public Appendered(PipedReader reader, JTextArea textArea, JScrollPane scroll) {
        this.reader = reader;
        this.textArea = textArea;
        this.scroll = scroll;

    }

    public Map<String, Bet_Wb_Info> getTestData(){
        Map<String, Bet_Wb_Info> map = new ConcurrentHashMap<>();
        Bet_Wb_Info info = new Bet_Wb_Info();
        info.setIsTrue(true);
        //设置bet信息
        info.setTeam_bet("富川FC");
        info.setPk_bet("2.5");
        info.setPllx_bet("大");
        info.setPl_bet("1");
        info.setMoney_bet("100");
        //设置wb信息
        info.setTeam_wb("富川FC");
        info.setPk_wb("2.5");
        info.setPllx_wb("小");
        info.setPl_wb("2.1");
        info.setMoney_wb("5");
        //设置金额
        info.setEnrn_money("100");
        map.put("0", info);
        map.put("1", info);
        map.put("2", info);

        return map;
    }

    public String getRandomString(int length){
        String str="▶▶▶▶▶▶▶▶▶▶";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    public void run() {

//        Map<String, Bet_Wb_Info> rsMap = BetNwbDeal.map;
//        rsMap=this.getTestData();
        music = new Music();


        while (true) {
            long startTime = System.currentTimeMillis(); //获取结束时间

            try{

                Map<String, Integer> currentKeyList = new HashMap<>();
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                int rowCount = tableModel.getRowCount();
                if (rowCount > 0) {
                    for (int i = 0; i < rowCount; i++) {
                        String keyTeam = (String) tableModel.getValueAt(i, 0);
                        String keyPllx = (String) tableModel.getValueAt(i, 2);
                        currentKeyList.put(keyTeam + keyPllx, i);
                    }
                }
                Map<String, Bet_Wb_Info> rsMap = BasicDeal.map;
                Set<String> keySet = rsMap.keySet();
                for (String key : keySet) {
                    Bet_Wb_Info info = rsMap.get(key);
                    if (currentKeyList.get(key) != null) {
                        //Todo 更新
                        String betPllx = info.getPllx_bet();
                        int rowIndex = currentKeyList.get(key);
                        tableModel.setValueAt(info.getPk_bet(), rowIndex, 1);
                        tableModel.setValueAt(betPllx, rowIndex, 2);
                        tableModel.setValueAt(info.getPl_bet(), rowIndex, 3);

                        tableModel.setValueAt(this.getRandomString((int)(1+Math.random()*(7-1+1))), rowIndex, 4);
                        tableModel.setValueAt(info.getPk_wb(), rowIndex, 6);
                        tableModel.setValueAt(info.getPllx_wb(), rowIndex, 7);
                        tableModel.setValueAt(info.getPl_wb(), rowIndex, 8);
                        tableModel.setValueAt(info.getEnrn_money(), rowIndex, 9);
                        if("大".equals(betPllx)){
                            tableModel.setValueAt(info.getMoney_bet(), rowIndex, 10);
                        }else{
                            tableModel.setValueAt(info.getMoney_wb(), rowIndex, 10);

                        }
                        //table.setModel(tableModel);
                    } else {
                        try {
                            music.music();
                        } catch (IOException e) {

                        }
                        music.Start();
                        //TODO 新增
                        Object[] teamObj = new Object[11];
                        String betPllx = info.getPllx_bet();
                        String wbPllx = info.getPllx_wb();

                        teamObj[0] = info.getTeam_bet();
                        teamObj[1] = info.getPk_bet();
                        teamObj[2] = betPllx;
                        teamObj[3] = info.getPl_bet();
                        teamObj[4] = this.getRandomString((int)(1+Math.random()*(7-1+1)));//simpleDateFormat.format(new Date());
                        teamObj[5] = info.getTeam_wb();
                        teamObj[6] = info.getPk_wb();
                        teamObj[7] = wbPllx;
                        teamObj[8] = info.getPl_wb();
                        teamObj[9] = info.getEnrn_money();
                        if("大".equals(betPllx)){
                            teamObj[10] = info.getMoney_bet();
                        }else{
                            teamObj[10] = info.getMoney_wb();
                        }

                        tableModel.addRow(teamObj);
                        //table.setModel(tableModel);
                    }
                    String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPl_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
                            "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
                            " 盈利金额:【" + info.getEnrn_money() + "】";
//                    System.out.println(infoStr);
                    rsMap.remove(key);
                }

            }catch(Exception e){
//                rsMap=this.getTestData();

                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis(); //获取结束时间

//            System.out.println("解析全部花费时长：" + (endTime - startTime) + "ms"); //输出程序运行时间

        }
    }
}