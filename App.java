import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class App {

    static int storeID = 1;
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static Scanner sc = new Scanner(System.in);


    //-----------------------------------------------------------------------
    static final String jdbcURL = ""; //jdbc url
    public static class GoToMainMenuException extends RuntimeException {}
   // Main Function starts here
    public static void main(String[] args) {

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            String user = ""; //user
            String passwd = ""; //userpass
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            mainMenu();
            
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }


    }
    
    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    // Main menu for different operations
    public static void mainMenu() {
    	 boolean flag = true;

         while(flag){

             System.out.println("Select your Task:");
             System.out.println("1 : Information processing");
             System.out.println("2 : Maintaining inventory records");
             System.out.println("3 : Maintaining billing and transaction records");
             System.out.println("4 : Reports");
             System.out.println("5 : Display Data");
             System.out.println("6 : Break");

             System.out.print("Please enter the input(1-6): ");
             System.out.println();


             int option = sc.nextInt();

             switch(option){
                 case 1:
                     informationProcessing();
                     break;
                 case 2:
                     maintainingInventoryRecords();
                     break;
                 case 3:
                     maintainingBillsAndTransactionRecords();
                     break;
                 case 4:
                     reports();
                     break;
                 case 5:
                     display();
                     break;
                 case 6:
                	 break;
                 default:
                     System.out.println("Please enter a valid input!");
                     break;

             }

             System.out.print("Do you want to update anything else ? [y/n] : ");
             char c = sc.next().charAt(0);
             if(c == 'y' || c == 'Y'){
                 continue;
             }else if(c == 'n' || c == 'N'){
                 flag = false;
                 System.out.println("Thank you for Using the application");
             }
         }
    }
//------------------Methods for Tasks and Operations--------------------

//----------------------------------Task 1. Information processing--------------------------------------------------------------------

    public static void informationProcessing() {
        boolean running = true;

        try {
            while (running) {
                System.out.println("\n==== WHOLESALE SYSTEM MENU ====\nSelect Category:\n1: Store\n2: Customer\n3: Staff\n4: Supplier\n5: Discount\n0: Exit");
                System.out.print("Enter your choice: ");
                int category = sc.nextInt();

                String[] categories = {"Store", "Customer", "Staff", "Supplier", "Discount"};
                Runnable[][] operations = {
                        {() -> addStore(), () -> updateStore(), () -> deleteStore()},
                        {() -> addCustomer(), () -> updateCustomer(), () -> deleteCustomer()},
                        {() -> addStaff(), () -> updateStaff(), () -> deleteStaff()},
                        {() -> addNewSupplier(), () -> updateSupplierInfo(), () -> deleteSupplier()},
                        {() -> addDiscount(), () -> addDiscountToProduct(), () -> updateDiscount(), () -> deleteDiscount()}
                };

                if (category >= 1 && category <= 5) {
                	if(category == 5) {
                		System.out.println("\n--- " + categories[category - 1] + " Operations ---\n1: Add\n2: Add Discount to Product \n3: Update\n4: Delete");
                	}else {
                    System.out.println("\n--- " + categories[category - 1] + " Operations ---\n1: Add\n2: Update\n3: Delete");
                	}System.out.print("Select operation: ");
                    int op = sc.nextInt();

                    if (op >= 1 && op <= 3) {
                        operations[category - 1][op - 1].run();
                    } else {
                        System.out.println("Invalid operation.");
                    }
                } else if (category == 0) {
                    running = false;
                    System.out.println("Exiting...");
                } else {
                    System.out.println("Invalid category. Try again.");
                }
            }
        } catch (GoToMainMenuException e) {
            System.out.println("Returning to main menu...");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }
    }

    // ---------- STORE EXAMPLE ----------
    public static void addStore() {
        try {
            System.out.println("Enter Store Information");

            System.out.print("Store Address: ");
            sc.nextLine(); // consume leftover newline
            String storeAddr = sc.nextLine();

            System.out.print("Store Phone: ");
            String storePhone = sc.next();

            stmt = conn.createStatement();
            String query = "INSERT INTO Store(storeAddress, phoneNumber) VALUES ('" + storeAddr + "', '" + storePhone + "')";
            stmt.executeUpdate(query);

            System.out.println("Store added successfully.");

            System.out.print("Do you want to return to the main menu? [y/n]: ");
            char c = sc.next().charAt(0);
            if (c == 'y' || c == 'Y') {
                throw new GoToMainMenuException();
            }

        } catch (GoToMainMenuException e) {
            throw e; // rethrow to be caught in informationProcessing()
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }
    }
