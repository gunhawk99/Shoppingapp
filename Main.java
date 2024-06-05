package shoppingapp;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize components
            ProductCatalog productCatalog = new ProductCatalog();
            OrderProcessing orderProcessing = OrderProcessing.getInstance();
            UserAuthentication userAuthentication = new UserAuthentication();
            PaymentProcessor paymentProcessor = new PaymentProcessor();
            try (Scanner scanner = new Scanner(System.in)) {
				// User registration and login
				System.out.println("Welcome to the Shopping System!");
				System.out.print("Register (1) or Login (2): ");
				int choice = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				String username, password, role;
				if (choice == 1) {
				    System.out.print("Enter username: ");
				    username = scanner.nextLine();
				    System.out.print("Enter password: ");
				    password = scanner.nextLine();
				    System.out.print("Enter role (admin/user): ");
				    role = scanner.nextLine();
				    boolean registered = userAuthentication.register(username, password, role);
				    if (registered) {
				        System.out.println("Registration successful. Please login.");
				    } else {
				        System.out.println("Registration failed. Username may already exist.");
				    }
				}

				System.out.print("Enter username: ");
				username = scanner.nextLine();
				System.out.print("Enter password: ");
				password = scanner.nextLine();
				boolean loggedIn = userAuthentication.login(username, password);
				if (!loggedIn) {
				    System.out.println("Login failed. Please check your credentials.");
				    return;
				}

				System.out.println("Login successful!");

				// Shopping operations
				Order order = new Order(userAuthentication.getCurrentUserId(), productCatalog);
				while (true) {
				    System.out.println("1. Add Product (Admin only)");
				    System.out.println("2. Add Item to Cart");
				    System.out.println("3. View Cart");
				    System.out.println("4. Remove Item from Cart");
				    System.out.println("5. Proceed to Payment");
				    System.out.println("6. List Products");
				    System.out.println("7. Logout");
				    System.out.print("Choose an option: ");
				    choice = scanner.nextInt();
				    scanner.nextLine(); // Consume newline

				    switch (choice) {
				        case 1:
				            if (!"admin".equals(userAuthentication.getCurrentUserRole())) {
				                System.out.println("Only admin can add products.");
				                break;
				            }
				            System.out.print("Enter product type (electronics/clothing): ");
				            String type = scanner.nextLine();
				            System.out.print("Enter product name: ");
				            String name = scanner.nextLine();
				            System.out.print("Enter product price: ");
				            double price = scanner.nextDouble();
				            scanner.nextLine(); // Consume newline
				            System.out.print("Enter product description: ");
				            String description = scanner.nextLine();

				            Product product;
				            if (type.equalsIgnoreCase("electronics")) {
				                System.out.print("Enter brand: ");
				                String brand = scanner.nextLine();
				                System.out.print("Enter model: ");
				                String model = scanner.nextLine();
				                System.out.print("Enter warranty period (months): ");
				                int warrantyPeriod = scanner.nextInt();
				                scanner.nextLine(); // Consume newline
				                product = ProductFactory.createProduct(type, 0, name, price, description, brand, model, String.valueOf(warrantyPeriod));
				            } else if (type.equalsIgnoreCase("clothing")) {
				                System.out.print("Enter size: ");
				                String size = scanner.nextLine();
				                System.out.print("Enter color: ");
				                String color = scanner.nextLine();
				                System.out.print("Enter material: ");
				                String material = scanner.nextLine();
				                product = ProductFactory.createProduct(type, 0, name, price, description, size, color, material);
				            } else {
				                System.out.println("Invalid product type.");
				                continue;
				            }

				            productCatalog.addProduct(product);
				            System.out.println("Product added successfully.");
				            break;

				        case 2:
				        	
				            System.out.println("Enter the product ID to add to your cart:");
				            for (Product p : productCatalog.listProducts()) {
				                System.out.println(p);
				            }

				            int productId = scanner.nextInt();
				            Product p = productCatalog.getProductById(productId);
				            if (p != null) {
				                order.addProduct(p);
				                System.out.println("Added " + p.getName() + " to your cart.");
				            } else {
				                System.out.println("Product not found.");
				            }
				            break;

				        case 3:
				            System.out.println("Your cart:");
				            for (Product prod : order.getProductList()) {
				                System.out.println(prod);
				            }
				            System.out.println("Total: $" + order.getTotalPrice());
				            break;

				        case 4:
				            System.out.println("Enter the product ID to remove from your cart:");
				            int removeProductId = scanner.nextInt();
				            order.removeProduct(removeProductId);
				            break;

				        case 5:
				            System.out.println("Proceeding to payment...");
				            if (paymentProcessor.processPayment(order.getTotalPrice())) {
				                orderProcessing.placeOrder(order);
				                System.out.println("Order placed successfully. Total: $" + order.getTotalPrice());
				                order.clearCart();
				            } else {
				                System.out.println("Payment failed.");
				            }
				            break;

				        case 6:
				            System.out.println("Listing all products...");
				            for (Product prod : productCatalog.listProducts()) {
				                System.out.println(prod);
				            }
				            break;

				        case 7:
				            userAuthentication.logout();
				            System.out.println("Logged out successfully.");
				            return;

				        default:
				            System.out.println("Invalid choice. Please try again.");
				    }
				}
			}
        } catch (Exception e) {
            CustomLogger.log(Level.SEVERE, "An error occurred: " + e.getMessage(), e);
        }
    }
}
