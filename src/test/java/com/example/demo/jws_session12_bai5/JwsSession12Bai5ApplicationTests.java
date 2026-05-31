package com.example.demo.jws_session12_bai5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JwsSession12Bai5ApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void getAllProductsReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createProductReturnsCreatedProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("P001", "Laptop Dell XPS 13", "Ultrabook", 32990000, 10)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productCode").value("P001"))
                .andExpect(jsonPath("$.productName").value("Laptop Dell XPS 13"))
                .andExpect(jsonPath("$.description").value("Ultrabook"))
                .andExpect(jsonPath("$.price").value(32990000))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void getAllProductsReturnsCreatedProductsInIdOrder() throws Exception {
        createProduct("P002", "Keyboard", "Mechanical keyboard", 1200000, 15);
        createProduct("P003", "Mouse", "Wireless mouse", 650000, 30);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productName").value("Keyboard"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].productName").value("Mouse"));
    }

    @Test
    void getProductByIdReturnsCreatedProduct() throws Exception {
        createProduct("P004", "Monitor", "27 inch monitor", 4990000, 5);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productCode").value("P004"))
                .andExpect(jsonPath("$.productName").value("Monitor"))
                .andExpect(jsonPath("$.description").value("27 inch monitor"))
                .andExpect(jsonPath("$.price").value(4990000))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void updateProductReturnsUpdatedProduct() throws Exception {
        createProduct("P005", "Tablet", "Android tablet", 7990000, 8);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("P005", "Tablet Pro", "Updated tablet", 9990000, 4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productCode").value("P005"))
                .andExpect(jsonPath("$.productName").value("Tablet Pro"))
                .andExpect(jsonPath("$.description").value("Updated tablet"))
                .andExpect(jsonPath("$.price").value(9990000))
                .andExpect(jsonPath("$.quantity").value(4));
    }

    @Test
    void deleteProductReturnsNoContentAndProductBecomesMissing() throws Exception {
        createProduct("P006", "Headphone", "Noise cancelling", 2990000, 6);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found with id: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getMissingProductReturnsJson404AndLogsWarn(CapturedOutput output) throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));

        assertThat(output)
                .contains("Controller request: method=getProductById, args=[999]")
                .contains("WARN")
                .contains("Service exception in findById: Product not found with id: 999")
                .contains("Controller method getProductById executed in")
                .contains("Product not found with id: 999");
    }

    @Test
    void updateMissingProductReturnsJson404() throws Exception {
        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("P999", "Missing", "Missing update", 100000, 1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteMissingProductReturnsJson404() throws Exception {
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }

    private void createProduct(String productCode, String productName, String description, double price, int quantity) throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson(productCode, productName, description, price, quantity)))
                .andExpect(status().isCreated());
    }

    private static String productJson(String productCode, String productName, String description, double price, int quantity) {
        return String.format(Locale.US, """
                {
                  "productCode": "%s",
                  "productName": "%s",
                  "description": "%s",
                  "price": %.2f,
                  "quantity": %d
                }
                """, productCode, productName, description, price, quantity);
    }
}
