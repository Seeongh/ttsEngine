package com.kosaf.core.api.frequencyPhrases.application.dto;

import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyPhUpdateDTO {

    private int phraseSeq;
    private String frequencyPh; //자주 사용하는 문구
    @Enumerated(EnumType.STRING)
     private UseFilter useAt; //사용여부


    public FrequencyPh toEntity() {
        return FrequencyPh
                .builder()
                .phraseSeq(phraseSeq)
                .frequencyPh(frequencyPh)
                .useAt(useAt)
                .build();
    }
}