public static void updateStore() {
    boolean flag = true;
   
    while (flag) {
    	 try {
    		    stmt = conn.createStatement();
        	int storeId = getValidForeignKeyId(stmt, "Store", "storeId");

            System.out.println("\nUpdate:\n1 : Address\n2 : Phone");
            System.out.print("Select an option: ");
            int input = sc.nextInt();

            String query = "", column = "", value = "";

            switch (input) {
                case 1 -> {
                    System.out.print("Enter the new address: ");
                    sc.nextLine(); // clear buffer
                    value = sc.nextLine();
                    column = "storeAddress";
                }
                case 2 -> {
                    System.out.print("Enter the new phone number: ");
                    value = sc.next();
                    column = "phoneNumber";
                }
                default -> {
                    System.out.println("Invalid input. Try again.");
                    continue;
                }
            }

            query = "UPDATE Store SET " + column + " = '" + value + "' WHERE storeId = " + storeId;
            
            stmt.executeUpdate(query);
            System.out.println("Store updated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {}    
        }

        System.out.print("Do you wish to update anything else? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true; // continue loop
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException(); // exit to main menu
        } else {
            flag = false; // exit updateStore loop
        }
    }
}
public static void deleteStore() {
    boolean flag = true;

    while (flag) {
    	


        try {
            stmt = conn.createStatement();
            int storeId = getValidForeignKeyId(stmt, "Store", "storeId");
            int rowsAffected = stmt.executeUpdate("DELETE FROM Store WHERE storeId = " + storeId);

            if (rowsAffected > 0) {
                System.out.println("Store with ID " + storeId + " deleted successfully.");
            } else {
                System.out.println("No store found with ID " + storeId + ".");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you wish to delete another store? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true; // keep going
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException(); // jump to main menu
        } else {
            flag = false; // exit delete loop
        }
    }
}

public static void addCustomer() {
    boolean flag = true;

    while (flag) {
        try {
            System.out.println("Enter Customer Information");

            sc.nextLine(); // clear input buffer
            stmt = conn.createStatement();

            System.out.print("First Name: ");
            String firstName = sc.nextLine();

            System.out.print("Last Name: ");
            String lastName = sc.nextLine();

            System.out.print("Phone Number: ");
            String phoneNumber = sc.nextLine();

            System.out.print("Address: ");
            String address = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.next();

            System.out.print("Membership Level (Gold/Platinum): ");
            String membershipLevel = sc.next();

            System.out.print("Membership Status (Active/Inactive): ");
            String membershipStatus = sc.next();

            int staffId = getValidForeignKeyId(stmt, "Staff", "staffID");
            int storeId = getValidForeignKeyId(stmt, "Store", "storeId");

            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(365);
            String startDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String insertCustomer = String.format(
                "INSERT INTO Customer (firstName, lastName, phone, homeAddress, Email, membershipLevel, memberShipStartDate, ActiveTillDate, ActiveStatus) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                firstName, lastName, phoneNumber, address, email, membershipLevel, startDate, endDateStr, membershipStatus
            );

            stmt.executeUpdate(insertCustomer);

            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int customerId = -1;
            if (rs.next()) {
                customerId = rs.getInt(1);
            }

            if (customerId == -1) {
                System.out.println("Failed to retrieve new customer ID.");
                return;
            }

            String insertSignup = String.format(
                "INSERT INTO SignUpInformation (staffId, customerId, signUpDate, storeId) " +
                "VALUES (%d, %d, CURDATE(), %d)",
                staffId, customerId, storeId
            );

            stmt.executeUpdate(insertSignup);
            System.out.println("Customer signed-up and added successfully.");

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        // Ask what to do next
        System.out.print("Do you want to add another customer? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true; // keep going
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException(); // return to main menu
        } else {
            flag = false; // exit loop and return to category menu
        }
    }
}
public static void updateCustomer() {
    System.out.print("Enter Customer ID: ");
    String CustomerID = sc.next();
    sc.nextLine(); // clear buffer

    String[] fields = {
        "firstName", "lastName", "phone", "homeAddress", "Email", "membershipLevel", "membershipStatus"
    };

    boolean flag = true;

    while (flag) {
        System.out.println("\nChoose field to update:");
        for (int i = 0; i < fields.length; i++) {
            System.out.printf("%d: %s%n", i + 1, fields[i]);
        }

        System.out.print("Enter option: ");
        int option = sc.nextInt();
        sc.nextLine(); // consume newline

        if (option < 1 || option > fields.length) {
            System.out.println("Invalid option.");
            continue;
        }

        String column = fields[option - 1];
        System.out.print("Enter new value for " + column + ": ");
        String newValue = sc.nextLine();

        updateCustomerField(CustomerID, column, newValue);

        System.out.print("Do you wish to update another field? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);
        sc.nextLine(); // consume newline

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException(); // exit to main menu
        } else {
            flag = false; // return to category submenu
        }
    }
}
public static void updateCustomerField(String CustomerID, String column, String newValue) {
    try {
        stmt = conn.createStatement();
        String query = String.format("UPDATE Customer SET %s = '%s' WHERE customerId = %s", column, newValue, CustomerID);
        int rows = stmt.executeUpdate(query);

        if (rows > 0)
            System.out.println(column + " updated successfully.");
        else
            System.out.println("No customer found with ID: " + CustomerID);

    }  catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
}
public static void deleteCustomer() {
    boolean flag = true;

    while (flag) {
        //System.out.print("Enter Customer ID to be deactivated: ");
        

        try {
            stmt = conn.createStatement();
            int customerID = getValidForeignKeyId(stmt, "Customer", "customerID");
            int rows = stmt.executeUpdate(
                "UPDATE Customer SET ActiveStatus = 'Inactive' WHERE customerId = " + customerID
            );

            if (rows > 0) {
                System.out.println("Customer with ID " + customerID + " has been marked as Inactive.");
            } else {
                System.out.println("No Customer found with ID " + customerID + ".");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to deactivate another customer? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true; // continue loop
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException(); // return to main menu
        } else {
            flag = false; // exit to category submenu
        }
    }
}


public static void addStaff() {
    boolean flag = true;

    while (flag) {
        System.out.println("Enter Staff Information");

        int storeID;
        try {
            while (true) {
                stmt = conn.createStatement();
                storeID = getValidForeignKeyId(stmt, "Store", "storeId");
                sc.nextLine(); // clear buffer

                String query = "SELECT 1 FROM Store WHERE storeId = " + storeID;
                ResultSet rsId = stmt.executeQuery(query);
                if (rsId.next()) {
                    break; // valid ID found
                } else {
                    System.out.println("Error: Store ID " + storeID + " does not exist. Please enter a valid Store ID.");
                }
            }

            // Input remaining staff details
            System.out.print("Staff Name: ");
            String staffName = sc.nextLine();

            System.out.print("Age: ");
            int age = sc.nextInt();
            sc.nextLine(); // clear buffer

            System.out.print("Job Title: ");
            String jobTitle = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.next();

            System.out.print("Address: ");
            sc.nextLine(); // clear buffer
            String address = sc.nextLine();

            System.out.print("Phone Number: ");
            String phoneNumber = sc.next();

            System.out.print("Time of Employment: ");
            String date = sc.next();
            

            // Insert into Staff table
            String insertQuery = String.format("""
                INSERT INTO Staff (storeId, name, age, jobTitle, email, phoneNo, address, timeOfEmployment)
                VALUES (%d, '%s', %d, '%s', '%s', '%s', '%s', '%s')
            """, storeID, staffName, age, jobTitle, email, phoneNumber, address, date);

            stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            int staffID = rs.next() ? rs.getInt(1) : -1;

            // Update manager if job title is "Manager"
            if (jobTitle.equalsIgnoreCase("Manager") && staffID != -1) {
                String updateQuery = "UPDATE Store SET managerId = " + staffID + " WHERE storeId = " + storeID;
                stmt.executeUpdate(updateQuery);
            }

            System.out.println("Staff added successfully.");
            System.out.print("Do you wish to add another staff? [y/n] (Or press 'm' to return to main menu): ");
            char c = sc.next().charAt(0);
            //sc.nextLine(); // consume newline

            if (c == 'y' || c == 'Y') {
                flag = true;
            } else if (c == 'm' || c == 'M') {
                throw new GoToMainMenuException(); // exit to main menu
            } else {
                flag = false; // return to category sub
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

       
    }
}

public static void updateStaff() {
    boolean flag = true;

    int staffID;
	try {
		stmt = conn.createStatement();
		staffID = getValidForeignKeyId(stmt, "Staff", "staffID");
	
    sc.nextLine(); // clear buffer

    String[] fields = {
        "storeID", "name", "age", "jobTitle", "email", "phoneNo", "address", "timeOfEmployment"
    };

    while (flag) {
        System.out.println("\nUpdate Options:");
        for (int i = 0; i < fields.length; i++) {
            System.out.printf("%d: %s%n", i + 1, fields[i]);
        }

        System.out.print("Select a field to update: ");
        int input = sc.nextInt();
        sc.nextLine(); // clear newline

        if (input < 1 || input > fields.length) {
            System.out.println("Invalid option. Try again.");
            continue;
        }

        String column = fields[input - 1];
        System.out.print("Enter new value for " + column + ": ");

        String newValue;
        if (column.equals("age") || column.equals("storeID")) {
            newValue = String.valueOf(sc.nextInt());
            sc.nextLine();
        } else {
            newValue = sc.nextLine();
        }

            String query = String.format("UPDATE Staff SET %s = '%s' WHERE staffID = %d", column, newValue, staffID);
            stmt.executeUpdate(query);
            System.out.println(column + " updated successfully.");

            // If job title updated to Manager, update Store table too
            if (column.equalsIgnoreCase("jobTitle") && newValue.equalsIgnoreCase("Manager")) {
                ResultSet rs = stmt.executeQuery("SELECT storeID FROM Staff WHERE staffID = " + staffID);
                if (rs.next()) {
                    int storeID = rs.getInt("storeID");
                    stmt.executeUpdate("UPDATE Store SET managerId = " + staffID + " WHERE storeID = " + storeID);
                    System.out.println("Manager ID updated in Store table.");
                }
                rs.close();
            }


        System.out.print("Do you wish to update anything else? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);
        sc.nextLine(); // clear newline

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
	}  catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
}
public static void deleteStaff() {
    boolean flag = true;

    while (flag) {
     

        try {
            stmt = conn.createStatement();
            int staffID = getValidForeignKeyId(stmt, "Staff", "staffID");
            int rows = stmt.executeUpdate("DELETE FROM Staff WHERE staffID = " + staffID);

            if (rows > 0) {
                System.out.println("Staff with ID " + staffID + " has been removed successfully.");
            } else {
                System.out.println("No staff found with ID " + staffID + ".");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to delete another staff member? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

public static void addNewSupplier() {
    boolean flag = true;

    while (flag) {
        System.out.println("Enter Supplier Information");

        sc.nextLine(); // clear buffer

        System.out.print("Supplier Name: ");
        String suppName = sc.nextLine();

        System.out.print("Supplier Phone: ");
        String suppPhone = sc.nextLine();

        System.out.print("Supplier Email: ");
        String suppEmail = sc.nextLine();

        System.out.print("Supplier Location: ");
        String suppLocation = sc.nextLine();

        try {
            stmt = conn.createStatement();
            String query = String.format("""
                INSERT INTO SupplierInformation (SupplierName, Phone, Email, Location)
                VALUES ('%s', '%s', '%s', '%s')
            """, suppName, suppPhone, suppEmail, suppLocation);

            stmt.executeUpdate(query);
            System.out.println("Supplier added successfully.");

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to add another supplier? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void updateSupplierInfo() {
    boolean flag = true;

    String[] fields = {"SupplierName", "Phone", "Email", "Location"};

    while (flag) {
        try {
        	stmt = conn.createStatement();
            //String query = "";

        	int suppID = getValidForeignKeyId(stmt, "SupplierInformation", "SupplierId");
            sc.nextLine(); // clear buffer

            System.out.println("\nChoose field to update:");
            for (int i = 0; i < fields.length; i++) {
                System.out.printf("%d: Change %s%n", i + 1, fields[i]);
            }

            System.out.print("Enter option: ");
            int option = sc.nextInt();
            sc.nextLine(); // clear buffer

            if (option < 1 || option > fields.length) {
                System.out.println("Invalid option.");
                continue;
            }

            String column = fields[option - 1];
            System.out.print("Enter new value for " + column + ": ");
            String newValue = sc.nextLine();

            updateSupplierColumn(column, newValue, suppID);

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you wish to update anything else? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);
        sc.nextLine(); // clear buffer

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void updateSupplierColumn(String column, String newValue, int suppID) {
    try {
        stmt = conn.createStatement();
        String query = "UPDATE SupplierInformation SET " + column + " = '" + newValue + "' WHERE supplierId = " + suppID;
        stmt.executeUpdate(query);
        System.out.println(column + " updated successfully.");
    }  catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
}
public static void deleteSupplier() {
    boolean flag = true;

    while (flag) {
        System.out.println("Delete Supplier Information");

       

        try {
            stmt = conn.createStatement();
            int suppID = getValidForeignKeyId(stmt, "SupplierInformation", "supplierId");
            int rows = stmt.executeUpdate("DELETE FROM SupplierInformation WHERE supplierId = " + suppID);

            if (rows > 0) {
                System.out.println("Supplier with ID " + suppID + " deleted successfully.");
            } else {
                System.out.println("No supplier found with ID " + suppID + ".");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to delete another supplier? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

public static void addDiscount() {
    boolean flag = true;

    while (flag) {
        System.out.println("Enter Discount Information");
        System.out.println("------------------------------");

        System.out.print("Enter the Discount Percentage: ");
        double discountPercentage = sc.nextDouble();

        System.out.print("Enter Sale Start Date (YYYY-MM-DD): ");
        String startDate = sc.next();

        System.out.print("Enter Sale End Date (YYYY-MM-DD): ");
        String endDate = sc.next();
        
        

        try {
            stmt = conn.createStatement();

            // Insert into Discount table
            String insertQuery = String.format(
                "INSERT INTO Discount (saleStartDate, saleEndDate, discountPercentage) VALUES ('%s', '%s', %.2f)",
                startDate, endDate, discountPercentage
            );
            stmt.executeUpdate(insertQuery);

            // Get generated discount ID
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int discountId = rs.next() ? rs.getInt(1) : -1;

            if (discountId == -1) {
                System.out.println("Failed to retrieve discount ID.");
                return;
            }

            System.out.println("Discount added with ID: " + discountId);

            System.out.print("Do you want to add this discount to any products right now? (y/n): ");
            char answer = sc.next().charAt(0);
            boolean more = (answer == 'y' || answer == 'Y');

            // Assign to products
            while (more) {
                //System.out.print("Enter Product ID to apply this discount: ");
                int productId = getValidForeignKeyId(stmt, "Product","productId" );

                int rows = stmt.executeUpdate("UPDATE Product SET discountId = " + discountId + " WHERE productId = " + productId);
                System.out.println(rows > 0
                    ? "Discount applied to Product ID: " + productId
                    : "Product ID " + productId + " not found or not updated.");

                System.out.print("Do you want to add this discount to another product? (y/n): ");
                more = sc.next().equalsIgnoreCase("y");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        // Ask for next action
        System.out.print("Do you want to add another discount? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

public static void addDiscountToProduct() {

	try {
		stmt = conn.createStatement();
        String query = "";
        int discountID = getValidForeignKeyId(stmt, "Discount", "discountId");
    	int productID = getValidForeignKeyId(stmt, "Product", "productId");

    	query = "UPDATE Product SET discountId = " + discountID + " WHERE productId = " + productID;
    	int rows = stmt.executeUpdate(query);

    	System.out.println(rows > 0 ? "Discount assigned to product successfully." : "Failed to assign discount.");

		System.out.print("Do you want to return to the main menu? [y/n]: ");
	    char c = sc.next().charAt(0);
	    if (c == 'y' || c == 'Y') {
	        throw new GoToMainMenuException();
	    }
	} catch (GoToMainMenuException e) {
	    throw e; // rethrow to be caught in informationProcessing()
	}  catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
		
}
public static void updateDiscount() {
    boolean flag = true;

    while (flag) {
        System.out.println("\nUpdate Discount Details");
        System.out.println("1: Change Discount Percentage");
        System.out.println("2: Change Start Date");
        System.out.println("3: Change End Date");
        System.out.print("Enter your choice: ");
        int option = sc.nextInt();

        try {
        	stmt = conn.createStatement();
            String query = "";
            
            int discountID = getValidForeignKeyId(stmt, "Discount", "discountId");

            
            switch (option) {
                case 1 -> {
                    System.out.print("Enter the new Discount Percentage: ");
                    double percentage = sc.nextDouble();
                    query = "UPDATE Discount SET discountPercentage = " + percentage + " WHERE discountId = " + discountID;
                }
                case 2 -> {
                    System.out.print("Enter the new Start Date (YYYY-MM-DD): ");
                    String startDate = sc.next();
                    query = "UPDATE Discount SET saleStartDate = '" + startDate + "' WHERE discountId = " + discountID;
                }
                case 3 -> {
                    System.out.print("Enter the new End Date (YYYY-MM-DD): ");
                    String endDate = sc.next();
                    query = "UPDATE Discount SET saleEndDate = '" + endDate + "' WHERE discountId = " + discountID;
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    continue;
                }
            }

            int rows = stmt.executeUpdate(query);
            System.out.println(rows > 0 ? "Discount updated successfully." : "No discount found with ID: " + discountID);

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to update another discount? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void deleteDiscount() {
    boolean flag = true;

    while (flag) {
        System.out.println("Delete Discount");
        System.out.println("------------------------------");

       

        try {
            stmt = conn.createStatement();
            int discountID = getValidForeignKeyId(stmt, "Discount", "discountId");
            int rows = stmt.executeUpdate("DELETE FROM Discount WHERE discountId = " + discountID);

            if (rows > 0) {
                System.out.println(" Discount with ID " + discountID + " deleted successfully.");
            } else {
                System.out.println(" No discount found with ID: " + discountID);
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to delete another discount? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}


//------------------------------------------------------------Task 2. Maintaining inventory records----------------------------------------
public static void maintainingInventoryRecords() {
    boolean running = true;

    try {
        while (running) {
            System.out.println("\nInventory Options:");
            System.out.println("1: Add product to Inventory and Store");
            System.out.println("2: Do you want to return any products?");
            System.out.println("3: Transfer product between two stores");
            System.out.println("0: Back");

            System.out.print("Enter your choice: ");
            int option = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (option) {
                case 1 -> addNewInventoryForSupplierWithTransaction();
                case 2 -> returnItems();
                case 3 -> transferInventory();
                case 0 -> {
                    running = false;
                    System.out.println("Returning to main menu...");
                }
                default -> System.out.println("Invalid input! Please try again.");
            }
        }
    }  catch (Exception e) {
        e.printStackTrace();
    } 
}

    
public static int getValidForeignKeyId(Statement stmt, String tableName, String columnName) throws SQLException {
    int id;

    while (true) {
        System.out.print("Enter the "+columnName+ ": ");
        id = sc.nextInt();
        sc.nextLine(); // clear buffer

        String query = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = " + id;
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            break; // valid foreign key found
        } else {
            System.out.println("Error: " + id + " in " + columnName+ " does not exist. Please enter a valid "+columnName+": ");
        }
    }

    return id;
}

public static void addNewInventoryForSupplierWithTransaction() {
    boolean flag = true;

    while (flag) {
        try {
            stmt = conn.createStatement();

            int storeId = getValidForeignKeyId(stmt, "Store", "storeId");
            int supplierId = getValidForeignKeyId(stmt, "SupplierInformation", "SupplierId");
            int billingStaffId = getValidForeignKeyId(stmt, "Staff", "staffID");

            double totalBill = 0.0;
            char moreProducts = 'y';
            List<String> productInsertQueries = new ArrayList<>();
            List<Integer> productQuantities = new ArrayList<>();

            // Collect product info
            while (moreProducts == 'y' || moreProducts== 'Y') {

                System.out.print("\nProduct Name: ");
                String productName = sc.nextLine();

                System.out.print("Buy Price: ");
                double buyPrice = sc.nextDouble();

                System.out.print("Market Price: ");
                double marketPrice = sc.nextDouble();

                System.out.print("Production Date (YYYY-MM-DD): ");
                String productionDate = sc.next();            

                System.out.print("Expiry Date (YYYY-MM-DD): ");
                String expiryDate = sc.next();

                System.out.print("Discount ID (or 0 if none): ");
                int discountId = sc.nextInt();

                System.out.print("Quantity: ");
                int quantity = sc.nextInt();

                totalBill += quantity * buyPrice;

                // Prepare insert query and quantity for later use
                String insertProduct = String.format("""
                    INSERT INTO Product 
                    (productName, supplierId, buyPrice, marketPrice, productionDate, expiryDate, discountId, quantityInStock, storeId)
                    VALUES ('%s', %d, %.2f, %.2f, '%s', '%s', %s, %d, %d)
                    """, productName, supplierId, buyPrice, marketPrice, productionDate, expiryDate,
                        (discountId == 0 ? "NULL" : String.valueOf(discountId)), quantity, storeId);

                productInsertQueries.add(insertProduct);
                productQuantities.add(quantity);

                System.out.print("Add another product? (y/n): ");
                moreProducts = sc.next().charAt(0);
            }
            LocalDate currentDate = LocalDate.now(); // get current date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // define format

            String dateString = currentDate.format(formatter); // convert to String

			// Insert SupplierTransaction first
            String insertTxn = String.format("""
                INSERT INTO SupplierTransaction
                (BillingStaffId, SupplierId, StoreId, TotalPrice, PurchaseDate)
                VALUES (%s, %d, %d, %.2f, '%s')
                """,
                (billingStaffId == 0 ? "NULL" : String.valueOf(billingStaffId)), supplierId, storeId, totalBill, dateString);

            stmt.executeUpdate(insertTxn, Statement.RETURN_GENERATED_KEYS);
            ResultSet rsTxn = stmt.getGeneratedKeys();
            int transactionID = rsTxn.next() ? rsTxn.getInt(1) : -1;

            // Now insert each product and link it
            for (int i = 0; i < productInsertQueries.size(); i++) {
                String insertProduct = productInsertQueries.get(i);
                int quantity = productQuantities.get(i);

                stmt.executeUpdate(insertProduct);
                ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                int productId = rs.next() ? rs.getInt(1) : -1;

                if (productId != -1) {
                    String insertCart = String.format("""
                        INSERT INTO SupplierProductCart (transactionId, productId, quantity)
                        VALUES (%d, %d, %d)
                        """, transactionID, productId, quantity);
                    stmt.executeUpdate(insertCart);

                    System.out.println("Transaction ID: " + transactionID + " | Product ID: " + productId);
                }
            }

            
            System.out.print("Do you want to continue? [y/n]: ");
            char c = sc.next().charAt(0);
            if (c == 'n' || c == 'N') {
                flag = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
          //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }
    }
}

public static void returnItems() {
    boolean flag = true;

    while (flag) {
        try {
            stmt = conn.createStatement();

            int transactionID = getValidForeignKeyId(stmt, "CustomerTransaction", "TransactionId");
            int productID = getValidForeignKeyId(stmt, "Product", "productId");

            System.out.print("Enter the quantity to return: ");
            int returnQty = sc.nextInt();

            conn.setAutoCommit(false);

            // Step 1: Get storeID
            ResultSet rs = stmt.executeQuery("SELECT StoreId FROM CustomerTransaction WHERE TransactionId = " + transactionID);
            if (!rs.next()) {
                System.out.println("Invalid transaction ID.");
                conn.rollback();
                continue;
            }
            int storeID = rs.getInt("StoreId");

            // Step 2: Validate product in ProductCart
            rs = stmt.executeQuery("SELECT quantity, totalPrice FROM ProductCart WHERE transactionId = " + transactionID + " AND productId = " + productID);
            if (!rs.next()) {
                System.out.println("Product not found in this transaction.");
                conn.rollback();
                continue;
            }

            int oldQty = rs.getInt("quantity");
            double totalPrice = rs.getDouble("totalPrice");

            if (returnQty > oldQty) {
                System.out.println("❌ Return quantity exceeds purchased quantity.");
                conn.rollback();
                continue;
            }

            double perUnitPrice = totalPrice / oldQty;
            double refund = perUnitPrice * returnQty;

            // Step 3: Update stock in Product table
            rs = stmt.executeQuery("SELECT quantityInStock FROM Product WHERE productId = " + productID + " AND storeId = " + storeID);
            if (rs.next()) {
                int newStock = rs.getInt("quantityInStock") + returnQty;
                stmt.executeUpdate("UPDATE Product SET quantityInStock = " + newStock + " WHERE productId = " + productID + " AND storeId = " + storeID);
            } else {
                stmt.executeUpdate(String.format("""
                    INSERT INTO Product (productId, productName, buyPrice, marketPrice, productionDate, expiryDate, quantityInStock, storeId)
                    VALUES (%d, 'Returned Product', 0, 0, '2024-01-01', '2024-12-31', %d, %d)
                    """, productID, returnQty, storeID));
            }

            // Step 4: Get customer and billing staff
            rs = stmt.executeQuery("SELECT CustomerID, BillingStaffId FROM CustomerTransaction WHERE TransactionId = " + transactionID);
            int customerID = rs.next() ? rs.getInt("CustomerID") : 0;
            int billingStaffID = rs.getInt("BillingStaffId");

            // Step 5: Insert into HandlesReturn
            stmt.executeUpdate(String.format("""
                INSERT INTO HandlesReturn (TransactionId, ProductId, billingStaffId, refundedAmount, quantity, customerId)
                VALUES (%d, %d, %d, %.2f, %d, %d)
                """, transactionID, productID, billingStaffID, refund, returnQty, customerID));

            conn.commit();
            System.out.println(" Return processed successfully. Refund: $" + refund);

        } catch (Throwable err) {
            System.out.println("Error during return. Rolling back...");
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
            	//try { if (stmt != null) close(stmt); } catch (Exception e) {} 
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.print("Do you want to return another item? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void transferInventory() {
    boolean flag = true;

    while (flag) {
        try {
            stmt = conn.createStatement();

            int productID = getValidForeignKeyId(stmt, "Product", "productId");
            System.out.print("Enter source store ID: ");
            int fromStore = sc.nextInt();

            System.out.print("Enter destination store ID: ");
            int toStore = sc.nextInt();

            System.out.print("Enter quantity to transfer: ");
            int transferQty = sc.nextInt();

            if (fromStore == toStore) {
                System.out.println("Source and destination stores cannot be the same.");
                continue;
            }

            conn.setAutoCommit(false);

            // Step 1: Validate product in source store
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE productId = " + productID + " AND storeId = " + fromStore);
            if (!rs.next()) {
                System.out.println("Product not found in source store.");
                conn.rollback();
                continue;
            }

            int availableQty = rs.getInt("quantityInStock");
            String productName = rs.getString("productName");

            if (availableQty < transferQty) {
                System.out.println("Not enough quantity to transfer.");
                conn.rollback();
                continue;
            }

            // Step 2: Deduct from source store
            stmt.executeUpdate(String.format(
                "UPDATE Product SET quantityInStock = quantityInStock - %d WHERE productId = %d AND storeId = %d",
                transferQty, productID, fromStore));

            // Step 3: Check if destination store already has the product
            rs = stmt.executeQuery("SELECT productId FROM Product WHERE productName = '" + productName + "' AND storeId = " + toStore);
            if (rs.next()) {
                int destProductId = rs.getInt("productId");

                // Step 4a: Add quantity to destination's existing product
                stmt.executeUpdate(String.format(
                    "UPDATE Product SET quantityInStock = quantityInStock + %d WHERE productId = %d AND storeId = %d",
                    transferQty, destProductId, toStore));
            } else {
                // Step 4b: Copy product to destination store with transferred quantity
                stmt.executeUpdate(String.format("""
                    INSERT INTO Product (productName, supplierId, buyPrice, marketPrice, productionDate, expiryDate, discountId, quantityInStock, storeId)
                    SELECT productName, supplierId, buyPrice, marketPrice, productionDate, expiryDate, discountId, %d, %d
                    FROM Product WHERE productId = %d AND storeId = %d LIMIT 1
                    """, transferQty, toStore, productID, fromStore));
            }

            conn.commit();
            System.out.println(" Product transferred successfully.");

        } catch (Throwable err) {
            System.out.println("Error occurred. Rolling back...");
            try {
                conn.rollback();
            } catch (Exception rollbackErr) {
                rollbackErr.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
                if (stmt != null) close(stmt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.print("Do you want to transfer another product? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

//----------------------------------Task 3. Maintaining billing and transaction records.--------------------------------------------------------------------

public static void maintainingBillsAndTransactionRecords() {
    boolean running = true;

    try {
        while (running) {
            System.out.println("\nBills & Transactions Menu:");
            System.out.println("1 : Generate Supplier Bills");
            System.out.println("2 : Make Customer Transactions");
            System.out.println("3 : Generate Reward Checks");
            System.out.println("0 : Back");
            System.out.print("Enter your choice: ");

            int option = sc.nextInt();

            switch (option) {
                case 1 -> generateSupplierBillBySupplierId();
                case 2 -> makeCustomerTransaction();
                case 3 -> generateRewardChecks();
                case 0 -> {
                    running = false;
                    System.out.println("Returning to main menu...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    } catch (GoToMainMenuException e) {
        System.out.println("Returning to main menu...");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
}
public static void generateSupplierBillBySupplierId() {
    boolean flag = true;

    while (flag) {
        try {
            stmt = conn.createStatement();

            System.out.print("Enter Supplier ID to view transactions: ");
            int supplierId = sc.nextInt();
            
            String txnQuery = "SELECT * FROM SupplierTransaction WHERE SupplierId = " + supplierId;
            ResultSet txnRs = stmt.executeQuery(txnQuery);

            if (!txnRs.isBeforeFirst()) {
                System.out.println("No transactions found for this supplier.");
                System.out.println("Do you want to generate bill for some other supplier? (y/n) ");
                char res = sc.next().charAt(0);
                if(res == 'n' || res == 'N') break;
                continue;
            }

            System.out.println("\n=== Transactions for Supplier ID: " + supplierId + " ===");
            List<Integer> transactionIds = new ArrayList<>();

            while (txnRs.next()) {
                int txnId = txnRs.getInt("TransactionId");
                int storeId = txnRs.getInt("StoreId");
                double total = txnRs.getDouble("TotalPrice");
                Date date = txnRs.getDate("PurchaseDate");

                System.out.printf("Transaction ID: %d | Store ID: %d | Total: $%.2f | Date: %s%n",
                        txnId, storeId, total, date);
                transactionIds.add(txnId);
            }

            System.out.print("\nEnter Transaction ID to view full bill: ");
            int selectedTxnId = sc.nextInt();

            if (!transactionIds.contains(selectedTxnId)) {
                System.out.println("Invalid transaction ID for this supplier.");
                continue;
            }

            ResultSet txnDetail = stmt.executeQuery("SELECT * FROM SupplierTransaction WHERE TransactionId = " + selectedTxnId);
            txnDetail.next();

            System.out.println("\n=== BILL DETAILS ===");
            System.out.printf("Transaction ID: %d | Store ID: %d | Date: %s%n",
                    selectedTxnId, txnDetail.getInt("StoreId"), txnDetail.getDate("PurchaseDate"));
            System.out.println("Products:");

            ResultSet cartRs = stmt.executeQuery("""
                SELECT P.productName, P.buyPrice, PC.quantity 
                FROM SupplierProductCart PC 
                JOIN Product P ON PC.productId = P.productId 
                WHERE PC.transactionId = """ + selectedTxnId);

            double grandTotal = 0.0;

            while (cartRs.next()) {
                String name = cartRs.getString("productName");
                double price = cartRs.getDouble("buyPrice");
                int qty = cartRs.getInt("quantity");
                double subtotal = price * qty;
                grandTotal += subtotal;

                System.out.printf(" - %s | Qty: %d | Unit Price: $%.2f | Subtotal: $%.2f%n",
                        name, qty, price, subtotal);
            }

            System.out.printf("Total Billed Amount: $%.2f%n", grandTotal);

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to view another supplier bill? [y/n] (Or press 'm' to return to main menu): ");
        char choice = sc.next().charAt(0);

        if (choice == 'y' || choice == 'Y') {
            flag = true;
        } else if (choice == 'm' || choice == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void makeCustomerTransaction() {
    boolean flag = true;

    while (flag) {
        try {
            System.out.print("Enter cashier ID: ");
            int cashierID = sc.nextInt();

            System.out.print("Enter customer ID: ");
            int customerID = sc.nextInt();

            System.out.print("Enter purchase date (YYYY-MM-DD): ");
            String purchaseDate = sc.next();

            stmt = conn.createStatement();

            int storeID = getValidForeignKeyId(stmt, "Store", "storeId");
            double totalPrice = 0.0;

            // Insert blank transaction (to be updated later)
            stmt.executeUpdate(String.format("""
                INSERT INTO CustomerTransaction (storeId, CustomerID, BillingStaffId, PurchaseDate, TotalPrice)
                VALUES (%d, %d, %d, '%s', %.2f)
                """, storeID, customerID, cashierID, purchaseDate, totalPrice));

            // Get the generated transaction ID
            ResultSet rsTxn = stmt.executeQuery(String.format("""
                SELECT TransactionId FROM CustomerTransaction
                WHERE storeId = %d AND CustomerID = %d AND BillingStaffId = %d AND PurchaseDate = '%s'
                """, storeID, customerID, cashierID, purchaseDate));

            int transactionID = rsTxn.next() ? rsTxn.getInt("TransactionId") : -1;
            if (transactionID == -1) {
                System.out.println("Transaction initialization failed.");
                continue;
            }

            boolean addingProducts = true;

            while (addingProducts) {
                System.out.print("Enter product ID: ");
                int productID = sc.nextInt();

                System.out.print("Enter quantity: ");
                int reqQty = sc.nextInt();

                ResultSet rsProduct = stmt.executeQuery(String.format("""
                    SELECT marketPrice, discountId, quantityInStock FROM Product
                    WHERE productId = %d AND storeId = %d
                    """, productID, storeID));

                if (!rsProduct.next()) {
                    System.out.println("Product not found in this store.");
                    continue;
                }

                double marketPrice = rsProduct.getDouble("marketPrice");
                int discountId = rsProduct.getInt("discountId");
                int availableQty = rsProduct.getInt("quantityInStock");

                if (reqQty > availableQty) {
                    System.out.println("Not enough quantity in stock.");
                    continue;
                }

                // Check discount
                double discountPercentage = 0.0;
                boolean discountApplied = false;

                if (discountId != 0) {
                    ResultSet rsDiscount = stmt.executeQuery(String.format("""
                        SELECT discountPercentage FROM Discount
                        WHERE discountId = %d AND '%s' BETWEEN saleStartDate AND saleEndDate
                        """, discountId, purchaseDate));

                    if (rsDiscount.next()) {
                        discountPercentage = rsDiscount.getDouble("discountPercentage");
                        discountApplied = true;
                    }
                }

                double baseTotal = marketPrice * reqQty;
                double finalPrice = discountApplied ? baseTotal * (1 - discountPercentage / 100.0) : baseTotal;
                totalPrice += finalPrice;

                // Add to ProductCart
                stmt.executeUpdate(String.format("""
                    INSERT INTO ProductCart (transactionId, productId, quantity, discountId, totalPrice)
                    VALUES (%d, %d, %d, %s, %.2f)
                    """, transactionID, productID, reqQty,
                        (discountApplied ? String.valueOf(discountId) : "NULL"), finalPrice));

                // Update inventory
                stmt.executeUpdate(String.format("""
                    UPDATE Product SET quantityInStock = quantityInStock - %d
                    WHERE productId = %d AND storeId = %d
                    """, reqQty, productID, storeID));

                System.out.print("Add another product? (y/n): ");
                char more = sc.next().charAt(0);
                addingProducts = (more == 'y' || more == 'Y');
            }

            // Update final total
            stmt.executeUpdate(String.format("""
                UPDATE CustomerTransaction SET TotalPrice = %.2f WHERE TransactionId = %d
                """, totalPrice, transactionID));

            System.out.printf("Transaction complete. Total price: $%.2f%n", totalPrice);

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        System.out.print("Do you want to make another transaction? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void generateRewardChecks() {
    boolean flag = true;

    while (flag) {
        System.out.println("\nGenerate Reward Checks");
        double rewardPercentage = 0.02;

        System.out.println("Reward Percentage: " + (rewardPercentage * 100) + "% (default for all Platinum Customers)");
        System.out.print("Enter the year for which to generate reward checks: ");
        int year = sc.nextInt();

        System.out.println("Reward Checks Option Menu:");
        System.out.println("1 - Generate reward check for a particular customer");
        System.out.println("2 - Generate reward check for all platinum customers");
        System.out.print("Enter your choice: ");
        int option = sc.nextInt();

        HashMap<Integer, Double> rewardsMap = new HashMap<>();

        switch (option) {
            case 1 -> {
                System.out.print("Enter customer ID: ");
                int customerID = sc.nextInt();

                try {
                    stmt = conn.createStatement();

                    // Check if customer is platinum and active
                    ResultSet rs = stmt.executeQuery("SELECT membershipLevel, ActiveStatus FROM Customer WHERE customerID = " + customerID);
                    if (rs.next()) {
                        String level = rs.getString("membershipLevel");
                        String active = rs.getString("ActiveStatus");

                        if (level.equalsIgnoreCase("Platinum") && active.equals("1")) {
                            rs = stmt.executeQuery(String.format("""
                                SELECT CustomerID, SUM(TotalPrice * %.2f) AS REWARDS
                                FROM CustomerTransaction
                                WHERE CustomerID = %d AND YEAR(PurchaseDate) = %d
                                GROUP BY CustomerID
                                """, rewardPercentage, customerID, year));

                            if (rs.next()) {
                                rewardsMap.put(rs.getInt("CustomerID"), rs.getDouble("REWARDS"));
                            }

                            displayRewards(rewardsMap);
                        } else {
                            System.out.println("❌ Cannot generate reward checks for non-platinum or inactive customers.");
                        }
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                } finally {
                   //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
                     
                }
            }

            case 2 -> {
                try {
                    stmt = conn.createStatement();

                    // Get all platinum & active customers
                    ResultSet rs = stmt.executeQuery("SELECT customerID FROM Customer WHERE membershipLevel = 'Platinum' AND ActiveStatus = 1");

                    ArrayList<Integer> platinumIDs = new ArrayList<>();
                    while (rs.next()) {
                        platinumIDs.add(rs.getInt("customerID"));
                    }

                    for (int customerID : platinumIDs) {
                        rs = stmt.executeQuery(String.format("""
                            SELECT CustomerID, SUM(TotalPrice * %.2f) AS REWARDS
                            FROM CustomerTransaction
                            WHERE CustomerID = %d AND YEAR(PurchaseDate) = %d
                            GROUP BY CustomerID
                            """, rewardPercentage, customerID, year));
                        
                        if (rs.next()) {
                            rewardsMap.put(rs.getInt("CustomerID"), rs.getDouble(("REWARDS")));
                        }
                    }

                    displayRewards(rewardsMap);

                }  catch (Exception e) {
                    e.printStackTrace();
                } finally {
                   //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
                     
                }
            }

            default -> System.out.println("Invalid choice.");
        }

        System.out.print("Do you want to generate more reward checks? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

public static void displayRewards(HashMap<Integer, Double> rewardsMap) {
    boolean flag = true;

    while (flag) {
        // Check if there are any rewards to display
        if (rewardsMap == null || rewardsMap.isEmpty()) {
            System.out.println("No rewards to display.");
        } else {
            // Print header for the rewards display
            System.out.println("\n=== Reward Checks ===");
            System.out.printf("%-15s %-15s%n", "Customer ID", "Reward Amount");
            System.out.println("-----------------------------------");

            // Display each reward entry
            for (Map.Entry<Integer, Double> entry : rewardsMap.entrySet()) {
                int customerId = entry.getKey();
                double rewardAmount = entry.getValue();
                System.out.printf("%-15d $%-14.2f%n", customerId, (double) rewardAmount);
            }
        }

        // Prompt for next action
        System.out.print("Do you want to display rewards again? [y/n] (Or press 'm' to return to main menu): ");
        char c = sc.next().charAt(0);

        if (c == 'y' || c == 'Y') {
            flag = true;
        } else if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

//----------------------------------Task 4. Reports--------------------------------------------------------------------
public static void reports() {
    boolean running = true;

    try {
        while (running) {
            System.out.println("\nReports Menu:");
            System.out.println("1: Get Total Sales report by day, by month, or by year");
            System.out.println("2: Get Sales Growth between two dates");
            System.out.println("3: Get Stock Quantity for a particular merchandise");
            System.out.println("4: Get Customer Growth by Year or Month");
            System.out.println("5: Get Customer Activity between two dates");
            System.out.println("0: Back");

            System.out.print("Please enter the input from the menu: ");
            int option = sc.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.print("Enter the period type (day / month / year): ");
                    String periodType = sc.next().toLowerCase();
                    switch (periodType) {
                        case "day" -> {
                            System.out.print("Enter the date (YYYY-MM-DD): ");
                            getSalesByPeriod("day", sc.next(), null);
                        }
                        case "month" -> {
                            System.out.print("Enter the month (1-12): ");
                            String month = sc.next();
                            System.out.print("Enter the year (YYYY): ");
                            getSalesByPeriod("month", month, sc.next());
                        }
                        case "year" -> {
                            System.out.print("Enter the year (YYYY): ");
                            getSalesByPeriod("year", sc.next(), null);
                        }
                        default -> System.out.println("Invalid period type. Please enter 'day', 'month', or 'year'.");
                    }
                }
                case 2 -> getSalesGrowth();
                case 3 -> {
                    System.out.println("What kind of merchandise report do you want?\n1 : By productID and storeID\n2 : By productID\n3 : By storeID");
                    int sub = sc.nextInt();
                    switch (sub) {
                        case 1 -> {
                            System.out.print("Enter the storeID: ");
                            int storeID = sc.nextInt();
                            System.out.print("Enter the productID: ");
                            int productID = sc.nextInt();
                            getMerchandiseStockReport(1, storeID, productID);
                        }
                        case 2 -> {
                            System.out.print("Enter the productID: ");
                            getMerchandiseStockReport(2, sc.nextInt(), 0);
                        }
                        case 3 -> {
                            System.out.print("Enter the storeID: ");
                            getMerchandiseStockReport(3, sc.nextInt(), 0);
                        }
                        default -> System.out.println("Invalid input.");
                    }
                }
                case 4 -> {
                    System.out.println("1. Get customer growth by year\n2 : Get customer growth by month");
                    int sub = sc.nextInt();
                    switch (sub) {
                        case 1 -> getCustomerGrowthbyYear();
                        case 2 -> getCustomerGrowthByMonth();
                        default -> System.out.println("Wrong input!!! Try again!!!");
                    }
                }
                case 5 -> {
                    System.out.print("Enter the customerID: ");
                    int customerID = sc.nextInt();
                    System.out.print("Enter the start date: ");
                    String start = sc.next();
                    System.out.print("Enter the end date: ");
                    getCustomerActivity(customerID, start, sc.next());
                }
                case 0 -> {
                    running = false;
                    System.out.println("Returning to main menu...");
                }
                default -> System.out.println("Please enter a valid input!!!!");
            }
        }
    } catch (GoToMainMenuException e) {
        System.out.println("Returning to main menu...");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
}//General reports such as total sales report by day, by month, or by year. - done
public static void getSalesByPeriod(String type, String value1, String value2) {
    boolean flag = true;

    while (flag) {
        System.out.println("\nGetting total sales based on the selected period...\n");

        String query;
        switch (type.toLowerCase()) {
            case "day":
                query = "SELECT SUM(TotalPrice) AS Total_Sales FROM CustomerTransaction WHERE PurchaseDate = '" + value1 + "'";
                break;
            case "month":
                query = "SELECT SUM(TotalPrice) AS Total_Sales FROM CustomerTransaction WHERE MONTH(PurchaseDate) = " + value1 +
                        " AND YEAR(PurchaseDate) = " + value2;
                break;
            case "year":
                query = "SELECT SUM(TotalPrice) AS Total_Sales FROM CustomerTransaction WHERE YEAR(PurchaseDate) = " + value1;
                break;
            default:
                System.out.println("Invalid type. Please enter 'day', 'month', or 'year'.");
                return;
        }

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            double totalSales = (rs.next() && !rs.wasNull()) ? rs.getDouble("Total_Sales") : 0;
            System.out.println("The total sales for the selected " + type + " is: $" + totalSales);

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }
        // Prompt for next action
        System.out.print("Press 'm' to return to main menu : ");
        char c = sc.next().charAt(0);

         if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
 //Sales growth report for a specific store for a given time period. - done
 public static void getSalesGrowth() {
    boolean flag = true;

    while (flag) {
        System.out.print("Enter the storeID: ");
        int storeID = sc.nextInt();

        System.out.print("Enter the start date (YYYY-MM-DD): ");
        String startDate = sc.next();

        System.out.print("Enter the end date (YYYY-MM-DD): ");
        String endDate = sc.next();

        System.out.println("\nGetting monthly sales report for the entered store between " + startDate + " and " + endDate + "...\n");

        try {
            stmt = conn.createStatement();

            // Validate store existence
            ResultSet storeCheck = stmt.executeQuery("SELECT * FROM Store WHERE storeID = " + storeID);
            if (!storeCheck.next()) {
                System.out.println("Invalid storeID entered. No such store found.");
            } else {
                String query = "SELECT YEAR(PurchaseDate) AS year, MONTH(PurchaseDate) AS month, " +
                               "SUM(TotalPrice) AS total_sales " +
                               "FROM CustomerTransaction " +
                               "WHERE storeID = " + storeID +
                               " AND PurchaseDate BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                               "GROUP BY YEAR(PurchaseDate), MONTH(PurchaseDate) " +
                               "ORDER BY YEAR(PurchaseDate), MONTH(PurchaseDate)";

                ResultSet rs = stmt.executeQuery(query);

                boolean hasData = false;
                double prevSales = -1;

                System.out.println("Year | Month | Total Sales ($) | Growth %");
                System.out.println("-----------------------------------------------");

                while (rs.next()) {
                    hasData = true;
                    int year = rs.getInt("year");
                    int month = rs.getInt("month");
                    double sales = rs.getDouble("total_sales");

                    String growth = (prevSales >= 0)
                            ? String.format("%.2f%%", ((sales - prevSales) / prevSales) * 100)
                            : "--";

                    System.out.printf("%4d | %5d | %15.2f | %s\n", year, month, sales, growth);
                    prevSales = sales;
                }

                if (!hasData)
                    System.out.println("No sales data found for the given store and date range.");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        // Prompt for next action
        System.out.print("Press 'm' to return to main menu : ");
        char c = sc.next().charAt(0);

         if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
//Merchandise stock report for each store or for a certain product.  - done
public static void getMerchandiseStockReport(int mode, int id1, int id2) {
    boolean flag = true;

    while (flag) {
        try {
            stmt = conn.createStatement();
            ResultSet rs;
            Map<Integer, Integer> stockMap = new HashMap<>();

            switch (mode) {
                case 1: // by productID and storeID
                    System.out.println("\nGetting stock for storeID " + id1 + " and productID " + id2 + "...");
                    rs = stmt.executeQuery("SELECT SUM(quantityInStock) AS Total_Stock FROM Product WHERE productId = " + id2 + " AND storeId = " + id1);
                    if (rs.next()) {
                        int total = rs.getInt("Total_Stock");
                        System.out.println("Total stock: " + (rs.wasNull() ? "Not found or zero" : total));
                    }
                    break;

                case 2: // by productID
                    System.out.println("\nGetting stock for productID " + id1 + " across stores...");
                    rs = stmt.executeQuery("SELECT storeId, quantityInStock FROM Product WHERE productId = " + id1);
                    while (rs.next()) stockMap.put(rs.getInt("storeId"), rs.getInt("quantityInStock"));

                    if (stockMap.isEmpty()) {
                        System.out.println("No stock found for productID.");
                    } else {
                        System.out.println("storeID -> Quantity");
                        stockMap.forEach((store, qty) -> System.out.println(store + " -> " + qty));
                    }
                    break;

                case 3: // by storeID
                    System.out.println("\nGetting stock for storeID " + id1 + " across products...");
                    rs = stmt.executeQuery("SELECT productId, quantityInStock FROM Product WHERE storeId = " + id1);
                    while (rs.next()) stockMap.put(rs.getInt("productId"), rs.getInt("quantityInStock"));

                    if (stockMap.isEmpty()) {
                        System.out.println("No stock found for storeID.");
                    } else {
                        System.out.println("productID -> Quantity");
                        stockMap.forEach((product, qty) -> System.out.println(product + " -> " + qty + " items"));
                    }
                    break;

                default:
                    System.out.println("Invalid mode selected.");
                    return;
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        // Prompt for next action
        System.out.print("Press 'm' to return to main menu : ");
        char c = sc.next().charAt(0);

         if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
//Customer growth report by month or by year. -- done
public static void getCustomerGrowthByMonth() {
    boolean flag = true;

    while (flag) {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = sc.next();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = sc.next();

        System.out.println("\nGetting monthly customer growth report between " + startDate + " and " + endDate + "...\n");

        try {
            stmt = conn.createStatement();

            String query = "SELECT YEAR(memberShipStartDate) AS year, MONTH(memberShipStartDate) AS month, " +
                           "COUNT(customerID) AS total_enrolls " +
                           "FROM Customer " +
                           "WHERE memberShipStartDate BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                           "GROUP BY YEAR(memberShipStartDate), MONTH(memberShipStartDate) " +
                           "ORDER BY YEAR(memberShipStartDate), MONTH(memberShipStartDate)";

            ResultSet rs = stmt.executeQuery(query);

            boolean dataExists = false;
            int lastEnrolls = -1;

            System.out.println("Year | Month | New Customers | Growth %");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                dataExists = true;
                int year = rs.getInt("year");
                int month = rs.getInt("month");
                int totalEnrolls = rs.getInt("total_enrolls");

                String growthStr = "--";
                if (lastEnrolls != -1 && lastEnrolls != 0) {
                    double growth = ((double)(totalEnrolls - lastEnrolls) / lastEnrolls) * 100;
                    growthStr = String.format("%.2f%%", growth);
                }

                System.out.printf("%4d | %5d | %14d | %s\n", year, month, totalEnrolls, growthStr);
                lastEnrolls = totalEnrolls;
            }

            if (!dataExists) {
                System.out.println("No customer signups found between the provided dates.");
            }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        // Prompt for next action
        System.out.print("Press 'm' to return to main menu : ");
        char c = sc.next().charAt(0);

         if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
public static void getCustomerGrowthbyYear() {
    boolean flag = true;

    while (flag) {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = sc.next();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = sc.next();

        System.out.println("\nGetting yearly customer growth report between " + startDate + " and " + endDate + "...\n");

        try {
            stmt = conn.createStatement();

            String query = "SELECT YEAR(memberShipStartDate) AS year, " +
                           "COUNT(customerID) AS total_enrolls " +
                           "FROM Customer " +
                           "WHERE memberShipStartDate BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                           "GROUP BY YEAR(memberShipStartDate) " +
                           "ORDER BY YEAR(memberShipStartDate)";

            ResultSet rs = stmt.executeQuery(query);

            boolean dataExists = false;
            int lastEnrolls = -1;

            System.out.println("Year | New Customers | Growth %");
            System.out.println("--------------------------------------");

            while (rs.next()) {
                dataExists = true;
                int year = rs.getInt("year");
                int totalEnrolls = rs.getInt("total_enrolls");

                String growthStr = "--";
                if (lastEnrolls != -1 && lastEnrolls != 0) {
                    double growth = ((double)(totalEnrolls - lastEnrolls) / lastEnrolls) * 100;
                    growthStr = String.format("%.2f%%", growth);
                }

                System.out.printf("%4d | %14d | %s\n", year, totalEnrolls, growthStr);
                lastEnrolls = totalEnrolls;
            }

            if (!dataExists) {
                System.out.println("No customer signups found between the provided dates.");
            }


           
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }

        // Prompt for next action
        System.out.print("Press 'm' to return to main menu : ");
        char c = sc.next().charAt(0);

         if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}
//Customer activity report such as total purchase amount for a given time period. - done
public static void getCustomerActivity(int customerID, String startDate, String endDate) {
    boolean flag = true;

    while (flag) {
        System.out.println("\nGetting total purchase amount for a customer for a given time period.\n");

        try {
            stmt = conn.createStatement();

            String query = "SELECT SUM(TotalPrice) AS Total_Activity " +
                           "FROM CustomerTransaction " +
                           "WHERE CustomerID = " + customerID +
                           " AND PurchaseDate BETWEEN '" + startDate + "' AND '" + endDate + "'";

            ResultSet rs = stmt.executeQuery(query);

            double total_activity = 0;

            if (rs.next()) {
                total_activity = rs.getDouble("Total_Activity");
                if (rs.wasNull()) {
                    total_activity = 0;
                }
            }

            System.out.println("The total purchase for CustomerID " + customerID +
                               " between '" + startDate + "' and '" + endDate + "' is: $" + total_activity);

            
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
           //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
             
        }
     // Prompt for next action
        System.out.print("Press 'm' to return to main menu : ");
        char c = sc.next().charAt(0);

         if (c == 'm' || c == 'M') {
            throw new GoToMainMenuException();
        } else {
            flag = false;
        }
    }
}

//----------------------------------Task 5. Display--------------------------------------------------------------------
// Displays all rows from a user-specified table in the database
public static void display() {
    System.out.print("Enter the table name you want to see: ");
    String table = sc.next();

    try {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        // Print column names
        for (int i = 1; i <= colCount; i++)
            System.out.print((i > 1 ? " | " : "") + meta.getColumnName(i));
        System.out.println();

        // Print rows
        while (rs.next()) {
            for (int i = 1; i <= colCount; i++)
                System.out.print((i > 1 ? " | " : "") + rs.getString(i));
            System.out.println();
        }

    }  catch (Exception e) {
        e.printStackTrace();
    } finally {
       //try { if (stmt != null) close(stmt); } catch (Exception e) {} 
         
    }
}
}