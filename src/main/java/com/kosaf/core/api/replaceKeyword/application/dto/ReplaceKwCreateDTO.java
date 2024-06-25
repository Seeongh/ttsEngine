package com.kosaf.core.api.replaceKeyword.application.dto;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import com.kosaf.core.config.validation.ValidGroups;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceKwCreateDTO {

    //@NotEmpty(message = "메인 키워드는 필수 입력 값입니다.", groups = ValidGroups.First.class)
    private String mainKw; //메인 키워드

   // @NotEmpty(message = "대체 키워드는 필수 입력 값입니다.", groups = ValidGroups.Second.class)
    private String replaceKw; // 대체 키워드

    List<ReplaceKwCreateDTO> keywordDtoList;

    public ReplaceKwCreateDTO(String mainKw, String replaceKw) {
        this.mainKw = mainKw;
        this.replaceKw = replaceKw;
    }

    public ReplaceKw toEntity() {
        return ReplaceKw
                .builder()
                .mainKw(mainKw)
                .replaceKw(replaceKw)
                .useAt(UseFilter.valueOf("Y"))
                .build();
    }

}
