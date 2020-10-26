package com.suarez.webporter.deal;

import com.suarez.webporter.deal.bet_wb.Bet_Wb_Info;
import com.suarez.webporter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public abstract class BasicDeal implements Runnable {
    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected DealConfig dealConfig;

    public static Map<String, Bet_Wb_Info> map = new ConcurrentHashMap<>();
}
