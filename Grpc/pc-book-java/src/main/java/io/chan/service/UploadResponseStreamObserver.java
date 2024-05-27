package io.chan.service;

import io.chan.UploadImageResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class UploadResponseStreamObserver implements StreamObserver<UploadImageResponse> {
  private static final Logger logger = Logger.getLogger(LaptopService.class.getName());
  private final CountDownLatch latch;

  public UploadResponseStreamObserver(final CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onNext(final UploadImageResponse response) {
    logger.info("receive response: " + response);
  }

  @Override
  public void onError(final Throwable throwable) {
    logger.severe("upload error: " + throwable.getMessage());
    latch.countDown();
  }

  @Override
  public void onCompleted() {
    logger.info("image upload completed");
    latch.countDown();
  }
}
