package com.kosaf.core.api.frequencyPhrases.infrastructure;

import com.kosaf.core.api.frequencyPhrases.application.dto.FrequencyPhListParam;
import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FrequencyPhrasesMapper {
    //select
    public Integer countAll(FrequencyPhListParam param);

    public List<FrequencyPh> findAll(FrequencyPhListParam param);

    public FrequencyPh findById(int phraseSeq);
    public FrequencyPh findByString(String frequencyPh);

    public Long create(FrequencyPh frequencyPh);

    public Long update(FrequencyPh frequencyPh);

    public Long delete(FrequencyPh frequencyPh);

}
