package com.suarez.webporter.deal;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.suarez.webporter.deal.bet_wb.Bet_Wb_Info;
import com.suarez.webporter.domain.DataInfo;
import com.suarez.webporter.domain.TeamInfo;
import com.suarez.webporter.util.RedisUtil;
import com.suarez.webporter.util.Util;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class BetWbDeal implements Runnable {
    @Autowired
    private RedisUtil redisUtil;

    public static Map<String, Bet_Wb_Info> map = new ConcurrentHashMap<>();


    public void begin() {
        while (true) {
            try {
                Set<String> Wbkeys = redisUtil.keysByPre("wb");
                Set<String> Betkeys = redisUtil.keysByPre("bet");
                for (String betkey : Betkeys) {
                    float maxSimilarity = 0;
                    String tmp_keyName = "";
                    for (String wbkey : Wbkeys) {
                        float tmp = Util.levenshtein(wbkey, betkey);
                        if (tmp > maxSimilarity) {
                            maxSimilarity = tmp;
                            tmp_keyName = wbkey;
                        }
                    }
                    if (maxSimilarity > 0.4) {
                        //分析
                        deal(redisUtil.get(betkey), redisUtil.get(tmp_keyName));
                    } else {
                        //System.out.println(betkey+"未找到匹配的赛事....");
                    }
                    redisUtil.removeKey(betkey);
                }

            } catch (Exception e) {
                //TODO
            }
        }
    }

    /**
     * 计算分析
     *
     * @param betStr bet
     * @param wbStr  wb
     */
    private void deal(String betStr, String wbStr) {
        Gson gson = new Gson();
        TeamInfo teamInfoBet = gson.fromJson(betStr, TeamInfo.class);
        String keyBet = teamInfoBet.getKeyName();
        List<DataInfo> infoListBet = teamInfoBet.getInfo();

        TeamInfo teamInfoWb = gson.fromJson(wbStr, TeamInfo.class);
        String keyWb = teamInfoWb.getKeyName();
        List<DataInfo> infoListWb = teamInfoWb.getInfo();

        Map<String, DataInfo> mapBet = Maps.newHashMap();
        for (DataInfo dataInfoBet : infoListBet) {
            String key = dataInfoBet.getPoint();
            mapBet.put(key, dataInfoBet);
        }
        for (DataInfo dataInfoWb : infoListWb) {
            String pointWb = dataInfoWb.getPoint();
            DataInfo dataInfoBet = mapBet.get(pointWb);
            if (dataInfoBet != null) {
                ResultInfo resultInfo_big = WebPhaser.webpoterPhase(1000, keyBet,
                        Double.valueOf(dataInfoBet.getBig_pl()),
                        Double.valueOf(dataInfoWb.getSm_pl()), pointWb);
                if (resultInfo_big.getIsTrue()) {
                    //推送消息
                    Bet_Wb_Info info = new Bet_Wb_Info();
                    info.setIsTrue(true);
                    //设置bet信息
                    info.setTeam_bet(teamInfoBet.getKeyName());
                    info.setPk_bet(dataInfoBet.getPoint());
                    info.setPllx_bet("大");
                    info.setPl_bet(dataInfoBet.getBig_pl());
                    info.setMoney_bet(resultInfo_big.getBig_money());
                    //设置wb信息
                    info.setTeam_wb(teamInfoWb.getKeyName());
                    info.setPk_wb(dataInfoWb.getPoint());
                    info.setPllx_wb("小");
                    info.setPl_wb(dataInfoWb.getSm_pl());
                    info.setMoney_wb(resultInfo_big.getSm_money());
                    //设置金额
                    info.setEnrn_money(resultInfo_big.getEnrn_money());
                    map.put(teamInfoBet.getKeyName() + info.getPllx_bet(), info);
                    String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPl_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
                            "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
                            " 盈利金额:【" + info.getEnrn_money() + "】";
                    System.out.println(infoStr);
                }

                //bet-小 wb-大
                ResultInfo resultInfo_sm = WebPhaser.webpoterPhase(1000, keyWb,
                        Double.valueOf(dataInfoBet.getSm_pl()),
                        Double.valueOf(dataInfoWb.getBig_pl()), pointWb);
                //TODO 推送消息
                if (resultInfo_sm.getIsTrue()) {
                    //推送消息
                    Bet_Wb_Info info = new Bet_Wb_Info();
                    info.setIsTrue(true);
                    //设置bet信息
                    info.setTeam_bet(teamInfoBet.getKeyName());
                    info.setPk_bet(dataInfoBet.getPoint());
                    info.setPllx_bet("小");
                    info.setPl_bet(dataInfoBet.getSm_pl());
                    info.setMoney_bet(resultInfo_sm.getSm_money());
                    //设置wb信息
                    info.setTeam_wb(teamInfoWb.getKeyName());
                    info.setPk_wb(dataInfoWb.getPoint());
                    info.setPllx_wb("大");
                    info.setPl_wb(dataInfoWb.getBig_pl());
                    info.setMoney_wb(resultInfo_sm.getBig_money());
                    //设置金额
                    info.setEnrn_money(resultInfo_sm.getEnrn_money());
                    map.put(teamInfoBet.getKeyName() + info.getPllx_bet(), info);
                    String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPk_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
                            "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
                            " 盈利金额:【" + info.getEnrn_money() + "】";
                    System.out.println(infoStr);
                }
            }
        }

        //TODO 测试
//        for (int i = 0; i < 500; i++) {
//            Bet_Wb_Info info = new Bet_Wb_Info();
//            info.setIsTrue(true);
//            Random random = new Random();
//            //设置bet信息
//            info.setTeam_bet("5555" + i);
//            info.setPk_bet("5555");
//            info.setPllx_bet("大");
//            info.setPl_bet(String.valueOf(random.nextInt()));
//            info.setMoney_bet("5555");
//            //设置wb信息
//            info.setTeam_wb("0000");
//            info.setPk_wb(String.valueOf(random.nextInt(10)));
//            info.setPllx_wb("小");
//            info.setPl_wb("0000");
//            info.setMoney_wb("0000");
//            //设置金额
//            info.setEnrn_money("0000");
//            map.put(info.getTeam_bet() + info.getPllx_bet(), info);
//            String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPl_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
//                    "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
//                    " 盈利金额:【" + info.getEnrn_money() + "】";
//            //System.out.println(infoStr);
//        }

    }

    @Override
    public void run() {
        begin();
    }
}
