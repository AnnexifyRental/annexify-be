package com.anuradha.centralservice.dto;

import java.util.List;

public record PostImageSaveDto(
        String id,
        String thumbnail,
        List<String> images
) {
}
