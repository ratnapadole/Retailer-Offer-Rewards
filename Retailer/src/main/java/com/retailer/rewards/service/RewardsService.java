package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerRewardResponseDTO;
import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.retailer.rewards.exception.CustomerNotFoundException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map;


/**
 * Service class to calculate and provide reward points for customers.
 */
@Service
public class RewardsService {

    @Autowired
    public CustomerRepository customerRepository;

    /**
     * Retrieves all customers and calculates their reward points
     * for transactions made within the last 3 months.
     */
    public List<CustomerRewardResponseDTO> getRewardsForAllCustomer() {
        try{
            List<Customer> customers = customerRepository.findAll();
            List<CustomerRewardResponseDTO> result = new ArrayList<>();
            LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

            for (Customer customer : customers) {
                Map<String, Integer> monthlyPoints = new HashMap<>();
                int totalPoints = 0;

                for (Transaction t : customer.getTransactions()) {
                    if (t.getDate() == null || t.getDate().isBefore(threeMonthsAgo)) {
                        continue;
                    }
                    int points = calculatePoints(t.getAmount());
                    String month = YearMonth.from(t.getDate()).toString();

                    monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
                    totalPoints += points;
                }

                CustomerRewardResponseDTO dto = new CustomerRewardResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        monthlyPoints,
                        totalPoints
                );
                result.add(dto);
            }
            return result;
        }catch (Exception e) {
            throw new RuntimeException("Failed to get rewards for customers", e);
        }
    }

    /**
     * Calculates reward points for a single transaction amount based on the rules:
     */
    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) (2 * (amount - 100) + 50);
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }

    /**
     * Retrieves reward points for a specific customer by ID.
     */
    public List<CustomerRewardResponseDTO> getCustomerRewardsById(Long id) {
        try{
            Optional<Customer> customerOptional = customerRepository.findById(id);
            if (!customerOptional.isPresent()) {
                throw new CustomerNotFoundException(id);
            }

            Customer customer = customerOptional.get();
            Map<String, Integer> monthlyPoints = new HashMap<>();
            int totalPoints = 0;

            LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

            for (Transaction t : customer.getTransactions()) {
                if (t.getDate() == null || t.getDate().isBefore(threeMonthsAgo)) {
                    continue;
                }

                int points = calculatePoints(t.getAmount());
                String month = YearMonth.from(t.getDate()).toString();

                monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
                totalPoints += points;
            }

            CustomerRewardResponseDTO dto = new CustomerRewardResponseDTO(
                    customer.getId(),
                    customer.getName(),
                    monthlyPoints,
                    totalPoints
            );

            List<CustomerRewardResponseDTO> result = new ArrayList<>();
            result.add(dto);
            return result;
        }catch (Exception e) {
            throw new RuntimeException("Failed to get rewards for customers", e);
        }
    }
}
