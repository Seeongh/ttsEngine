package com.kosaf.core.api.ttsDemo.application;

import com.kosaf.core.api.ttsDemo.application.dto.RequestTTSDTO;
import com.kosaf.core.api.ttsDemo.application.dto.ResponseTTSDTO;
import com.kosaf.core.config.webClient.ServerCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestTTSRestService {

    public final ServerCaller serverCaller;

    public Mono<ResponseEntity<Resource>>  requestTTS(RequestTTSDTO ttsDto) {
        return serverCaller.generateWaveFile(ttsDto)
                .map(resource -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf("audio/wav"))
                        .body(resource));
    }
}
