package com.kosaf.core.api.replaceKeyword.application.dto;

import com.kosaf.core.api.replaceKeyword.domain.InputKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InputKeywordDTO {

    int num;
    String mainKw;
    String replaceKw;
    boolean duplicated;

    public static InputKeywordDTO of(InputKeyword inputkeyword) {
        return InputKeywordDTO
                .builder()
                .num(inputkeyword.getNum())
                .mainKw(inputkeyword.getMainKw())
                .replaceKw(inputkeyword.getReplaceKw())
                .duplicated(inputkeyword.isDuplicated())
                .build();
    }

}
