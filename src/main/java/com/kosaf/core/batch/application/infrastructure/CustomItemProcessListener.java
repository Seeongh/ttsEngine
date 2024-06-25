package com.kosaf.core.batch.application.infrastructure;

import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

@RequiredArgsConstructor
public class CustomItemProcessListener implements ItemProcessListener<ReplaceKw, ReplaceKw> {

    private final JdbcBatchItemWriter<ReplaceKw> trReplaceKwWriter;

    @Override
    public void beforeProcess(ReplaceKw item) {

    }

    @Override
    public void afterProcess(ReplaceKw item, ReplaceKw result) {

    }

    @Override
    public void onProcessError(ReplaceKw item, Exception e) {

    }
}
