package io.chan.service.clientstreaming;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DiskImageStore implements ImageStore {
    private final String folder;
    private final ConcurrentHashMap<String, ImageMetaData> data;

    public DiskImageStore(String folder) {
        this.folder = folder;
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public String save(String laptopId, String imageType, ByteArrayOutputStream imageDataStream) throws IOException {
        String imageId = UUID.randomUUID().toString();
        String imagePath = String.format("./%s/%s%s", folder, imageId, imageType);

        File directory = new File(folder);
        if (!directory.exists()) {
            directory.mkdir();
        }
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(imagePath)
        ) {
            imageDataStream.writeTo(fileOutputStream);
            ImageMetaData imageMetaData = new ImageMetaData(laptopId, imageType, imagePath);
            data.put(imageId, imageMetaData);
            return imageId;
        }
    }
}
