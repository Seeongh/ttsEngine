package com.kosaf.core.api.replaceKeyword.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InputKeyword {
    private final static int INQ_CNT = 0;
    int num;
    String mainKw;
    String replaceKw;
    String frequencyPhrases;
    boolean duplicated;

    @Builder
    public InputKeyword(int num, String mainKw, String replaceKw, String frequencyPhrases, boolean duplicated) {
        this.num = num;
        this.mainKw = mainKw;
        this.replaceKw = replaceKw;
        this.frequencyPhrases = frequencyPhrases;
        this.duplicated = duplicated;
    }
}
