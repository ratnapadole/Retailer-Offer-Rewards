
package com.retailer.rewards.dto;
import java.util.Map;

public class CustomerRewardResponseDTO {
    private Long customerId;
    private String customerName;
    private Map<String, Integer> monthlyPoints;
    private int totalPoints;

    public CustomerRewardResponseDTO() {}
    public CustomerRewardResponseDTO(int points) {
        this.totalPoints = points;
    }
    public CustomerRewardResponseDTO(Long customerId, String customerName, Map<String, Integer> monthlyPoints, int totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Map<String, Integer> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }


}
