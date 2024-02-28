package io.chan.springbatch.session07.itemstream;

import org.springframework.batch.item.*;

public class CustomItemStreamWriter implements ItemStreamWriter<String> {
    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        for (String item : chunk.getItems()) {
            System.out.println(">> " + item);
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("CustomItemStreamWriter.open");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("CustomItemStreamWriter.update");
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("CustomItemStreamWriter.close");
    }
}
