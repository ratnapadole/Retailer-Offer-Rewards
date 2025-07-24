package com.retailer.rewards;

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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Integration tests for the Rewards API endpoints using a real Spring Boot context.

 * Endpoints tested:
 * - GET /api/rewards/customerRewards
 * - GET /api/rewards/getRewardsPersonWise/{customerId}
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardsIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private int port;

    private String baseUrl;
    /**
     * Initializes test data before each test.
     */
    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/rewards";

        // Clear previous data
        customerRepository.deleteAll();

        // Create and save test customer
        Customer customer = new Customer();
        customer.setName("Alice");

        Transaction t1 = new Transaction(120.0, LocalDateTime.now().minusDays(5));
        t1.setCustomer(customer);
        Transaction t2 = new Transaction(80.0, LocalDateTime.now().minusDays(10));
        t2.setCustomer(customer);

        customer.setTransactions(List.of(t1, t2));
        customerRepository.save(customer);
    }

    /**
     * Verifies the integration of the /api/rewards/customerRewards endpoint.
     */
    @Test
    void testCustomerRewardsIntegration() {
        // Call: GET /api/rewards/customerRewards
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "/customerRewards", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Map<String, Object>> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());

        Map<String, Object> customer = body.get(0);
        assertEquals("Alice", customer.get("customerName"));
        assertEquals(100, customer.get("totalPoints"));  // 90 + 10
    }

    /**
     * Verifies that /api/rewards/getRewardsPersonWise/{customerId} returns
     */
    @Test
    void testGetRewardsPersonWise_Success() {
        Long customerId = customerRepository.findAll().get(0).getId();
        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/getRewardsPersonWise/" + customerId,
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Verifies that /api/rewards/getRewardsPersonWise/{customerId} returns
     */
    @Test
    void testGetRewardsPersonWise_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/getRewardsPersonWise/9999",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Customer with ID 9999 not found"));
    }
}
