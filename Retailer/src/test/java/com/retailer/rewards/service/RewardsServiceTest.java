package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerRewardResponseDTO;
import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.Transaction;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for the {@link RewardsService} class.
 */

public class RewardsServiceTest {

    private CustomerRepository customerRepository;
    private RewardsService rewardsService;
    /**
     * Sets up a new instance of RewardsService with a mocked CustomerRepository
     * before each test case.
     */
    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        rewardsService = new RewardsService();
        rewardsService.customerRepository = customerRepository;
    }
    /**
     * Verifies the reward point calculation logic for different amounts:
     */
    @Test
    void testCalculatePoints() throws Exception {
        assertEquals(90, rewardsService.calculatePoints(120)); // 2*20 + 1*50
        assertEquals(25, rewardsService.calculatePoints(75));  // 1*25
        assertEquals(0, rewardsService.calculatePoints(40));   // no rewards
    }
    /**
     * Tests reward point calculation for all customers.
     */
    @Test
    void testGetRewardsForAllCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setTransactions(List.of(
                createTransaction(120.0, LocalDateTime.now().minusDays(10)), // 90
                createTransaction(70.0, LocalDateTime.now().minusDays(5))    // 20
        ));

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerRewardResponseDTO> result = rewardsService.getRewardsForAllCustomer();

        assertEquals(1, result.size());
        CustomerRewardResponseDTO dto = result.get(0);
        assertEquals(1L, dto.getCustomerId());
        assertEquals("John", dto.getCustomerName());
        assertEquals(110, dto.getTotalPoints());
        assertFalse(dto.getMonthlyPoints().isEmpty());
    }

    @Test
    void testOldTransactionsAreIgnored() throws Exception {
        Customer customer = new Customer();
        customer.setId(2L);
        customer.setName("Jane");
        customer.setTransactions(List.of(
                createTransaction(120.0, LocalDateTime.now().minusMonths(4)), // ignored
                createTransaction(70.0, LocalDateTime.now().minusDays(8))     // 20
        ));

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerRewardResponseDTO> result = rewardsService.getRewardsForAllCustomer();

        assertEquals(1, result.size());
        CustomerRewardResponseDTO dto = result.get(0);
        assertEquals("Jane", dto.getCustomerName());
        assertEquals(20, dto.getTotalPoints());
    }
    /**
     * Verifies that reward points are calculated correctly for a specific customer ID.
     */
    @Test
    void testGetCustomerRewardsById_Success() throws Exception {
        Long customerId = 3L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Smith");
        customer.setTransactions(List.of(
                createTransaction(120.0, LocalDateTime.now().minusDays(2)) // 90
        ));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        List<CustomerRewardResponseDTO> result = rewardsService.getCustomerRewardsById(customerId);

        assertEquals(1, result.size());
        CustomerRewardResponseDTO dto = result.get(0);
        assertEquals("Smith", dto.getCustomerName());
        assertEquals(90, dto.getTotalPoints());
    }
    /**
     * Verifies that when a customer ID is not found, the service throws
     */
    @Test
    void testGetCustomerRewardsById_CustomerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> rewardsService.getCustomerRewardsById(99L)
        );

        assertEquals("Customer with ID 99 not found.", exception.getMessage());
    }

    private Transaction createTransaction(double amount, LocalDateTime date) {
        Transaction t = new Transaction();
        t.setAmount(amount);
        t.setDate(date);
        return t;
    }
}
