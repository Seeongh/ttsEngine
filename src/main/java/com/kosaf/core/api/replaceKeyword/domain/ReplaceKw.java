package com.kosaf.core.api.replaceKeyword.domain;

import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplaceKw {

    private final static int INQ_CNT = 0;
    private int replaceKwNo;
    private int rkeywordSeq;
    private String mainKw; //메인 키워드
    private String replaceKw; // 대체 키워드

    private UseFilter useAt; //사용여부
    private String registId; //등록인id
    private String updtId; //등록인id
    private String registDt; //등록일자
    private String updtDt; //변경일자

    @Builder
    public ReplaceKw(int replaceKwNo, int rkeywordSeq, String mainKw, String replaceKw, UseFilter useAt, String registId, String updtId, String registDt, String updtDt) {
        this.replaceKwNo = replaceKwNo;
        this.rkeywordSeq = rkeywordSeq;
        this.mainKw = mainKw;
        this.replaceKw = replaceKw;
        this.useAt = useAt;
        this.registId = registId;
        this.updtId = updtId;
        this.registDt = registDt;
        this.updtDt = updtDt;
    }
    public void create() {

    }
    public void update() {

    }
    public void delete() {

    }

}
