package com.retailer.rewards.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a Transaction in the retailer system.
 *
 * Each Transaction has a unique ID, an amount, a transaction date-time,
 * and is associated with a Customer.
 */
@Entity
public class Transaction {

    /**
     * Primary key for Transaction entity.
     * Auto-generated by the persistence provider.
     */
    @Id
    @GeneratedValue
    private Long id;
    private Double amount;
    private LocalDateTime date;

    /**
     * Many-to-one relationship to the Customer entity.
     * Each transaction belongs to one customer.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Transaction() {}

    public Transaction(Double amount, LocalDateTime date) {
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
