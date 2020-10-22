package com.suarez.webporter.client;

import com.suarez.webporter.deal.BetNwbDeal;
import com.suarez.webporter.deal.BetWbDeal;
import com.suarez.webporter.deal.ResultInfo;
import com.suarez.webporter.deal.bet_wb.Bet_Wb_Info;
import com.suarez.webporter.util.SpringBeanUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.PipedReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Appendered extends Thread {
    PipedReader reader;
    JTextArea textArea;
    JTable table;
    JScrollPane scroll;

    public Appendered(JTable table) {
        this.table = table;
    }

    public Appendered(PipedReader reader, JTextArea textArea, JScrollPane scroll) {
        this.reader = reader;
        this.textArea = textArea;
        this.scroll = scroll;

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
        while (true) {
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

                Map<String, Bet_Wb_Info> rsMap = BetNwbDeal.map;
                Set<String> keySet = rsMap.keySet();
                for (String key : keySet) {
                    Bet_Wb_Info info = rsMap.get(key);
                    if (currentKeyList.get(key) != null) {
                        //Todo 更新
                        int rowIndex = currentKeyList.get(key);
                        tableModel.setValueAt(info.getPk_bet(), rowIndex, 1);
                        tableModel.setValueAt(info.getPllx_bet(), rowIndex, 2);
                        tableModel.setValueAt(info.getPl_bet(), rowIndex, 3);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

                        tableModel.setValueAt(this.getRandomString((int)(1+Math.random()*(7-1+1))), rowIndex, 4);
                        tableModel.setValueAt(info.getPk_wb(), rowIndex, 6);
                        tableModel.setValueAt(info.getPllx_wb(), rowIndex, 7);
                        tableModel.setValueAt(info.getPl_wb(), rowIndex, 8);
                        tableModel.setValueAt(info.getEnrn_money(), rowIndex, 9);
                        //table.setModel(tableModel);
                    } else {
                        //TODO 新增
                        Object[] teamObj = new Object[11];
                        teamObj[0] = info.getTeam_bet();
                        teamObj[1] = info.getPk_bet();
                        teamObj[2] = info.getPllx_bet();
                        teamObj[3] = info.getPl_bet();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        teamObj[4] = this.getRandomString((int)(1+Math.random()*(7-1+1)));//simpleDateFormat.format(new Date());
                        teamObj[5] = info.getTeam_wb();
                        teamObj[6] = info.getPk_wb();
                        teamObj[7] = info.getPllx_wb();
                        teamObj[8] = info.getPl_wb();
                        teamObj[9] = info.getEnrn_money();
                        tableModel.addRow(teamObj);
                        //table.setModel(tableModel);
                    }
                    String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPl_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
                            "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
                            " 盈利金额:【" + info.getEnrn_money() + "】";
                    System.out.println(infoStr);
                    rsMap.remove(key);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}