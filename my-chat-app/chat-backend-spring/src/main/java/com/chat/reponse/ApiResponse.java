package com.chat.reponse;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private T data;           // Generic type to hold any data (could be a list or single object)
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(String message) {
        this.message = message;
        this.data = null;
    }

    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
