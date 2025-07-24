
#  Retailer Offer-Rewards - Reward Points System

Retailer Offer-Rewards is a Spring Boot-based program that awards customers with points based on their transaction amounts. The system calculates **monthly** and **total reward points** for each customer and exposes them via **REST APIs**.

## Features

- Reward point calculation based on transaction history
- Monthly and total reward breakdown
- RESTful API structure
- Exception handling (e.g., CustomerNotFoundException)
- Global error handler using @ControllerAdvice
- DTO layer for clean API responses
- Unit and integration tests using JUnit 5
- JavaDocs included for all core classes

##  Reward Points Calculation Logic

| Rule                                      | Points |
|-------------------------------------------|--------|
| For every $1 over **$100**                | 2 pts  |
| For every $1 between **$50 and $100**     | 1 pt   |
| Below $50                                 | 0 pts  |

### Example:
Transaction Amount: **$120**  
→ (2 × 20) + (1 × 50) = **90 points**

## Project Structure
com.retailer.rewards

├── 	controller # REST API endpoints

├── 	service # Business logic (reward calculation)

├── 	dto # DTOs for API responses

├── 	model / entity # JPA entity classes (Customer, Transaction)

├── 	repository # Spring Data JPA repositories

├── 	exception # Global and custom exception handling

└── 	RetailerApplication.java # Main Spring Boot class


## API Endpoints

| Method | Endpoint                                      | Description                              |
|--------|-----------------------------------------------|------------------------------------------|
| GET    |  /api/rewards/customerRewards                 | Get rewards summary for all customers    |
| GET    |  /api/rewards/getRewardsPersonWise/{id}       | Get rewards for a specific customer      |
| GET    |  /api/rewards/calculatePoints?amount={value}  | Calculate points for a given amount      |


##  Response: /api/rewards/customerRewards

[

  {
	
    "customerId": 1,
		
    "customerName": "Ratna",
		
    "totalPoints": 115,
		
    "monthlyPoints": {
		
      "2025-07": 115
			
    }

  },
	
  {
	
    "customerId": 2,
		
    "customerName": "Anita",
		
    "totalPoints": 155,
		
    "monthlyPoints": {
		
      "2025-06": 155
			
    }
		
  },
	
  {
	
    "customerId": 3,
		
    "customerName": "Pooja",
		
    "totalPoints": 0,
		
    "monthlyPoints": {
		
      "2025-05": 0
			
    }
		
  }


 
 ##  SQL


INSERT INTO customer (id, name) VALUES (1, 'Ratna');

INSERT INTO customer (id, name) VALUES (2, 'Anita');

INSERT INTO customer (id, name) VALUES (3, 'Pooja');

INSERT INTO transaction (id, amount, date, customer_id) VALUES (1, 120.0, '2025-07-01', 1);

INSERT INTO transaction (id, amount, date, customer_id) VALUES (2, 75.0, '2025-07-15', 1);

INSERT INTO transaction (id, amount, date, customer_id) VALUES (3, 95.0, '2025-06-10', 2);

INSERT INTO transaction (id, amount, date, customer_id) VALUES (4, 130.0, '2025-06-20', 2);

INSERT INTO transaction (id, amount, date, customer_id) VALUES (5, 40.0, '2025-05-05', 3);


