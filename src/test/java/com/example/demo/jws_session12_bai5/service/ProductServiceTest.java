package com.example.demo.jws_session12_bai5.service;

import com.example.demo.jws_session12_bai5.exception.ProductNotFoundException;
import com.example.demo.jws_session12_bai5.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest {
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void createAssignsIncrementingIds() {
        Product firstProduct = productService.create(product(null, "P001", "Laptop", "Office laptop", 20000000, 10));
        Product secondProduct = productService.create(product(null, "P002", "Mouse", "Wireless mouse", 500000, 20));

        assertThat(firstProduct.id()).isEqualTo(1);
        assertThat(secondProduct.id()).isEqualTo(2);
        assertThat(productService.findAll()).containsExactly(firstProduct, secondProduct);
    }

    @Test
    void findByIdReturnsExistingProduct() {
        Product createdProduct = productService.create(product(null, "P003", "Keyboard", "Mechanical keyboard", 1200000, 15));

        assertThat(productService.findById(createdProduct.id())).isEqualTo(createdProduct);
    }

    @Test
    void findByIdThrowsWhenProductDoesNotExist() {
        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: 99");
    }

    @Test
    void updateReplacesProductAndKeepsPathId() {
        productService.create(product(null, "P004", "Monitor", "24 inch monitor", 3500000, 3));

        Product updatedProduct = productService.update(1L, product(88L, "P004", "Monitor Pro", "27 inch monitor", 4900000, 2));

        assertThat(updatedProduct.id()).isEqualTo(1);
        assertThat(updatedProduct.productName()).isEqualTo("Monitor Pro");
        assertThat(productService.findById(1L)).isEqualTo(updatedProduct);
    }

    @Test
    void updateThrowsWhenProductDoesNotExist() {
        assertThatThrownBy(() -> productService.update(99L, product(null, "P999", "Missing", "Missing", 1, 1)))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: 99");
    }

    @Test
    void deleteRemovesExistingProduct() {
        productService.create(product(null, "P005", "Speaker", "Bluetooth speaker", 1500000, 6));

        productService.delete(1L);

        assertThat(productService.findAll()).isEmpty();
        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: 1");
    }

    @Test
    void deleteThrowsWhenProductDoesNotExist() {
        assertThatThrownBy(() -> productService.delete(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: 99");
    }

    private static Product product(Long id, String productCode, String productName, String description, double price, int quantity) {
        return new Product(id, productCode, productName, description, price, quantity);
    }
}
