package io.chan.springbatch.session10.classifier;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor2 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        System.out.println("Processing: " + item);
        return null;
    }
}
