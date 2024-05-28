package io.chan.service.bidirectional;

public record Rating(
        int count,
        double sum
) {
    public static Rating add(Rating r1, Rating r2) {
        int count = r1.count + r2.count;
        double sum = r1.sum + r2.sum;
        return new Rating(count, sum);
    }
}
