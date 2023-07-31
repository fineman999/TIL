package org.hello.item08.autocloseable;

import java.io.BufferedInputStream;

public class AutoClosableIsGood implements AutoCloseable {
    private BufferedInputStream inputStream;
    @Override
    public void close() throws Exception {
        try {
            inputStream.close();
        } catch (Exception e) {
            System.out.println("failed to close() " + inputStream);
        }
    }
}
