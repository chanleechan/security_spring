package com.project.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class ApiResponse {
    public String msg;
    public String jwt;

    @Builder
    public ApiResponse(String msg, String jwt) {
        this.msg = msg;
        this.jwt = jwt;
    }

    public static ApiResponse create(String msg, String jwt) {
        return ApiResponse.builder().msg(msg).jwt(jwt).build();
    }

}
