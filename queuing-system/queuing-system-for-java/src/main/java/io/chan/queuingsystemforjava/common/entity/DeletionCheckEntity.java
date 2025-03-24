package io.chan.queuingsystemforjava.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public abstract class DeletionCheckEntity {

    @Column
    private ZonedDateTime deletedAt;
}
