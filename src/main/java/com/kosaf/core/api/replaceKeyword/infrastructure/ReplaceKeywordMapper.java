package com.kosaf.core.api.replaceKeyword.infrastructure;

import com.kosaf.core.api.replaceKeyword.application.dto.ReplaceKwListParam;
import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplaceKeywordMapper {
    //select
    public Integer countAll(ReplaceKwListParam param);

    public List<ReplaceKw> findAll(ReplaceKwListParam param);

    public ReplaceKw findById(int rkeywordSeq);
    public ReplaceKw findByString(String keyword);

    public Long create(ReplaceKw replaceKw);

    public Long update(ReplaceKw replaceKw);

    public Long delete(ReplaceKw replaceKw);

}
