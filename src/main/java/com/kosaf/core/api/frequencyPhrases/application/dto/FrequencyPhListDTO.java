package com.kosaf.core.api.frequencyPhrases.application.dto;

import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrequencyPhListDTO {

    private int phraseNo; //순서
    private int phraseSeq;
    private String frequencyPh; //자주 사용하는 문구

    @Enumerated(EnumType.STRING)
    private UseFilter useAt; //사용여부
    private String registId; //등록인id
    private String registDt; //등록일자
    private String updtDt; //변경일자
    private String updtId; //변경일자

    public static FrequencyPhListDTO of(FrequencyPh frequencyPh) {
        return FrequencyPhListDTO
                .builder()
                .phraseNo(frequencyPh.getPhraseNo())
                .phraseSeq(frequencyPh.getPhraseSeq())
                .frequencyPh(frequencyPh.getFrequencyPh())
                .useAt(frequencyPh.getUseAt())
                .registId(frequencyPh.getRegistId())
                .registDt(frequencyPh.getRegistDt())
                .updtDt(frequencyPh.getUpdtDt())
                .updtId(frequencyPh.getUpdtId())
                .build();
    }

}
