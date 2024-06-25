package com.kosaf.core.config.webClient;

import com.kosaf.core.api.monitoring.application.dto.ResponseBodyDto;
import com.kosaf.core.api.monitoring.application.dto.ResponseServerDto;
import com.kosaf.core.config.webClient.dto.RequestReplaceKw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;

    /**
     * 대체어/ 자주사용하는 문구
     * @param requestBody
     * @param uri
     * @return
     */
    public Mono<Map> requestPostresponse(String requestBody, String uri) {
        log.info("requestPostresponse requestBody= {}, uri = {}", requestBody, uri);
        return webClient
                .post()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(e -> {
                    // 에러 로그 추가
                    log.error("Error in requestPostResponseAsync: ", e);
                })
                .onErrorResume(e -> {
                   // 에러 발생 시 대체 값 반환
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    errorResponse.put("message", e.getMessage());
                    return Mono.just(errorResponse);
               });
    }

    /**
     * 모니터링 & 대체어 리스트 받기
     * @param uri
     * @return
     */
    public Mono<ResponseBodyDto> requestGetresponse(String uri) {
        return    webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ResponseBodyDto.class)
                .onErrorResume(e -> {
                    log.error("Error in requestGetResponse: ", e); // 에러 로그 추가
                    return Mono.just(ResponseBodyDto.builder()
                            .code( HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message( e.getMessage())
                            .result(null)
                            .build());
                });
    }
    /**
     * 서버 모니터링
     * @return
     */
    public Mono<ResponseServerDto> extractType(String uri) {
        return this.requestGetresponse(uri).flatMap(response -> {
            ResponseServerDto serverDto =
                    extractTypesFromMap(response.getResult());
            return Mono.just(serverDto);
        });
    }

    private ResponseServerDto extractTypesFromMap(Map<String, Object> map) {
        if(map ==null) {
            return new ResponseServerDto("None", new ArrayList<>());
        }

        ResponseServerDto serverDto = new ResponseServerDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            log.info("ash result = {}", entry.toString());
            if (entry.getKey().endsWith(".type")) {
                serverDto.setApiType(entry.getValue().toString());
            } else if (entry.getKey().equals("9.servers") && entry.getValue() instanceof Map) {
                Map<String, Object> servers = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> serverEntry : servers.entrySet()) {
                    if (serverEntry.getValue() instanceof Map) {
                        Map<String, Object> serverDetails = (Map<String, Object>) serverEntry.getValue();
                        if (serverDetails.containsKey("0.type")) {
                            Map<String, String> serverTypeEntry = new HashMap<>();
                            serverTypeEntry.put(serverEntry.getKey(), serverDetails.get("0.type").toString());
                            serverDto.getResultList().add(serverTypeEntry);
                        }
                    }
                }
            }
        }
        return serverDto;
    }
}
