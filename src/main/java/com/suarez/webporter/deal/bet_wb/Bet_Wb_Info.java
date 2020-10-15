package com.suarez.webporter.deal.bet_wb;

import lombok.Data;

/**
 * Created by feng on 2020/10/15.
 */
@Data
public class Bet_Wb_Info {
    private Boolean isTrue;
    private String team_wb;
    private String pk_wb;
    private String pl_wb;
    private String pllx_wb;
    private String money_wb;
    private String team_bet;
    private String pk_bet;
    private String pl_bet;
    private String pllx_bet;
    private String money_bet;
    private String enrn_money;

    public Bet_Wb_Info(){

    }
}
