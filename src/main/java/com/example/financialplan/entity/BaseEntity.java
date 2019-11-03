package com.example.financialplan.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
public abstract class BaseEntity {

    @Getter
    @Setter
    private LocalDateTime creationDateTime;

    @Getter
    @Setter
    private LocalDateTime modificationDateTime;

    @PrePersist
    public void updateCreationDateTime() {
        creationDateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void updateModificationDateTime() {
        modificationDateTime = LocalDateTime.now();
    }

    public String getFormattedTimestamp() {
        return creationDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
