package com.example.demo.jws_session12_bai5.service;

import com.example.demo.jws_session12_bai5.exception.ProductNotFoundException;
import com.example.demo.jws_session12_bai5.model.Product;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Product> findAll() {
        return products.values()
                .stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();
    }

    public Product findById(Long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public Product create(Product product) {
        Long id = nextId.getAndIncrement();
        Product createdProduct = new Product(
                id,
                product.productCode(),
                product.productName(),
                product.description(),
                product.price(),
                product.quantity()
        );
        products.put(id, createdProduct);
        return createdProduct;
    }

    public Product update(Long id, Product product) {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }

        Product updatedProduct = new Product(
                id,
                product.productCode(),
                product.productName(),
                product.description(),
                product.price(),
                product.quantity()
        );
        products.put(id, updatedProduct);
        return updatedProduct;
    }

    public void delete(Long id) {
        Product removedProduct = products.remove(id);
        if (removedProduct == null) {
            throw new ProductNotFoundException(id);
        }
    }
}
