package com.suarez.webporter.deal;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.suarez.webporter.domain.DataInfo;
import com.suarez.webporter.domain.Source;
import com.suarez.webporter.domain.TeamInfo;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2020/10/11.
 */
public class WebPhaser {

    /**
     * 计算当前赔率是否存在收益
     *
     * @param money  本金
     * @param team   比赛场次
     * @param big_pl 大球赔率
     * @param sm_pl  小球赔率
     * @param pk     盘口
     * @return 返回结果
     */
    public static ResultInfo webpoterPhase(int money, String team, Double big_pl, Double sm_pl, String pk) {
        ResultInfo resultInfo = new ResultInfo();
        Double sm_money = (big_pl + 1) * money / (sm_pl + big_pl + 2);
        Double big_money = money - sm_money;
        Double enrn_money = big_money * big_pl - sm_money;
        if (big_money * big_pl - sm_money > 0) {
            //存在收益
            resultInfo.setIsTrue(true);
            resultInfo.setBig_pl(String.valueOf(big_pl));
            resultInfo.setSm_pl(String.valueOf(sm_pl));
            resultInfo.setTeam(team);
            resultInfo.setPk(pk);
            resultInfo.setBig_money(String.valueOf(Math.round(big_money)));
            resultInfo.setSm_money(String.valueOf(Math.round(sm_money)));
            resultInfo.setEnrn_money(String.valueOf(Math.round(enrn_money)));
        } else {
            resultInfo.setIsTrue(false);
            resultInfo.setBig_pl(String.valueOf(big_pl));
            resultInfo.setSm_pl(String.valueOf(sm_pl));
            resultInfo.setTeam(team);
            resultInfo.setPk(pk);
        }
        return resultInfo;
    }

    public static String printResultInfo(ResultInfo resultInfo, String big_ly,String sm_ly) {
        //System.out.println("是否满足收益条件:" + resultInfo.getIsTrue());
        if (resultInfo.getIsTrue()) {
            String rsStr = big_ly+"  场次: " + resultInfo.getTeam() + " 盘口:" + resultInfo.getPk() +"大球赔率:" + resultInfo.getBig_pl() + " 大球金额:【" + resultInfo.getBig_money() + "】-----   " +
                    sm_ly + " 小球赔率:" + resultInfo.getSm_pl() + "小球金额:【" + resultInfo.getSm_money() +
                    "】 盈利金额:【" + resultInfo.getEnrn_money()+"】";
            System.out.println(rsStr);
            return rsStr;
        } else {
            String rsStr = "场次:" + resultInfo.getTeam() + "盘口:" + resultInfo.getPk() +
                    "大球赔率:" + resultInfo.getBig_pl() + "小球赔率:" + resultInfo.getSm_pl();
            //System.out.println(rsStr);
            return Strings.EMPTY;
        }
    }

    public static void WebporerDeal(String dataStrPrimary, String dataStrCustom) {
        List<ResultInfo> resultInfoList = new ArrayList<>();
        Gson gson = new Gson();
        TeamInfo teamInfoPrimary = gson.fromJson(dataStrPrimary, TeamInfo.class);
        String keyPrimary = teamInfoPrimary.getKeyName();
        List<DataInfo> infoListPrimary = teamInfoPrimary.getInfo();

        TeamInfo teamInfoCustom = gson.fromJson(dataStrCustom, TeamInfo.class);
        String keyCustom = teamInfoCustom.getKeyName();
        List<DataInfo> infoListCustom = teamInfoCustom.getInfo();

        Map<String, DataInfo> mapPrimary = Maps.newHashMap();
        for (DataInfo dataInfo : infoListPrimary) {
            String key = dataInfo.getPoint();
            mapPrimary.put(key, dataInfo);
        }
        for (DataInfo dataInfo : infoListCustom) {
            String point = dataInfo.getPoint();
            DataInfo dataInfoPrimary = mapPrimary.get(point);
            if (dataInfoPrimary != null) {
                ResultInfo resultInfo_big = WebPhaser.webpoterPhase(250, keyPrimary,
                        Double.valueOf(dataInfoPrimary.getBig_pl()),
                        Double.valueOf(dataInfo.getSm_pl()), point);
                //TODO 推送消息
                WebPhaser.printResultInfo(resultInfo_big,"bet","wb");

                //bet-小 wb-大
                ResultInfo resultInfo_sm = WebPhaser.webpoterPhase(250, keyCustom,
                        Double.valueOf(dataInfoPrimary.getSm_pl()),
                        Double.valueOf(dataInfo.getBig_pl()), point);
                //TODO 推送消息
                WebPhaser.printResultInfo(resultInfo_sm,"wb","bet");
            }
        }
    }


    public static void main(String[] args) {
        ResultInfo resultInfo = webpoterPhase(2500, "test", 1.05, 1.06, "0.5");
        System.out.println("是否满足收益条件:" + resultInfo.getIsTrue());
        if (resultInfo.getIsTrue()) {
            String rsStr = "场次:" + resultInfo.getTeam() + "盘口:" + resultInfo.getPk() +
                    "大球赔率:" + resultInfo.getBig_pl() + "大球金额:" + resultInfo.getBig_money() +
                    "小球赔率:" + resultInfo.getSm_pl() + "小球金额:" + resultInfo.getSm_money() +
                    "盈利金额:" + resultInfo.getEnrn_money();
            System.out.println(rsStr);
        }
    }
}