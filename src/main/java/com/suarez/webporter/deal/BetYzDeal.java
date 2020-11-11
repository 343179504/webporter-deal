package com.suarez.webporter.deal;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.suarez.webporter.client.Appendered;
import com.suarez.webporter.deal.bet_wb.Bet_Wb_Info;
import com.suarez.webporter.domain.DataInfo;
import com.suarez.webporter.domain.MatchTeam;
import com.suarez.webporter.domain.TeamInfo;
import com.suarez.webporter.util.PointUtil;
import com.suarez.webporter.util.RedisUtil;
import com.suarez.webporter.util.Util;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class BetYzDeal extends BasicDeal {
    public Appendered appendered;

    public void begin() {
        while (true) {
            try {
                Set<String> Wbkeys = redisUtil.keysByPre("yz");
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
                        //分析区间
                        dealArea(redisUtil.get(betkey), redisUtil.get(tmp_keyName));
                    } else {
                        //System.out.println(betkey+"未找到匹配的赛事....");
                    }
                    //redisUtil.removeKey(betkey);
                }

            } catch (Exception e) {
                //TODO
                e.printStackTrace();
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

        //记录匹配比赛
        String matchKey = "match:" + teamInfoBet.getKeyName() + " || " + teamInfoWb.getKeyName();
        MatchTeam matchTeam = new MatchTeam();
        matchTeam.setKey(matchKey);
        matchTeam.setTeam_One(teamInfoBet.getKeyName());
        matchTeam.setTeam_Two(teamInfoWb.getKeyName());
        redisUtil.set(matchKey, new Gson().toJson(matchTeam));

        Map<String, DataInfo> mapBet = Maps.newHashMap();
        for (DataInfo dataInfoBet : infoListBet) {
            String key = dataInfoBet.getPoint();
            mapBet.put(key, dataInfoBet);
        }
        for (DataInfo dataInfoWb : infoListWb) {
            String pointWb = dataInfoWb.getPoint();
            DataInfo dataInfoBet = mapBet.get(pointWb);
            if (dataInfoBet != null) {
                ResultInfo resultInfo_big = WebPhaser.webpoterPhase(dealConfig.getWebpoterPhaseMoney(), keyBet,
                        Double.valueOf(dataInfoBet.getBig_pl()),
                        Double.valueOf(dataInfoWb.getSm_pl()), pointWb, dealConfig.getWebpoterPhaseEarnMoney());

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
                    //推送数据
                    appendered.showRsInfo(info);
                    String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPl_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
                            "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
                            " 盈利金额:【" + info.getEnrn_money() + "】";
                    //System.out.println(infoStr);
                }

                //bet-小 wb-大
                ResultInfo resultInfo_sm = WebPhaser.webpoterPhase(dealConfig.getWebpoterPhaseMoney(), keyWb,
                        Double.valueOf(dataInfoBet.getSm_pl()),
                        Double.valueOf(dataInfoWb.getBig_pl()), pointWb, dealConfig.getWebpoterPhaseEarnMoney());
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
                    //推送数据
                    appendered.showRsInfo(info);
                    String infoStr = "(BET)场次: " + info.getTeam_bet() + " (BET)盘口:" + info.getPk_bet() + " (BET)赔率:(" + info.getPllx_bet() + ")" + info.getPl_bet() +
                            "(WB)场次: " + info.getTeam_wb() + " (WB)盘口:" + info.getPl_wb() + " (WB)赔率:(" + info.getPllx_wb() + ")" + info.getPl_wb() +
                            " 盈利金额:【" + info.getEnrn_money() + "】";
                    //System.out.println(infoStr);
                }
            }
        }
    }

    /**
     * 计算分析区间
     *
     * @param betStr bet
     * @param wbStr  wb
     */
    private void dealArea(String betStr, String wbStr) {
        Gson gson = new Gson();
        TeamInfo teamInfoBet = gson.fromJson(betStr, TeamInfo.class);
        String keyBet = teamInfoBet.getKeyName();
        List<DataInfo> infoListBet = teamInfoBet.getInfo();

        TeamInfo teamInfoWb = gson.fromJson(wbStr, TeamInfo.class);
        List<DataInfo> infoListWb = teamInfoWb.getInfo();

        Map<String, DataInfo> mapWb = Maps.newHashMap();
        for (DataInfo dataInfoWb : infoListWb) {
            //处理盘口
            String key = PointUtil.changePoint(dataInfoWb.getPoint());
            mapWb.put(key, dataInfoWb);
        }
        for (DataInfo dataInfoBet : infoListBet) {
            String pointBet = PointUtil.changePoint(dataInfoBet.getPoint());
            //获取下区间点
            String lowerPoint = PointUtil.getPointLower(pointBet);
            DataInfo dataInfoWb = mapWb.get(lowerPoint);
            if (dataInfoWb != null) {
                //存在下区间，且存在可分析数据
                boolean isSmBet = Double.parseDouble(pointBet) > Double.parseDouble(lowerPoint);
                if (isSmBet) {
                    //bet 取小球赔率
                    ResultInfo resultInfo_lower = WebPhaser.webpoterLowerPhase(100, keyBet,
                            Double.valueOf(dataInfoBet.getSm_pl()),
                            Double.valueOf(dataInfoWb.getBig_pl()), dealConfig.getWebpoterPhaseEarnMoney());
                    //推送Bet 小球数据
                    sendRsInfo_Bet_Lower(teamInfoBet.getKeyName(), teamInfoWb.getKeyName(), dataInfoBet, dataInfoWb, resultInfo_lower);
                } else {
                    //bet取大球赔率
                    ResultInfo resultInfo_Upper = WebPhaser.webpoterLowerPhase(100, keyBet,
                            Double.valueOf(dataInfoWb.getSm_pl()),
                            Double.valueOf(dataInfoBet.getBig_pl()), dealConfig.getWebpoterPhaseEarnMoney());
                    //推送Bet 小球数据
                    sendRsInfo_Bet_Upper(teamInfoBet.getKeyName(), teamInfoWb.getKeyName(), dataInfoBet, dataInfoWb, resultInfo_Upper);
                }
            }

            //获取上区间点
            String upperPoint = PointUtil.getPointUpper(pointBet);
            DataInfo dataInfoWbUpper = mapWb.get(upperPoint);
            if (dataInfoWbUpper != null) {
                //存在上区间，且存在可分析数据
                boolean isSmBet = Double.parseDouble(pointBet) > Double.parseDouble(lowerPoint);
                if (isSmBet) {
                    //bet 取小球赔率
                    ResultInfo resultInfo_upper = WebPhaser.webpoterUpperPhase(100, keyBet,
                            Double.valueOf(dataInfoWbUpper.getBig_pl()),
                            Double.valueOf(dataInfoBet.getSm_pl()), dealConfig.getWebpoterPhaseEarnMoney());
                    //推送Bet 小球数据
                    sendRsInfo_Bet_Upper(teamInfoBet.getKeyName(), teamInfoWb.getKeyName(), dataInfoBet, dataInfoWbUpper, resultInfo_upper);
                } else {
                    //bet 取大球赔率
                    ResultInfo resultInfo_upper = WebPhaser.webpoterUpperPhase(100, keyBet,
                            Double.valueOf(dataInfoBet.getBig_pl()),
                            Double.valueOf(dataInfoWbUpper.getSm_pl()), dealConfig.getWebpoterPhaseEarnMoney());
                    //推送Bet 大球数据
                    sendRsInfo_Bet_Upper(teamInfoBet.getKeyName(), teamInfoWb.getKeyName(), dataInfoBet, dataInfoWbUpper, resultInfo_upper);
                }
            }
        }
    }

    /**
     * 推送bet小球信息
     *
     * @param teamBet     比赛场次-bet
     * @param teamWb      比赛场次-wb
     * @param dataInfoBet 盘口信息-bet
     * @param dataInfoWb  盘口信息-wb
     * @param resultInfo  分析结果
     */
    private void sendRsInfo_Bet_Lower(String teamBet, String teamWb, DataInfo dataInfoBet, DataInfo dataInfoWb, ResultInfo resultInfo) {
        if (resultInfo.getIsTrue()) {
            Bet_Wb_Info info = new Bet_Wb_Info();
            info.setIsTrue(true);
            //设置bet信息
            info.setTeam_bet(teamBet);
            info.setPk_bet(dataInfoBet.getPoint());
            info.setPllx_bet("小");
            info.setPl_bet(dataInfoBet.getSm_pl());
            info.setMoney_bet(resultInfo.getSm_money());
            //设置wb信息
            info.setTeam_wb(teamWb);
            info.setPk_wb(dataInfoWb.getPoint());
            info.setPllx_wb("大");
            info.setPl_wb(dataInfoWb.getBig_pl());
            info.setMoney_wb(resultInfo.getBig_money());
            //设置金额
            info.setEnrn_money(resultInfo.getEnrn_money());
            //推送数据
            appendered.showRsInfo(info);
        }
    }

    /**
     * 推送bet大球信息
     *
     * @param teamBet     比赛场次-bet
     * @param teamWb      比赛场次-wb
     * @param dataInfoBet 盘口信息-bet
     * @param dataInfoWb  盘口信息-wb
     * @param resultInfo  分析结果
     */
    private void sendRsInfo_Bet_Upper(String teamBet, String teamWb, DataInfo dataInfoBet, DataInfo dataInfoWb, ResultInfo resultInfo) {
        if (resultInfo.getIsTrue()) {
            Bet_Wb_Info info = new Bet_Wb_Info();
            info.setIsTrue(true);
            //设置bet信息
            info.setTeam_bet(teamBet);
            info.setPk_bet(dataInfoBet.getPoint());
            info.setPllx_bet("大");
            info.setPl_bet(dataInfoBet.getBig_pl());
            info.setMoney_bet(resultInfo.getBig_money());
            //设置wb信息
            info.setTeam_wb(teamWb);
            info.setPk_wb(dataInfoWb.getPoint());
            info.setPllx_wb("小");
            info.setPl_wb(dataInfoWb.getSm_pl());
            info.setMoney_wb(resultInfo.getSm_money());
            //设置金额
            info.setEnrn_money(resultInfo.getEnrn_money());
            //推送数据
            appendered.showRsInfo(info);
        }
    }


    public Appendered getAppendered() {
        return appendered;
    }

    public void setAppendered(Appendered appendered) {
        this.appendered = appendered;
    }

    @Override
    public void run() {
        begin();
    }
}
