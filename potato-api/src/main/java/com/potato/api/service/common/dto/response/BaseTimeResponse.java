package com.potato.api.service.common.dto.response;

import com.potato.domain.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class BaseTimeResponse {

    protected LocalDateTime createdDateTime;

    protected LocalDateTime lastModifiedDateTime;

    protected void setBaseTime(BaseTimeEntity entity) {
        this.createdDateTime = entity.getCreatedDateTime();
        this.lastModifiedDateTime = entity.getLastModifiedDateTime();
    }

}
