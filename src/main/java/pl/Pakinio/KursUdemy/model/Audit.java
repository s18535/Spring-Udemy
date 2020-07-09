package pl.Pakinio.KursUdemy.model;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
 class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePersite(){
        createdOn=LocalDateTime.now();
    }
    @PreUpdate
    void preMerge(){
        updatedOn=LocalDateTime.now();
    }
}
