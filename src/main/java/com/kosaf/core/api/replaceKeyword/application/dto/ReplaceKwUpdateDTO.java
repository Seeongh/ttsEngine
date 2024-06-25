package com.kosaf.core.api.replaceKeyword.application.dto;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import com.kosaf.core.config.validation.ValidGroups;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceKwUpdateDTO {

    private int rkeywordSeq;
    private String mainKw;
     private String replaceKw; // 대체 키워드
    @Enumerated(EnumType.STRING)
     private UseFilter useAt; //사용여부


    public ReplaceKw toEntity() {
        return ReplaceKw
                .builder()
                .rkeywordSeq(rkeywordSeq)
                .mainKw(mainKw)
                .replaceKw(replaceKw)
                .useAt(useAt)
                .build();
    }
}
