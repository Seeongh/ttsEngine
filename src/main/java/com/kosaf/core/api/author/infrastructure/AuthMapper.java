package com.kosaf.core.api.author.infrastructure;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    public Long findById(String userId);
}
