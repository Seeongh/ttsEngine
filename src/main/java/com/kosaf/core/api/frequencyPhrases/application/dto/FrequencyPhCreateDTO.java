package com.kosaf.core.api.frequencyPhrases.application.dto;

import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyPhCreateDTO {

    private String frequencyPh; //자주 사용하는 문구

    List<FrequencyPhCreateDTO> fpDtoList;

    public FrequencyPhCreateDTO(String frequencyPh) {
        this.frequencyPh = frequencyPh;
    }

    public FrequencyPh toEntity() {
        return FrequencyPh
                .builder()
                .frequencyPh(frequencyPh)
                .useAt(UseFilter.valueOf("Y"))
                .build();
    }

}
