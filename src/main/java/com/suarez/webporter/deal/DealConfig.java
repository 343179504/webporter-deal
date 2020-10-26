package com.suarez.webporter.deal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class DealConfig {
    @Value("${webpoter.phase.money}")
    private int webpoterPhaseMoney;

    @Value("${webpoter.phase.earn.money}")
    private int webpoterPhaseEarnMoney;
}
