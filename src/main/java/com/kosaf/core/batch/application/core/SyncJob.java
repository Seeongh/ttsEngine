package com.kosaf.core.batch.application.core;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import com.kosaf.core.api.replaceKeyword.infrastructure.ReplaceKeywordMapper;
import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import com.kosaf.core.batch.application.infrastructure.*;
import com.kosaf.core.config.webClient.ServerCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.batch.runtime.context.StepContext;
import javax.sql.DataSource;
import java.sql.SQLOutput;
import java.util.*;

/**
 * 밤마다 수행할
 * 대체어 - tts엔진 싱크 작업
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SyncJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private ReplaceKeywordMapper replaceKwMapper;

    @Autowired
    private ServerCaller serverCaller;


    @Autowired
    private DataSource dataSource;


    private int chunkSize = 10;


    @Bean(name = "syncKwJob")
    public Job syncJob(Step syncStep) {
        log.info(">>>syncJob");

        return jobBuilderFactory.get("syncKwJob")
                .validator(new JobValidator())
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(syncStep)
                .build();
    }

    @JobScope
    @Bean("syncStep")
    public Step syncStep(ItemReader<ReplaceKw> trReplaceKwReader, ItemProcessor<ReplaceKw, ReplaceKw> trReplaceKwProcessor, ItemWriter<ReplaceKw> trReplaceKwWriter) {
        log.info(">>syncStep");

        return stepBuilderFactory.get("syncStep")
                .<ReplaceKw, ReplaceKw>chunk(chunkSize) // 제네릭 타입을 명확하게 명시
                .reader(trReplaceKwReader)
                .processor(trReplaceKwProcessor)
                .writer(trReplaceKwWriter)
                .listener(new CustomStepExecutionListener())
                .build();
    }


    @StepScope
    @Bean
    public ItemProcessor<ReplaceKw, ReplaceKw> trReplaceKwProcessor() {
        log.info("ash ------ processor");
        return new ItemProcessor<ReplaceKw, ReplaceKw>() {
            //Map<String,String> serverResult = serverCaller.SyncServer();
            Map<String, String> serverResult = testSetting();

            //jobExecutionContext.put("serverResult", serverResult);
            @Override
            public ReplaceKw process(ReplaceKw item) throws Exception {

                /**
                 * 서버 적재 내용과 db적재 내용 확인
                 */
                log.info("엥 = {}", item.getMainKw());
                return item;
//                if(item.getUseAt().equals(UseFilter.Y)) { //사용함 == 서버 등록됨
//                    if (serverResult.containsKey(item.getMainKw()) && !serverResult.get(item.getReplaceKw()).equals(item.getReplaceKw())) { //사용하는데 서버랑 다름
//                        log.info("db != server  {} DB: {}, SERVER: {}", item.getMainKw(), item.getReplaceKw(),serverResult.get(item.getMainKw()) );
//                        ReplaceKw updateKw = ReplaceKw.builder()
//                                .rkeywordSeq(item.getRkeywordSeq())
//                                .mainKw(item.getMainKw())
//                                .replaceKw(serverResult.get(item.getMainKw())) //replace  서버에 싱크를 맞춤
//                                .useAt(UseFilter.Y)
//                                .build();
//
//                        return updateKw;
//                    }
//                    else {
//                        if (serverResult.containsKey(item.getMainKw())) { //사용하고 서버에 등록도됨
//                            log.info("db == server {}", item.getMainKw());
//                            return null;
//                        }
//                        else { //DB에서는 사용하는데 서버에 등록 안됨
//                            log.info("db Y, server N {}", item.getMainKw());
//                            ReplaceKw updateKw = ReplaceKw.builder()
//                                    .rkeywordSeq(item.getRkeywordSeq())
//                                    .mainKw(item.getMainKw())
//                                    .replaceKw(item.getReplaceKw())
//                                    .useAt(UseFilter.N) //사용 안함으로 변경
//                                    .build();
//
//                            return updateKw;
//                        }
//                    }
//                }
//                else { //DB에서는 사용 안하는데 서버에 등록된경우
//                    if (serverResult.containsKey(item.getMainKw())) {
//                        log.info("db N, server Y {}/{}", item.getMainKw(), item.getReplaceKw());
//                        ReplaceKw updateKw = ReplaceKw.builder()
//                                .rkeywordSeq(item.getRkeywordSeq())
//                                .mainKw(item.getMainKw())
//                                .replaceKw(serverResult.get(item.getMainKw())) //서버 결과로 사용
//                                .useAt(UseFilter.Y) //사용 함으로 변경
//                                .build();
//
//                        return updateKw;
//                    }
//                    else { //DB엔 사용안함으로 있는데 서버에도 없는 경우
//                        return null;
//                    }
//                }
            }

        };
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<ReplaceKw> trReplaceKwWriter() {
        log.info("ash ------ writer: {} ", dataSource.toString());

        return new JdbcBatchItemWriterBuilder<ReplaceKw>()
                .dataSource(dataSource)
                .sql("UPDATE kosaf.replace_keyword_mast SET use_at = 'Y' where rkeyword_seq = :rkeywordSeq")
                .beanMapped()
                .build();
//        return new MyBatisBatchItemWriterBuilder<ReplaceKw>()
//                .sqlSessionFactory(sqlSessionFactory)
//                .statementId("com.kosaf.core.api.replaceKeyword.infrastructure.ReplaceKeywordMapper.updateBatch")
//                .assertUpdates(false)
//                .build();
    }

    @StepScope
    @Bean
    public MyBatisPagingItemReader<ReplaceKw> trReplaceKwReader() {
        log.info("ash ------ reader");

        return new MyBatisPagingItemReaderBuilder<ReplaceKw>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.kosaf.core.api.replaceKeyword.infrastructure.ReplaceKeywordMapper.findAllBatch")
                .pageSize(chunkSize)
                .build();
    }

    public Map<String, String> testSetting() {
        String input = "{App=앱, US $=유에스 $, GS SHOP=지에스 샵, H/W=하드웨어, CJONmart=씨제이온마트, 동의.=동이., 베스킨라빈스 31=베스킨라빈스 써리원, S/W=소프트웨어, 동의 =동이 , B&B=비엔비, US=유에스, Xi Jinping=시진핑, D&B=디엔비, USA=유에스에이, £=￡, USIM=유심, ¥=￥, KT&G=케이티엔지, M&A=엠엔에이, ₩=￦, A&P=에이엔피, 베스킨라빈스31=베스킨라빈스 써리원, R&B=알엔비, R&D=알엔디, iMBC=아이엠비씨, P&L=피엔엘, AT&T=에이티엔티}";

        // Remove the curly braces at the beginning and end
        input = input.substring(1, input.length() - 1);

        // Split the string by ', ' to get the key-value pairs
        String[] pairs = input.split(", (?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

        Map<String, String> resultServer = new HashMap<>();

        for (String pair : pairs) {
            // Split each pair by the first '=' to get the key and value
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            // Put the key and value into the map
            resultServer.put(key, value);
        }
        return resultServer;
    }
}
