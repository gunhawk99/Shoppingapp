# Shoppingapp

**Shopping Application Project**
This project implements a simple shopping application with functionalities for user authentication, product management, order processing, and payment handling. It utilizes Java programming language and MySQL database for backend operations.

**Classes:**
**1. UserAuthentication**
•	Handles user authentication tasks such as login, logout, and user registration.
•	Utilizes a MySQL database for user authentication and session management.

**2. ProductFactory**
•	Responsible for creating instances of different types of products (Electronics and Clothing) based on provided parameters.

**3. ProductCatalog**
•	Manages product-related tasks such as adding, removing, and listing products from a MySQL database.

**4. Product**
•	Abstract class representing a generic product with common attributes like ID, name, price, and description.
•	Specific product types (Electronics and Clothing) extend this class.

**5. PaymentProcessor**
•	Handles the processing of payments, simulating successful payment processing for positive amounts.

**6. OrderProcessing**
•	Manages the processing of orders including placing orders and canceling orders.
•	Interacts with the PaymentProcessor for payment processing.

**7. Order**
•	Represents an order in the shopping system, managing addition/removal of products and order retrieval from the database.

**8. Main**
•	Contains the main method for running the shopping application, providing a command-line interface for user interaction.

**9. Electronics**
•	Represents electronic products in the shopping system, extending the Product class and adding specific attributes like brand, model, and warranty period.

**10. CustomLogger**
•	Provides custom logging functionality for the shopping application using Java's built-in logging framework.

**11. Clothing**
•	Represents clothing products in the shopping system, extending the Product class and adding specific attributes like size, color, and material.

**Functionality:**
**•**	**User Authentication:** Allows users to register, login, and logout.
**•	Product Management:** Supports adding, removing, listing, and retrieving products.
**•	Order Processing:** Enables users to place orders, cancel orders, and view their cart.
**•	Payment Handling:** Simulates payment processing for orders using a custom payment processor.

How it works:
1.	Starting the Application:
•	Execute the Main class to launch the shopping application.

2.	User Registration and Login:
a.	Users are prompted to register or login.
b.	Registration involves providing a username, password, and role.
c.	Upon successful registration or login, users are granted access to the system.

3.	Managing Products:
a.	Admin users can add new products, specifying type, name, price, and attributes.
b.	Product data is stored in a MySQL database for retrieval and management.

4.	Shopping Operations:
a.	Users can add products to their cart, view the cart contents, and remove items.
b.	Proceeding to payment initiates a simulated payment process.

5.	Order Processing:
a.	Payment verification leads to order placement, with details saved in the database.
b.	Users can cancel orders if needed, removing them from the system.

6.	Logging and Security:
a.	Custom logging tracks system events and errors for debugging and monitoring.
b.	User authentication ensures secure access to the application's functionalities.

setup:
Database: We use MySql database for data persistant so ensure database connection is set properly.
Dependencies: add mysqlconnector jar file.
