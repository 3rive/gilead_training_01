package com.gilead.orders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAnOrderWhenStockIsAvailable() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sku":"BOOK-1","quantity":2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(startsWith("ord-")))
                .andExpect(jsonPath("$.sku").value("BOOK-1"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void missingOrderBecomesNotFound() throws Exception {
        mockMvc.perform(get("/api/orders/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.details.orderId").value("missing"));
    }

    @Test
    void tooLargeQuantityBecomesConflict() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sku":"PEN-2","quantity":4}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_STOCK"))
                .andExpect(jsonPath("$.details.sku").value("PEN-2"))
                .andExpect(jsonPath("$.details.requested").value(4))
                .andExpect(jsonPath("$.details.available").value(1));
    }

    @Test
    void nonPositiveQuantityBecomesBadRequest() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sku":"BOOK-1","quantity":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_ORDER"))
                .andExpect(jsonPath("$.details.field").value("quantity"));
    }
}
