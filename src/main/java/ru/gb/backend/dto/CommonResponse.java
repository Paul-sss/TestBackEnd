package ru.gb.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonResponse<AnyData> {
    @JsonProperty("data")
    public AnyData data;
    @JsonProperty("success")
    public Boolean success;
    @JsonProperty("status")
    public Integer status;
}
