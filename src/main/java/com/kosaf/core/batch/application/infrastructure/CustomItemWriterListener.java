package com.kosaf.core.batch.application.infrastructure;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class CustomItemWriterListener  extends StepExecutionListenerSupport {
    public CustomItemWriterListener() {
        super();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        return super.afterStep(stepExecution);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution);
    }
}
