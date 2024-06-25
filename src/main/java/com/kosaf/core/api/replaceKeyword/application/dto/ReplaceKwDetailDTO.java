package com.kosaf.core.api.replaceKeyword.application.dto;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@AllArgsConstructor
public class ReplaceKwDetailDTO {
    private int replaceKwNo;
    private int rkeywordSeq;
    private String mainKw; //메인 키워드
    private String replaceKw; // 대체 키워드

    @Enumerated(EnumType.STRING)
    private UseFilter useAt; //사용여부

    private String registId; //등록인id
    private String registDt; //등록일자
    private String updtDt; //변경일자

    public static ReplaceKwDetailDTO of(ReplaceKw replaceKw) {
        return ReplaceKwDetailDTO
                .builder()
                .replaceKwNo(replaceKw.getReplaceKwNo())
                .rkeywordSeq(replaceKw.getRkeywordSeq())
                .mainKw(replaceKw.getMainKw())
                .replaceKw(replaceKw.getReplaceKw())
                .useAt(replaceKw.getUseAt())
                .registId(replaceKw.getRegistId())
                .registDt(replaceKw.getRegistDt())
                .updtDt(replaceKw.getUpdtDt())
                .build();
    }

}
