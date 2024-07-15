package com.kosaf.core.config.webClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kosaf.core.api.frequencyPhrases.application.dto.FrequencyPhCreateDTO;
import com.kosaf.core.api.frequencyPhrases.application.dto.RequestCacheMethod;
import com.kosaf.core.api.monitoring.application.dto.ResponseBodyDto;
import com.kosaf.core.api.monitoring.application.dto.ResponseServerDto;
import com.kosaf.core.api.replaceKeyword.application.dto.ReplaceKwCreateDTO;
import com.kosaf.core.api.replaceKeyword.application.dto.RequestCommandMethod;
import com.kosaf.core.api.ttsDemo.application.dto.RequestTTSDTO;
import com.kosaf.core.api.ttsDemo.application.dto.ResponseTTSDTO;
import com.kosaf.core.common.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerCaller {

    private final String server1BaseUrl = "http://tts.ai-mediazen.com:56012";
    private final String server2BaseUrl = "http://tts.ai-mediazen.com:56014";
    private final String ttsUrl = "http://tts.ai-mediazen.com:56014";
    private final WebClientService webClientService;

    /**
     * 자주 사용하는 문구 서버 호출
     * @param createDto
     * @param cacheMethod
     * @return
     */
    public Long requestCacheFrequencyPh(FrequencyPhCreateDTO createDto, String cacheMethod) {
        ObjectMapper mapper = new ObjectMapper();
        //요청 생성
        ObjectNode rootNode = createRequestBodyFP(createDto, cacheMethod);

        String requestBody = "";
        String rollbackRequest = "";
        try {
            //요청할  body 생성
            requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);


            //실패했을때를 대비한 request 생성
            rootNode.put("_CACHE", RequestCacheMethod.getRollbackMethod(cacheMethod)) ;
            rollbackRequest =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //호출
        log.info("ash frequencyPh cache = {}", requestBody);
        Mono<Map> response1 = webClientService.requestPostresponse(requestBody, server1BaseUrl);
        Mono<Map> response2 = webClientService.requestPostresponse(requestBody, server2BaseUrl);

        //결과 대기
        return responseSuccessFail(response1, response2, rollbackRequest, server1BaseUrl, server2BaseUrl);
    }

    /**
     * 자주 사용하는 문구  RequestBody 생성
     */
     private ObjectNode createRequestBodyFP(FrequencyPhCreateDTO createDto, String cacheMethod){
         ObjectMapper mapper = new ObjectMapper();
         ObjectNode rootNode = mapper.createObjectNode();
         StringBuilder frequencyPh = new StringBuilder();

         if((createDto.getFpDtoList() != null) && (!createDto.getFpDtoList().isEmpty())) { //엑셀을 통해 여러개가 들어옴
             for(FrequencyPhCreateDTO dto : createDto.getFpDtoList()) {
                 frequencyPh.append(dto.getFrequencyPh());
                 frequencyPh.append(".");
             }
         }
         else {
             frequencyPh.append(createDto.getFrequencyPh());
         }

         rootNode.put("_TEXT", String.valueOf(frequencyPh));
         rootNode.put("_CACHE", cacheMethod);
         rootNode.put("output_type", "path");

         return rootNode;
     }


    /**
     * 대체키워드 서버 호출
     * @param createDto
     * @param commandMethod
     * @param url1 : 첫번쨰 서버의 요청url, 해당 url로 요청을 할지말지 판단하는 역할도 함
     * @param url2 : 두번째 서버의 요청url, 해당 url로 요청을 할지말지 판단하는 역할도 함
     * @return
     */
    public Long requestReplaceKwServer(ReplaceKwCreateDTO createDto, String commandMethod, String url1, String url2) {

        ObjectMapper mapper = new ObjectMapper();

        Pair<ObjectNode, ArrayNode> requestBodyPair = createRequestBodyKw(createDto);
        ObjectNode rootNode = requestBodyPair.getLeft();
        ArrayNode delArray = requestBodyPair.getRight();

        String insertBody ="";
        String deleteBody = "";

        try {
            insertBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            //실패했을때를 대비한 request 생성
            rootNode.set("words", delArray) ;
            rootNode.put("command", RequestCommandMethod.delete.name());
            deleteBody =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String requestBody = "";
        String rollbackRequest = "";
        if(commandMethod.equals(RequestCommandMethod.insert.name())) {
            requestBody = insertBody;
            rollbackRequest = deleteBody;
        }
        else {
            requestBody = deleteBody;
            rollbackRequest = insertBody;
        }

        Mono<Map>response1 = null;
        Mono<Map>response2 = null;

        if((url1 != null) && !url1.isEmpty()){
           url1 = server1BaseUrl+url1;
           response1 = webClientService.requestPostresponse(requestBody, url1);
        }
        if((url2 != null) && !url2.isEmpty()){
            url2 = server1BaseUrl+url2;
            response2 = webClientService.requestPostresponse(requestBody, url2);
        }

        return responseSuccessFail(response1, response2, rollbackRequest, url1, url2);
    }

    /**
     * 대체 키워드 관련 insert, delete 요청 생성
     * @param createDto
     * @param commandMethod
     * @return
     */
    private Pair<ObjectNode, ArrayNode> createRequestBodyKw(ReplaceKwCreateDTO createDto) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("command", RequestCommandMethod.insert.name());
        ArrayNode wordsArray = mapper.createArrayNode();
        ArrayNode delArray = mapper.createArrayNode();

        if((createDto.getKeywordDtoList() != null) && (!createDto.getKeywordDtoList().isEmpty())) { //엑셀을 통해 여러개가 들어옴
            log.info("ash servercaller list");
            for (ReplaceKwCreateDTO dto : createDto.getKeywordDtoList()) {

                ObjectNode wordNode = mapper.createObjectNode();
                wordNode.put("from", dto.getMainKw());
                wordNode.put("to", dto.getReplaceKw());
                wordsArray.add(wordNode);
                delArray.add(dto.getMainKw()); //롤백시 필요한 삭제 내용
            }
        }
        else {
            log.info("ash servercaller single");

            ObjectNode wordNode = mapper.createObjectNode();
            wordNode.put("from", createDto.getMainKw());
            wordNode.put("to", createDto.getReplaceKw());
            wordsArray.add(wordNode);
            delArray.add(createDto.getMainKw()); //롤백시 필요한 삭제 내용
        }

        rootNode.set("words", wordsArray);

        return Pair.of(rootNode, delArray);
    }

    /**
     * 모니터링 호출
     */
    public List<ResponseServerDto> getMonitoringData() {
        List<ResponseServerDto> resultList = new ArrayList<>();

        resultList.add(webClientService.extractType(server1BaseUrl+"/check-server").block());
        resultList.add(webClientService.extractType(server2BaseUrl+"/check-server").block());

        return resultList;
    }

    /**
     * 2개의 요청 성공/실패 여부 결정
     * @param response1
     * @param response2
     * @param rootNode 실패했을 경우 요청값
     * @return
     */
    private Long responseSuccessFail(Mono<Map> response1 , Mono<Map> response2, String rollbackBody, String url1, String url2) {
        log.info("response Success Fail");

        if (response1 != null && response2 != null) {
            return Mono.zip(response1, response2)
                    .flatMap(tuple -> {
                        Map res1 = tuple.getT1();
                        Map res2 = tuple.getT2();


                        /**
                         * 두개다 요청한경우
                         */

                        log.info("result res1= {}, res2= {}", res1.get("code"), res2.get("code"));

                        if (HttpStatus.OK.value() == (Integer) res1.get("code") && HttpStatus.OK.value() == (Integer) res2.get("code")) { //두개다 성공한 경우
                            return Mono.just(1L);
                        } else if (HttpStatus.OK.value() == (Integer) res1.get("code")) {
                            log.info("res1 success, res2 fail, rollback req1");
                            // res2 failed, res1 succeeded
                            webClientService.requestPostresponse(rollbackBody, url1)
                                    .flatMap(response -> {
                                        // 예외를 던지기 위한 flatMap 사용
                                        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
                                    })
                                    .onErrorResume(e -> {
                                        log.info("등록에 성공한 서버1로 롤백요청이 실패했습니다.");
                                        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
                                    });
                            return Mono.just(0L);
                        } else if (HttpStatus.OK.value() == (Integer) res2.get("code")) {
                            log.info("res1 fail, res2 success, rollback req2");
                            // res1 failed, res2 succeeded
                            webClientService.requestPostresponse(rollbackBody, url2)
                                    .flatMap(response -> {
                                        // 예외를 던지기 위한 flatMap 사용
                                        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
                                    })
                                    .onErrorResume(e -> {
                                        log.info("등록에 성공한 서버1로 롤백요청이 실패했습니다.");
                                        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
                                    });
                            return Mono.just(0L);
                        } else {
                            // Both requests failed
                            log.info("두 서버 요청이 실패했습니다.");
                            throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    })
                    .onErrorResume(e -> {
                        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
                    })
                    .block();
        }
        else if (response1 != null) {
            // response1만 요청한 경우 처리
            return Mono.just(1L).block();

        } else if (response2 != null) {
            // response2만 요청한 경우 처리
            return Mono.just(1L).block();
        } else {
            return Mono.just(0L).block(); // 모든 경우에 해당하지 않는 경우 처리
        }
    }


    /**
     * 서버 1과 2의 싱크를 맞추며
     * @return 서버1의 from , to 를 map으로 가지고온다.
     */
    public Map<String, String>  SyncServer () {
        String url1 = server1BaseUrl+"/word-replace";
        String url2 = server2BaseUrl+"/word-replace";

        Mono<ResponseBodyDto> response1 = webClientService.requestGetresponse(url1);
        Mono<ResponseBodyDto> response2 = webClientService.requestGetresponse(url2);

        return Mono.zip(response1, response2)
                .map(tuple -> {
                    ResponseBodyDto responseDto1 = tuple.getT1();
                    ResponseBodyDto responseDto2 = tuple.getT2();

                    log.info("ash 변환");
                    List<Map<String, String>> words1 = (List<Map<String, String>>) responseDto1.getResult().get("words");
                    List<Map<String, String>> words2 = (List<Map<String, String>>) responseDto2.getResult().get("words");


                    log.info("map변환");
                    Map<String, String> map1 = words1.stream().collect(Collectors.toMap(word -> word.get("from"), word -> word.get("to")));
                    Map<String, String> map2 = words2.stream().collect(Collectors.toMap(word -> word.get("from"), word -> word.get("to")));


                    log.info("Map1: {}", map1);
                    log.info("Map2: {}", map2);


                    /**
                     * 기본 컨셉 : 1번이 먼저 결과 처리가 되어서 1번을 기준으로 2번을 가공함.
                     */

                    // 1. 1번 요청에는 있는데 2번에는 없는 from to 한쌍
                    List<ReplaceKwCreateDTO> onlyInFirst = map1.entrySet().stream()
                            .filter(entry -> !map2.containsKey(entry.getKey()))
                            .map(entry -> {
                                log.info("1번 경우 = {}", entry.getKey());
                                return ReplaceKwCreateDTO
                                        .builder()
                                        .mainKw(entry.getKey())
                                        .replaceKw(entry.getValue())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    //2번서버에 요청날림.
                    if(!onlyInFirst.isEmpty()) {
                        log.info("1번 경우 2번에 없음");
                        requestReplaceKwServer(ReplaceKwCreateDTO.builder().keywordDtoList(onlyInFirst).build()
                                ,RequestCommandMethod.insert.name()
                                ,null
                                ,"/word-replace");
                    }

                    // 2. 2번 요청에는 있는데 1번에는 없는 from to 한쌍
                    List<ReplaceKwCreateDTO> onlyInSecond = map2.entrySet().stream()
                            .filter(entry -> !map1.containsKey(entry.getKey()))
                            .map(entry -> {
                                log.info("1번에 없음 delete");
                                return ReplaceKwCreateDTO
                                    .builder()
                                    .mainKw(entry.getKey())
                                    .replaceKw(entry.getValue())
                                    .build();})
                            .collect(Collectors.toList());

                    //2번 서버 지우기
                    if(!onlyInFirst.isEmpty()) {
                        log.info("2번에 있어서 지움");
                        requestReplaceKwServer(ReplaceKwCreateDTO.builder().keywordDtoList(onlyInSecond).build()
                                ,RequestCommandMethod.delete.name()
                                ,null
                                ,"/word-replace");
                    }

                    // 3. 1번과 2번의 from은 같지만 to는 다른 경우 1번의 from to 로 2번 서버로 날려서 맞춤
                    List<ReplaceKwCreateDTO> differentToValues = map1.entrySet().stream()
                            .filter(entry -> map2.containsKey(entry.getKey()) && !entry.getValue().equals(map2.get(entry.getKey())))
                            .map(entry -> {
                                log.info("두개가 안맞음 {} != {},{}", entry.getKey(),entry.getValue(), map2.get(entry.getKey()) );
                                return ReplaceKwCreateDTO
                                    .builder()
                                    .mainKw(entry.getKey())
                                    .replaceKw(entry.getValue())
                                    .build();})
                            .collect(Collectors.toList());

                    //2번서버에 요청날림.
                    if(!differentToValues.isEmpty()) {

                        log.info("두개 안맞아서 보냄");
                        requestReplaceKwServer(ReplaceKwCreateDTO.builder().keywordDtoList(differentToValues).build()
                                ,RequestCommandMethod.insert.name()
                                ,null
                                ,"/word-replace");
                    }

                    return map1;
                })
                .block();
    }

    /**
     * TTS요청
     */
    public Mono<Resource> generateWaveFile(RequestTTSDTO ttsDto) {
        return webClientService.requestTTS(ttsUrl, ttsDto);
    }
}