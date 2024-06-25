package com.kosaf.core.api.frequencyPhrases.domain;

import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FrequencyPh {

    private final static int INQ_CNT = 0;
    private int phraseNo;
    private int phraseSeq;
    private String frequencyPh; //자주사용하는 문장

    private UseFilter useAt; //사용여부
    private String registId; //등록인id
    private String updtId; //등록인id
    private String registDt; //등록일자
    private String updtDt; //변경일자


    @Builder
    public FrequencyPh(int phraseNo, int phraseSeq, String frequencyPh, UseFilter useAt, String registId, String updtId, String registDt, String updtDt) {
        this.phraseNo = phraseNo;
        this.phraseSeq = phraseSeq;
        this.frequencyPh = frequencyPh;
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
