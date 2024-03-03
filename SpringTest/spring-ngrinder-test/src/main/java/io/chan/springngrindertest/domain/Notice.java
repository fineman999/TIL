package io.chan.springngrindertest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_notice_who", columnList = "who"),
        @Index(name = "idx_notice_createDate", columnList = "createDate")
})
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String who;

    @CreatedDate
    @Column(nullable = false)
    private ZonedDateTime createDate;

    @LastModifiedDate
    @Column(nullable = false)
    private ZonedDateTime updateDate;

}
