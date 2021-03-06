package com.suarez.webporter.deal;


/**
 * Created by feng on 2020/10/11.
 */
public class WebPhaser {

    /**
     * 通过固定小球金额计算当前赔率是否存在收益
     *
     * @param sm_money     小球金额
     * @param team      比赛场次
     * @param big_pl    大球赔率
     * @param sm_pl     小球赔率
     * @param pk        盘口
     * @param earnMoney 盈利阈值
     * @return 返回结果
     */
    public static ResultInfo webpoterPhaseBySmMoney(int sm_money, String team, Double big_pl, Double sm_pl, String pk, int earnMoney) {
        ResultInfo resultInfo = new ResultInfo();

        Double big_money = (sm_money*(sm_pl+1))/(big_pl+1);
//        Double big_money = money - sm_money;
        Double enrn_money = big_money * big_pl - sm_money;
        if ((big_money * big_pl - sm_money > earnMoney)&&(sm_money * sm_pl - big_money > earnMoney)) {
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


    /**
     * 计算当前赔率是否存在收益
     *
     * @param money     本金
     * @param team      比赛场次
     * @param big_pl    大球赔率
     * @param sm_pl     小球赔率
     * @param pk        盘口
     * @param earnMoney 盈利阈值
     * @return 返回结果
     */
    public static ResultInfo webpoterPhase(int money, String team, Double big_pl, Double sm_pl, String pk, int earnMoney) {
        ResultInfo resultInfo = new ResultInfo();
        Double sm_money = (big_pl + 1) * money / (sm_pl + big_pl + 2);
        Double big_money = money - sm_money;
        Double enrn_money = big_money * big_pl - sm_money;
        if (big_money * big_pl - sm_money > earnMoney) {
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

    public static void printResultInfo(ResultInfo resultInfo, String big_ly, String sm_ly) {
        //System.out.println("是否满足收益条件:" + resultInfo.getIsTrue());
        if (resultInfo.getIsTrue()) {
            String rsStr = big_ly + "  场次: " + resultInfo.getTeam() + " 盘口:" + resultInfo.getPk() + "大球赔率:" + resultInfo.getBig_pl() + " 大球金额:【" + resultInfo.getBig_money() + "】-----   " +
                    sm_ly + " 小球赔率:" + resultInfo.getSm_pl() + "小球金额:【" + resultInfo.getSm_money() +
                    "】 盈利金额:【" + resultInfo.getEnrn_money() + "】";
            //System.out.println(rsStr);
        } else {
            String rsStr = "场次:" + resultInfo.getTeam() + "盘口:" + resultInfo.getPk() +
                    "大球赔率:" + resultInfo.getBig_pl() + "小球赔率:" + resultInfo.getSm_pl();
            //System.out.println(rsStr);
        }
    }


    public static void main(String[] args) {
        ResultInfo resultInfo = webpoterPhase(2500, "test", 1.05, 1.06, "0.5",0);
        System.out.println("是否满足收益条件:" + resultInfo.getIsTrue());
        if (resultInfo.getIsTrue()) {
            String rsStr = "场次:" + resultInfo.getTeam() + "盘口:" + resultInfo.getPk() +
                    "大球赔率:" + resultInfo.getBig_pl() + "大球金额:" + resultInfo.getBig_money() +
                    "小球赔率:" + resultInfo.getSm_pl() + "小球金额:" + resultInfo.getSm_money() +
                    "盈利金额:" + resultInfo.getEnrn_money();
            //System.out.println(rsStr);
        }
    }
}