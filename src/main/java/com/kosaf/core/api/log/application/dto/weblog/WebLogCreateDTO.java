package com.kosaf.core.api.log.application.dto.weblog;

import com.kosaf.core.api.log.domain.WebLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WebLogCreateDTO {

  private String occrrncDe;
  private String occrrncTime;
  private String url;
  private String rqesterIp;
  private Integer rqesterId;

  // dto to entity
  public WebLog toEntity() {
    return WebLog
        .builder()
        .occrrncDe(occrrncDe)
        .occrrncTime(occrrncTime)
        .url(url)
        .rqesterIp(rqesterIp)
        .rqesterId(rqesterId)
        .build();
  }

}
