package com.kosaf.core.batch.application.infrastructure;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

@Slf4j
@RequiredArgsConstructor
public class CustomItemProcessListener implements ItemProcessListener<ReplaceKw, ReplaceKw> {

    private final JdbcBatchItemWriter<ReplaceKw> trReplaceKwWriter;

    @Override
    public void beforeProcess(ReplaceKw item) {
        log.info("ash process 시작");
    }

    @Override
    public void afterProcess(ReplaceKw item, ReplaceKw result) {
        log.info("ash process 종료");
    }

    @Override
    public void onProcessError(ReplaceKw item, Exception e) {

    }
}
