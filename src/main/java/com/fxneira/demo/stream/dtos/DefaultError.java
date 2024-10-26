package com.fxneira.demo.stream.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultError {
    private String error;
    private String cause;
}
