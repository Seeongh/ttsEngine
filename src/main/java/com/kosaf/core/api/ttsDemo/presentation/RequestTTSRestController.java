package com.kosaf.core.api.ttsDemo.presentation;

import com.kosaf.core.api.ttsDemo.application.RequestTTSRestService;
import com.kosaf.core.api.ttsDemo.application.dto.RequestTTSDTO;
import com.kosaf.core.api.ttsDemo.application.dto.ResponseTTSDTO;
import com.kosaf.core.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RequestTTSRestController {

    private final RequestTTSRestService TtsService;

    /**
     * tts요청하기
     */
    @PostMapping("RequestTTS")
    public Mono<ResponseEntity<Resource>> requestTTS(@RequestBody RequestTTSDTO ttsDto) {
        log.info("ash requestTTS get Param : {}", ttsDto.toString());
        return TtsService.requestTTS(ttsDto);
    }

}
