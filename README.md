Phase 1: Core System (Java & Hibernate)
Features:
User roles: Administrator, Specialist, and Customer.
Administrator can add, modify, or delete main services (e.g., decor, plumbing) and sub-services (e.g., kitchen appliances).
Specialists can be assigned to specific sub-services.
Validation includes unique email checks and password requirements (8 characters, mix of letters and numbers).
Specialists' status changes from "New" to "Pending Approval" or "Approved" based on admin verification.
Customers can request services with specific requirements (price, date, address).
Order status tracking: "Waiting for Specialist Proposals," "In Progress," "Completed," and "Paid."

Phase 2: Spring Boot & Spring Data
Reimplemented Features:
All Phase 1 features are adapted to Spring Boot with unit tests for each service method.
Specialists can propose bids on open orders.
Orders can be viewed, organized, and assigned to specialists based on customer preferences.

Phase 3: MVC & API Testing
Enhancements:
System refactored using Spring MVC.
Customer payment options: account balance or online payment with validation.
Post-service feedback: customers rate specialists on a 1-5 scale and can add comments.

Phase 4: Security & User Management
Security:
Spring Security implemented with role-based access control.
Activation emails for new accounts; specialists' status updated upon verification.
Profile management for specialists and customers, with full service history and reporting available for administrators.

Phase 5: JWT, Spring Data JPA, and Enhanced Exception Handling
JWT Integration: Implemented JWT for stateless authentication, securing APIs with role-based access control.
Spring Data JPA: Integrated Spring Data JPA for CRUD operations while retaining CriteriaBuilder for complex custom searches.
Enhanced Exception Handling: Centralized exception management with @RestControllerAdvice, integrating SLF4J logging for detailed error tracking and improved debugging.

## Technologies Used

Backend: Java, Hibernate, Spring Boot, Spring MVC, Spring Security
Database: PostgreSQL
Testing: JUnit, Postman
