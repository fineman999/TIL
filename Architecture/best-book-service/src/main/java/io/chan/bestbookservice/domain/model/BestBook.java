package io.chan.bestbookservice.domain.model;

import io.chan.bestbookservice.domain.vo.Item;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * 비지니스 로직 구현 베스트도서등록
 * UUID로 키생성
 * 전달된 품목으로 아이템 설정, 대여 횟수 1로 설정 도서대여횟수 증가
 * 현재도서횟수에 1회 증가
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BestBook {
    private String id;
    private Item item;
    private long rentCount;

    public static BestBook register(Item item, UUID uuid) {
        return new BestBook(uuid.toString(), item, 1);
    }

    public Long increaseRentCount() {
        rentCount++;
        return rentCount;
    }
}
