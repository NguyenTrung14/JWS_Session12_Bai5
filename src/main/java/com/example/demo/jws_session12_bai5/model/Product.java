package com.example.demo.jws_session12_bai5.model;

public record Product(
        Long id,
        String productCode,
        String productName,
        String description,
        Double price,
        Integer quantity
) {
}
