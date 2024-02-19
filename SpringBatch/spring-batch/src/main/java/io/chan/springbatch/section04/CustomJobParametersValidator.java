package io.chan.springbatch.section04;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class CustomJobParametersValidator implements JobParametersValidator {
    @Override
    public void validate(@Nullable JobParameters parameters) throws JobParametersInvalidException {
        if (Objects.requireNonNull(parameters).getString("name") == null) {
            throw new JobParametersInvalidException("name parameter is missing");
        }
    }
}
