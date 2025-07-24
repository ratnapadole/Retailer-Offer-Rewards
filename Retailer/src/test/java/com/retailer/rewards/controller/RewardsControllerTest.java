package com.retailer.rewards.controller;

import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();

        testCustomer = new Customer();
        testCustomer.setName("Ratna");

        Transaction t1 = new Transaction(120.0, LocalDateTime.now().minusDays(5)); // 90 points
        t1.setCustomer(testCustomer);

        Transaction t2 = new Transaction(80.0, LocalDateTime.now().minusDays(15)); // 30 points
        t2.setCustomer(testCustomer);

        testCustomer.setTransactions(List.of(t1, t2));
        customerRepository.save(testCustomer);
    }

    @Test
    void testCalculatePointsEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/rewards/calculatePoints?amount=120", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(90, response.getBody().get("totalPoints"));
    }

    @Test
    void testGetAllCustomerRewards() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/rewards/customerRewards", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        Map<String, Object> customer = (Map<String, Object>) response.getBody().get(0);
        assertEquals("Ratna", customer.get("customerName"));
        assertEquals(120, customer.get("totalPoints")); // 90 + 30
    }

    @Test
    void testGetCustomerRewardById() {
        Long customerId = testCustomer.getId();
        ResponseEntity<List> response = restTemplate.getForEntity("/api/rewards/getRewardsPersonWise/" + customerId, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        Map<String, Object> rewardData = (Map<String, Object>) response.getBody().get(0);
        assertEquals("Ratna", rewardData.get("customerName"));
        assertEquals(120, rewardData.get("totalPoints"));
    }

    @Test
    void testGetRewardsForInvalidCustomer() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/rewards/getRewardsPersonWise/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Customer with ID 999 not found"));
    }
}
