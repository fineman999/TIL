package io.chan.service.bidirectional;

import io.chan.RateLaptopResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class RateLaptopResponseStreamObserver implements StreamObserver<RateLaptopResponse> {
    private final static Logger logger = Logger.getLogger(RateLaptopResponseStreamObserver.class.getName());
    private final CountDownLatch finishLatch;


    public RateLaptopResponseStreamObserver(CountDownLatch finishLatch, String[] laptopIds, double[] scores) {
        this.finishLatch = finishLatch;
    }

    @Override
    public void onNext(RateLaptopResponse rateLaptopResponse) {
        logger.info("Received rate laptop response for laptop ID: " + rateLaptopResponse.getLaptopId() + " with average score: " + rateLaptopResponse.getAverageScore());
    }

    @Override
    public void onError(Throwable throwable) {
        logger.severe("Rate laptop response failed: " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        logger.info("Rate laptop response completed");
        finishLatch.countDown();
    }
}
