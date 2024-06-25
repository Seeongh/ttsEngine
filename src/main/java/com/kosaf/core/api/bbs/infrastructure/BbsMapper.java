package com.kosaf.core.api.bbs.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.bbs.application.dto.BbsDTO;
import com.kosaf.core.api.bbs.application.dto.BbsDetailDTO;
import com.kosaf.core.api.bbs.application.dto.BbsListParam;
import com.kosaf.core.api.bbs.domain.Bbs;

@Mapper
public interface BbsMapper {

  public Integer countAll(BbsListParam param);

  public List<BbsDTO> findAll(BbsListParam param);

  public BbsDetailDTO findById(Long bbsSeq);

  public Long updateInqCnt(Bbs bbs);

  public Long create(Bbs bbs);

  public Long update(Bbs bbs);

  public Long delete(Bbs bbs);
}
