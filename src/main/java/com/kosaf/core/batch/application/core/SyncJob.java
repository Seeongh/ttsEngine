package com.kosaf.core.batch.application.core;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import com.kosaf.core.api.replaceKeyword.infrastructure.ReplaceKeywordMapper;
import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import com.kosaf.core.batch.application.infrastructure.CustomItemProcessListener;
import com.kosaf.core.batch.application.infrastructure.CustomItemSqlParameterSourceProvider;
import com.kosaf.core.batch.application.infrastructure.JobLoggerListener;
import com.kosaf.core.batch.application.infrastructure.JobValidator;
import com.kosaf.core.config.webClient.ServerCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
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
                .<ReplaceKw, ReplaceKw>chunk(5) // 제네릭 타입을 명확하게 명시
                .reader(trReplaceKwReader)
                .processor(trReplaceKwProcessor)
                .writer(trReplaceKwWriter)
                .listener(new CustomItemProcessListener((JdbcBatchItemWriter<ReplaceKw>) trReplaceKwWriter))
                .build();
    }


    @StepScope
    @Bean
    public ItemProcessor<ReplaceKw, ReplaceKw> trReplaceKwProcessor() {
        return new ItemProcessor<ReplaceKw, ReplaceKw>() {
            Map<String,String> serverResult = serverCaller.SyncServer();
            //jobExecutionContext.put("serverResult", serverResult);
            @Override
            public ReplaceKw process(ReplaceKw item) throws Exception {

                /**
                 * 서버 적재 내용과 db적재 내용 확인
                 */
                if(item.getUseAt().equals(UseFilter.Y)) { //사용함 == 서버 등록됨
                    if (serverResult.containsKey(item.getMainKw()) && !serverResult.get(item.getMainKw()).equals(item.getReplaceKw())) { //사용하는데 서버랑 다름
                        log.info("db != server  {} DB: {}, SERVER: {}", item.getMainKw(), item.getReplaceKw(),serverResult.get(item.getMainKw()) );
                        ReplaceKw updateKw = ReplaceKw.builder()
                                .rkeywordSeq(item.getRkeywordSeq())
                                .mainKw(item.getMainKw())
                                .replaceKw(serverResult.get(item.getMainKw())) //replace  서버에 싱크를 맞춤
                                .useAt(UseFilter.Y)
                                .build();

                        return updateKw;
                    }
                    else {
                        if (serverResult.containsKey(item.getMainKw())) { //사용하고 서버에 등록도됨
                            log.info("db == server {}", item.getMainKw());
                            return null;
                        }
                        else { //DB에서는 사용하는데 서버에 등록 안됨
                            log.info("db Y, server N {}", item.getMainKw());
                            ReplaceKw updateKw = ReplaceKw.builder()
                                    .rkeywordSeq(item.getRkeywordSeq())
                                    .mainKw(item.getMainKw())
                                    .replaceKw(item.getReplaceKw())
                                    .useAt(UseFilter.N) //사용 안함으로 변경
                                    .build();

                            return updateKw;
                        }
                    }
                }
                else { //DB에서는 사용 안하는데 서버에 등록된경우
                    if (serverResult.containsKey(item.getMainKw())) {
                        log.info("db N, server Y {}/{}", item.getMainKw(), item.getReplaceKw());
                        ReplaceKw updateKw = ReplaceKw.builder()
                                .rkeywordSeq(item.getRkeywordSeq())
                                .mainKw(item.getMainKw())
                                .replaceKw(serverResult.get(item.getMainKw())) //서버 결과로 사용
                                .useAt(UseFilter.Y) //사용 함으로 변경
                                .build();

                        return updateKw;
                    }
                    else { //DB엔 사용안함으로 있는데 서버에도 없는 경우
                        return null;
                    }
                }
            }

        };
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<ReplaceKw> trReplaceKwWriter() {
        return new JdbcBatchItemWriterBuilder<ReplaceKw>()
                .itemSqlParameterSourceProvider(new CustomItemSqlParameterSourceProvider<>())
                .sql("UPDATE kosaf.replace_keyword_mast SET replace_kw = :replaceKw, use_at = :useAt, updt_dt= now() WHERE rkeyword_seq = :rkeywordSeq")
                .dataSource(dataSource)
                .build();
    }
    @StepScope
    @Bean
    public MyBatisPagingItemReader<ReplaceKw> trReplaceKwReader() {
        return new MyBatisPagingItemReaderBuilder<ReplaceKw>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.kosaf.core.api.replaceKeyword.infrastructure.ReplaceKeywordMapper.findAll")
                .pageSize(10)
                .parameterValues(Collections.emptyMap())
                .build();
    }

}
