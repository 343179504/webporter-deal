package com.suarez.webporter.core;


import com.google.gson.Gson;
import com.suarez.webporter.deal.WebPhaser;
import com.suarez.webporter.domain.DataInfo;
import com.suarez.webporter.domain.Source;
import com.suarez.webporter.domain.TeamInfo;
import com.suarez.webporter.util.RedisUtil;
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
            Set<String> keys = redisUtil.keys();
            for (String key : keys) {
                String val = redisUtil.get(key);
                WebPhaser.WebporterDeal(val);
                //TODO 测试速度
//                Gson gson = new Gson();
//                TeamInfo teamInfo = gson.fromJson(val, TeamInfo.class);
//                String keyName = teamInfo.getKeyName();
//                List<Source> sourceList = teamInfo.getSource();
//                Source wbSource = null;
//                label:
//                for (Source source : sourceList) {
//                    switch (source.getName()) {
//                        case "bet":
//                            wbSource = source;
//                            break;
//                        default:
//                            break label;
//                    }
//                }
//
//                List<DataInfo> infoList = wbSource.getInfo();
//                for (DataInfo dataInfo : infoList) {
//                    DataInfo old_info = map.get(key + dataInfo.getPoint());
//                    if (old_info == null) {
//                        map.put(key + dataInfo.getPoint(), dataInfo);
//                    } else {
//                        if (!dataInfo.getBig_pl().equals(old_info.getBig_pl())) {
//                            //TODO 大球变化
//                            System.out.println("team:"+key+" 盘口:" + dataInfo.getPoint() + " 大球:" +dataInfo.getBig_pl());
//                        }
//                        if (!dataInfo.getSm_pl().equals(old_info.getSm_pl())) {
//                            //TODO 小球变化
//                            System.out.println("team:"+key+" 盘口:" + dataInfo.getPoint() + " 小球:" +dataInfo.getSm_pl());
//                        }
//                        map.put(key + dataInfo.getPoint(), dataInfo);
//                    }
//                }
            }
        }
    }
}
