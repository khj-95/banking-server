package com.numble.bankingserver.common.util;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 생성시간 자동으로 넣어줄수 있는 entity 필요한 entity에 extends BaseTime 하면 자동으로 넣어짐.
 */
@Getter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseTime {

    @CreatedDate
    private LocalDateTime createdDateTime;
}
