package com.wesley.backend.dto;

import java.util.Collections;
import java.util.List;

public record ResponseDto<T>(Boolean ok, String error, List<T> data) {


    public static <T> ResponseDto<T> success(List<T> data) {
        return new ResponseDto<>(true, null, data);
    }

    public static <T> ResponseDto<T> success(T item) {
        return new ResponseDto<>(true, null, Collections.singletonList(item));
    }

    public static <T> ResponseDto<T> error(String errorMessage) {
        return new ResponseDto<>(false, errorMessage, null);
    }
}
