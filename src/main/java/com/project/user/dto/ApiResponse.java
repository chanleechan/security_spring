package com.project.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ApiResponse<T> {
    public String msg;
    public T object;

    @Builder
    public ApiResponse(String msg, T object) {
        this.msg = msg;
        this.object = object;
    }

    public static <T> ApiResponse<T> create(String msg, T object) {
        return ApiResponse.<T>builder().msg(msg).object(object).build();
    }

}
