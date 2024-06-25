package com.kosaf.core.config.webClient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestReplaceKw {
    String command;
    String[] words;


}
