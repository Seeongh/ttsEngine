package com.kosaf.core.api.frequencyPhrases.application.dto;

import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@AllArgsConstructor
public class FrequencyPhDTO {

    private int phraseSeq;
    private String frequencyPh; //자주 사용하는 문구

    @Enumerated(EnumType.STRING)
    private UseFilter useAt; //사용여부
    private String registId; //등록인id
    private String registDt; //등록일자
    private String updtDt; //변경일자
    private Integer rgtrSysId;

    public static FrequencyPhDTO of(FrequencyPh frequencyPh) {
        return FrequencyPhDTO
                .builder()
                .phraseSeq(frequencyPh.getPhraseSeq())
                .frequencyPh(frequencyPh.getFrequencyPh())
                .useAt(frequencyPh.getUseAt())
                .build();
    }

}
