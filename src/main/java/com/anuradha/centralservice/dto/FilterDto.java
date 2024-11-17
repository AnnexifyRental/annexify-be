package com.anuradha.centralservice.dto;

import com.anuradha.centralservice.enums.CommonStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FilterDto {
    private int page;
    private int size;
    private List<CommonStatus> statuses;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
