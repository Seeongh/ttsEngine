package com.kosaf.core.api.monitoring.presentation;

import com.kosaf.core.api.monitoring.application.MonitoringService;
import com.kosaf.core.api.monitoring.application.dto.ResponseServerDto;
import com.kosaf.core.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MonitoringRestController {

    private final MonitoringService monitoringService;

    /**
     * 서버 상태 가지고오기
     */
    @GetMapping("/health-check")
    public ApiResponse<List<ResponseServerDto>> healthCheck() {
        return monitoringService.healthCheck();
    }

}
