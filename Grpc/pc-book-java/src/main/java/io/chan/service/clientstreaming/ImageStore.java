package io.chan.service.clientstreaming;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ImageStore {
    String save(String laptopId, String imageType, ByteArrayOutputStream imageDataStream) throws IOException;
}
