package com.kosaf.core.batch.application.infrastructure;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class JobValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String filename = parameters.getString("fileName");

        // 예외 발생 시 throw
    }
}
