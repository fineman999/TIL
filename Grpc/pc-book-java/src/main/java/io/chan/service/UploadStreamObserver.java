package io.chan.service;

import com.google.protobuf.ByteString;
import io.chan.ImageInfo;
import io.chan.UploadImageRequest;
import io.chan.UploadImageResponse;
import io.chan.service.clientstreaming.ImageStore;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class UploadStreamObserver implements StreamObserver<UploadImageRequest> {
    private static final Logger logger = Logger.getLogger(LaptopService.class.getName());
    private final ImageStore imageStore;
    private final StreamObserver<UploadImageResponse> responseObserver;
    private String laptopId;
    private String imageType;
    private ByteArrayOutputStream imageDataStream;

    public UploadStreamObserver(StreamObserver<UploadImageResponse> responseObserver, ImageStore imageStore) {
        this.responseObserver = responseObserver;
        this.imageStore = imageStore;
    }

    @Override
    public void onNext(UploadImageRequest request) {
        if (request.getDataCase() == UploadImageRequest.DataCase.INFO) {

            ImageInfo info = request.getInfo();
            logger.info("Receive an image: " + info);

            laptopId = info.getLaptopId();
            imageType = info.getImageType();
            imageDataStream = new ByteArrayOutputStream();
            return;
        }


        ByteString chunkData = request.getChunkData();
        logger.info("Receive a chunk with " + chunkData.size() + " bytes.");

        if (imageDataStream == null) {
            logger.severe("image info is missing");

            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("Image info is missing").asRuntimeException()
            );
            return;
        }

        try {
            chunkData.writeTo(imageDataStream);
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Failed to save image chunk").augmentDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void onError(Throwable throwable) {
        logger.warning(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        String imageId = "";
        int imageSize = imageDataStream.size();
        try {
            imageId = imageStore.save(laptopId, imageType, imageDataStream);
        } catch (IOException e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Failed to save image").augmentDescription(e.getMessage()).asRuntimeException()
            );
        }

        UploadImageResponse response = UploadImageResponse.newBuilder()
                .setId(imageId)
                .setSize(imageSize)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
