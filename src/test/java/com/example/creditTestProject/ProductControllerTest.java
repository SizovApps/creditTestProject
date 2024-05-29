package com.example.creditTestProject;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateProducts() throws Exception {
        int numberOfProducts = 100000;
        for (int i = 0; i < numberOfProducts; i++) {
            MockHttpServletResponse response = mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Name\"}")).andReturn().getResponse();
            assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        }
    }

    @Test
    public void testFetchProducts() throws Exception {

        for (int i = 0; i < 100000; i++) {
            mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Name\"}"));
        }


        int numberOfFetches = 100000;
        int numberOfThreads = 100;

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreads);
        List<Long> executionTimes = new CopyOnWriteArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfFetches; i++) {
            executor.submit(() -> {
                long threadStartTime = System.currentTimeMillis();
                int randomId = (int) (Math.random() * 100000) + 1;
                try {
                    MockHttpServletResponse response = mockMvc.perform(get("/api/v1/products/" + randomId)).andReturn().getResponse();
                    assertEquals(HttpStatus.OK.value(), response.getStatus());
                } catch (Exception ignored) {}
                executionTimes.add(System.currentTimeMillis() - threadStartTime);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        Collections.sort(executionTimes);

        long median = executionTimes.get(executionTimes.size() / 2);

        int index_percentile95 = (int) Math.ceil(executionTimes.size() * 0.95);
        long percentile95 = executionTimes.get(index_percentile95 - 1);

        int index_percentile99 = (int) Math.ceil(executionTimes.size() * 0.99);
        long percentile99 = executionTimes.get(index_percentile99 - 1);

        System.out.println("Общее время: " + (endTime - startTime) + "ms");
        System.out.println("Медианное время: " + median + "ms");
        System.out.println("Перцинтиль 95: " + percentile95 + "ms");
        System.out.println("Перцинтиль 99: " + percentile99 + "ms");
    }

}
