package io.chan.springngrindertest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Getter
@Entity
@Table
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String title;

    @Column(length = 100)
    private String content;

    @Column(length = 30)
    private String who;

    @CreatedDate
    @Column(nullable = false)
    private Timestamp createDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Timestamp updateDate;

}
