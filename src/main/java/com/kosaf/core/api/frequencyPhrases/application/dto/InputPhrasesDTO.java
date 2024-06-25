package com.kosaf.core.api.frequencyPhrases.application.dto;

import com.kosaf.core.api.frequencyPhrases.domain.InputPhrases;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InputPhrasesDTO {

    int num;
    String frequencyPh;
    boolean duplicated;

    public static InputPhrasesDTO of(InputPhrases inputPhrases) {
        return InputPhrasesDTO
                .builder()
                .num(inputPhrases.getNum())
                .frequencyPh(inputPhrases.getFrequencyPh())
                .duplicated(inputPhrases.isDuplicated())
                .build();
    }

}
