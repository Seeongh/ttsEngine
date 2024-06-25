package com.kosaf.core.api.monitoring.application;

import com.kosaf.core.api.monitoring.application.dto.ResponseServerDto;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.config.webClient.ServerCaller;
import com.kosaf.core.config.webClient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    public final ServerCaller serverCaller;

    public ApiResponse< List<ResponseServerDto>> healthCheck() {
        List<ResponseServerDto> resultList = serverCaller.getMonitoringData();
        if(!resultList.isEmpty()) {
            return ApiResponse.of(HttpStatus.OK, resultList);
        }
        else {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }
}
