package com.kosaf.core.batch.application.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.result.Outputs;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
public class CustomStepExecutionListener implements ItemWriteListener<T> {

    @Override
    public void beforeWrite(List<? extends T> items) {
        log.info("ash beforeWrite");
//        boolean allItemsNull = items.stream().allMatch(item -> item == null);
//        if (allItemsNull) {
//            StepContext stepContext = StepSynchronizationManager.getContext();
//            if (stepContext != null) {
//                ExecutionContext executionContext = stepContext.getStepExecution().getExecutionContext();
//                Chunk<?> chunk = (Chunk<?>) executionContext.get("chunk");
//
//                if (chunk != null && chunk.isEmpty()) {
//                    log.info("ash setEnd");
//                    chunk.setEnd();
//                }
//            }
//        }
    }



    @Override
    public void afterWrite(List<? extends T> items) {
        log.info("ash afterWrite");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends T> items) {

    }
}
