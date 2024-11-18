package com.anuradha.centralservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
        String id,
        String title,
        String description,
        String address,
        String rooms,
        String beds,
        String baths,
        String size,
        String price,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd' 'HH:mm:ss"
        )
        LocalDateTime createdAt,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd' 'HH:mm:ss"
        )
        LocalDateTime updatedAt,
        String thumbnail,
        List<String> images
) {

}

