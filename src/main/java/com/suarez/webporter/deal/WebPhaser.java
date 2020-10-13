package com.suarez.webporter.deal;

import com.google.gson.Gson;
import com.suarez.webporter.domain.DataInfo;
import com.suarez.webporter.domain.Source;
import com.suarez.webporter.domain.TeamInfo;

import java.util.List;

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

    public static void printResultInfo(ResultInfo resultInfo) {
        System.out.println("是否满足收益条件:" + resultInfo.getIsTrue());
        if (resultInfo.getIsTrue()) {
            String rsStr = "场次:" + resultInfo.getTeam() + "盘口:" + resultInfo.getPk() +
                    "大球赔率:" + resultInfo.getBig_pl() + "大球金额:" + resultInfo.getBig_money() +
                    "小球赔率:" + resultInfo.getSm_pl() + "小球金额:" + resultInfo.getSm_money() +
                    "盈利金额:" + resultInfo.getEnrn_money();
            System.out.println(rsStr);
        } else {
            String rsStr = "场次:" + resultInfo.getTeam() + "盘口:" + resultInfo.getPk() +
                    "大球赔率:" + resultInfo.getBig_pl() + "小球赔率:" + resultInfo.getSm_pl();
            System.out.println(rsStr);
        }
    }

    public static void WebporterDeal(String dataStr) {
        Gson gson = new Gson();
        TeamInfo teamInfo = gson.fromJson(dataStr, TeamInfo.class);
        String key = teamInfo.getKeyName();
        List<Source> sourceList = teamInfo.getSource();
        Source wbSource = null;
        Source betSource = null;
        label:
        for (Source source : sourceList) {
            switch (source.getName()) {
                case "wb":
                    wbSource = source;
                    break;
                case "bet":
                    betSource = source;
                    break;
                default:
                    break label;
            }
        }

        if (wbSource != null && betSource != null) {
            List<DataInfo> bet_dataInfoList = betSource.getInfo();
            List<DataInfo> wb_dataInfoList = wbSource.getInfo();
            for (DataInfo bet_dataInfo : bet_dataInfoList) {
                String point = bet_dataInfo.getPoint();
                for (DataInfo wb_dataInfo : wb_dataInfoList) {
                    if (wb_dataInfo.getPoint().equals(point)) {
                        //bet 大 wb 小
                        ResultInfo resultInfo_big = WebPhaser.webpoterPhase(2500, key,
                                Double.valueOf(bet_dataInfo.getBig_pl()),
                                Double.valueOf(wb_dataInfo.getSm_pl()), point);
                        //TODO 推送消息
                        WebPhaser.printResultInfo(resultInfo_big);

                        //bet-小 wb-大
                        ResultInfo resultInfo_sm = WebPhaser.webpoterPhase(2500, key,
                                Double.valueOf(bet_dataInfo.getSm_pl()),
                                Double.valueOf(wb_dataInfo.getBig_pl()), point);
                        //TODO 推送消息
                        WebPhaser.printResultInfo(resultInfo_sm);
                    }
                }
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