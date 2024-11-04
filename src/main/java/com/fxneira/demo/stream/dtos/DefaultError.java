package com.fxneira.demo.stream.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class DefaultError extends Dto {
    private String error;
    private String cause;
}
