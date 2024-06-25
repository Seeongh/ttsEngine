package com.kosaf.core.api.replaceKeyword.application.dto;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplaceKwDTO {

    private int rkeywordSeq;
    private String mainKw; //메인 키워드
    private String replaceKw; // 대체 키워드

    @Enumerated(EnumType.STRING)
    private UseFilter useAt; //사용여부
    private String registId; //등록인id
    private String registDt; //등록일자
    private String updtDt; //변경일자
    private Integer rgtrSysId;

    public static ReplaceKwDTO of(ReplaceKw replaceKw) {
        return ReplaceKwDTO
                .builder()
                .rkeywordSeq(replaceKw.getRkeywordSeq())
                .mainKw(replaceKw.getMainKw())
                .replaceKw(replaceKw.getReplaceKw())
                .useAt(replaceKw.getUseAt())
                .build();
    }
}
