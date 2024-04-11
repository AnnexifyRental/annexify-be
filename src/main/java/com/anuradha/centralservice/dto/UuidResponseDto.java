package com.anuradha.centralservice.dto;

public class UuidResponseDto {

    private String uuid;

    public UuidResponseDto() {
    }

    public UuidResponseDto(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
