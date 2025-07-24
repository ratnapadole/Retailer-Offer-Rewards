package com.retailer.rewards.controller;

import com.retailer.rewards.dto.CustomerRewardResponseDTO;
import com.retailer.rewards.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller to expose endpoints related to customer rewards calculation.
 *
 *  Provides API endpoints to:
 * - Calculate reward points for a given transaction amount.
 * - Retrieve reward points summary for all customers.
 * - Retrieve reward points summary for customerWise.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {
    @Autowired
    private RewardsService rewardsService;

    /**
     * Calculate reward points for a given transaction amount.
     * Example: For amount = 120, the points returned would be 90.
     */
    @GetMapping("/calculatePoints")
    public ResponseEntity<CustomerRewardResponseDTO> calculatePoints(@RequestParam int amount) {
        int points = rewardsService.calculatePoints(amount);
        CustomerRewardResponseDTO response = new CustomerRewardResponseDTO(points);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve the rewards summary for all customers.
     */
    @GetMapping("/customerRewards")
    public ResponseEntity<List<CustomerRewardResponseDTO>> getRewardsForAllCustomer() {
        List<CustomerRewardResponseDTO> rewards = rewardsService.getRewardsForAllCustomer();
        return ResponseEntity.ok(rewards);
    }

    /**
      * Retrieve the rewards summary for a specific customer by their ID.
      * Example URL: /api/rewards/getRewardsPersonWise/1
    */
    @GetMapping("/getRewardsPersonWise/{customerId}")
    public ResponseEntity<List<CustomerRewardResponseDTO>> getRewards(@PathVariable Long customerId) {
        List<CustomerRewardResponseDTO> rewards = rewardsService.getCustomerRewardsById(customerId);
        return ResponseEntity.ok(rewards);
    }
}