package com.retailer.rewards.exception;
/**
* Custom exception thrown when a customer with the specified ID is not found in the database.
*/
 public class CustomerNotFoundException extends RuntimeException {
    /**
    * message if missing customer ID.
    */
    public CustomerNotFoundException(Long id) {
        super("Customer with ID " + id + " not found.");
    }
}