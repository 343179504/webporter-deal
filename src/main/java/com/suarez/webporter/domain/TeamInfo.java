package com.suarez.webporter.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by feng on 2020/10/12.
 */
@Data
public class TeamInfo {
    private String keyName;
    private String teamText;
    private List<Source> source;
}
