package io.chan.service.bidirectional;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRatingStore implements RatingStore{
    private final ConcurrentHashMap<String, Rating> data;

    public InMemoryRatingStore() {
        this.data = new ConcurrentHashMap<>();
    }

    /**
     * merge 메소드는 data에 laptopId가 존재하면 Rating::add를 호출하고, 존재하지 않으면 새로운 Rating(1, score)를 넣는다.
     */
    @Override
    public Rating add(String laptopId, double score) {
        return data.merge(laptopId, new Rating(1, score), Rating::add);
    }
}
