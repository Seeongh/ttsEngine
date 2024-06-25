package com.kosaf.core.api.frequencyPhrases.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InputPhrases {
    private final static int INQ_CNT = 0;
    int num;
    String frequencyPh;
    boolean duplicated;

    @Builder
    public InputPhrases(int num,  String frequencyPh, boolean duplicated) {
        this.num = num;
        this.frequencyPh = frequencyPh;
        this.duplicated = duplicated;
    }
}
