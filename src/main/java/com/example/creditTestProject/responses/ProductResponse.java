package com.example.creditTestProject.responses;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private final int status;
    private Object data;
    private String detail;
}
