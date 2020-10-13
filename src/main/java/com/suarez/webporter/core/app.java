package com.suarez.webporter.core;


import com.google.gson.Gson;
import com.suarez.webporter.deal.WebPhaser;
import com.suarez.webporter.domain.DataInfo;
import com.suarez.webporter.domain.Source;
import com.suarez.webporter.domain.TeamInfo;
import com.suarez.webporter.util.RedisUtil;
import com.suarez.webporter.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class app implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private RedisUtil redisUtil;

    public static Map<String, DataInfo> map = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        while (true){
            Set<String> Wbkeys = redisUtil.keysByPre("wb");
            Set<String> Betkeys = redisUtil.keysByPre("bet");
            for (String betkey : Betkeys) {
                float maxSimilarity=0;
                String tmp_keyName= "";
                for (String wbkey : Wbkeys) {
                    float tmp = Util.levenshtein(wbkey,betkey);
                    if(tmp>maxSimilarity){
                        maxSimilarity=tmp;
                        tmp_keyName=wbkey;
                    }

                }
                if(maxSimilarity>0.4){
                    WebPhaser.WebporterDeal(redisUtil.get(tmp_keyName),redisUtil.get(betkey));
                }else{
                    //System.out.println(betkey+"未找到匹配的赛事....");
                }

            }

        }
    }
}
