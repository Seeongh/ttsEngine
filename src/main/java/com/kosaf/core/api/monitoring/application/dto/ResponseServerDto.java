package com.kosaf.core.api.monitoring.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseServerDto {
    private String ApiType; //api 서버의 상태
    private List<Map<String,String>> resultList = new ArrayList<>(); //key : 9.servers의 tts프로세스, Value: 각 tts 프로세스의 상태값

}
