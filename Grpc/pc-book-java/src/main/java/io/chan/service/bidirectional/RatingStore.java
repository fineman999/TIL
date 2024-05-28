package io.chan.service.bidirectional;

public interface RatingStore {
    Rating add(String laptopId, double score);
}
