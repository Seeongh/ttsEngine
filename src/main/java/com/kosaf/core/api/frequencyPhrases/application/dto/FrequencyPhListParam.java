package com.kosaf.core.api.frequencyPhrases.application.dto;

import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class FrequencyPhListParam {

    //대체 키워드 목록 조회를 위한 Parameter 정의
    private final static int PAGE = 1;
    private final static int DEFAULT_PAGE_SCALE = 10;

    /**
     * 조회페이지
     */
    private int page = PAGE;

    /**
     * 페이지당 게시물수
     */
    private Integer pageScale = DEFAULT_PAGE_SCALE;

    //keyword seq
    private int phraseSeq;
    //frequency phrases seq

    private String keyword;

    //사용 여부 enum
//   @Enumerated(EnumType.STRING)
//    private UseFilter useAt;

    private String useAt;

    @Builder
    public FrequencyPhListParam(Integer page, Integer pageScale, int phraseSeq, String keyword,
                              String useAt) {
        this.page = (page == null) ? PAGE : page;
        this.pageScale = (pageScale == null) ? DEFAULT_PAGE_SCALE : pageScale;
        this.phraseSeq = phraseSeq;
        this.keyword = keyword;
        this.useAt = useAt;

    }

}
