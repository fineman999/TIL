package io.chan.service.bidirectional;

import io.chan.RateLaptopRequest;
import io.chan.RateLaptopResponse;
import io.chan.service.LaptopStore;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class RateLaptopRequestStreamObserver implements StreamObserver<RateLaptopRequest> {
    private final static Logger logger = Logger.getLogger(RateLaptopRequestStreamObserver.class.getName());
    private final RatingStore ratingStore;
    private final LaptopStore laptopStore;
    private final StreamObserver<RateLaptopResponse> responseObserver;

    public RateLaptopRequestStreamObserver(StreamObserver<RateLaptopResponse> responseObserver, RatingStore ratingStore, LaptopStore laptopStore) {
        this.ratingStore = ratingStore;
        this.responseObserver = responseObserver;
        this.laptopStore = laptopStore;
    }

    @Override
    public void onNext(RateLaptopRequest rateLaptopRequest) {
        String laptopId = rateLaptopRequest.getLaptopId();
        double score = rateLaptopRequest.getScore();

        logger.info("Received rate laptop request for laptop ID: " + laptopId + " with score: " + score);

        if (laptopStore.find(laptopId).isEmpty()) {
            logger.warning("Laptop ID not found: " + laptopId);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Laptop ID not found: " + laptopId).asRuntimeException());
            return;
        }

        Rating rating = ratingStore.add(laptopId, score);
        RateLaptopResponse response = RateLaptopResponse.newBuilder()
                .setLaptopId(laptopId)
                .setRatedCount(rating.count())
                .setAverageScore(rating.sum() / rating.count())
                .build();

        responseObserver.onNext(response);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.warning("Rate laptop request failed: " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }
}
