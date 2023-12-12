import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ManageCustomer {
    public void viewCustomer() {
        Scanner scanner = new Scanner(System.in);
        char choice;
        do {
            Customer customer = Customer.getCustomer();
            if (customer != null) {
                Customer.printCustomerHeader();
                System.out.println("\t" + customer.getId() + "\t\t|\t\t" + customer.getName() + "\t\t|\t" +
                        customer.getPhone() + "\t\t|\t" + customer.getEmail() + "\t|\t\t" + customer.getGender() +
                        "\t|\t" + customer.getDateOfBirth() + "\t\t|\t\t" + customer.getAddress() + "\t|\t\t" +
                        customer.getCity() + "\t|\t\t" + customer.getProvince() + "\t\t|\t\t" + customer.getZip());
                System.out.println();
            } else {
                System.out.println("Customer not found");
            }
            System.out.print("Do you want to view another item? (y/n)");
            choice = scanner.next().trim().charAt(0);
        } while (choice == 'y' || choice == 'Y');
    }
    public void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        char choice;
        do {
            Customer customer;
            System.out.println("Enter customer details: ");
            customer = createBasicCustomer();
            System.out.print("Email : ");
            customer.setEmail(scanner.next().trim());
            System.out.print("Date of Birth : ");
            String dateString = scanner.next().trim();
            try {
                customer.setDateOfBirth(Date.valueOf(dateString));
            } catch (Exception e) {
                System.out.println("Invalid date format");
            }
            System.out.print("Address : ");
            customer.setAddress(scanner.next().trim());
            scanner = new Scanner(System.in);
            System.out.print("City : ");
            customer.setCity(scanner.next().trim());
            System.out.print("Province : ");
            customer.setProvince(scanner.next().trim());
            System.out.print("Zip : ");
            customer.setZip(scanner.next().trim());
            customer.saveCustomer();
            System.out.print("Do you want to view another item? (y/n)");
            choice = scanner.next().trim().charAt(0);
        } while (choice == 'y' || choice == 'Y');
    }

    private Customer createBasicCustomer() {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
        System.out.print("Name : ");
        customer.setName(scanner.next().trim());
        System.out.print("Phone : ");
        customer.setPhone(scanner.next().trim());
        System.out.print("Gender : ");
        customer.setGender(scanner.next().trim());
        if(customer.getName().isEmpty() || customer.getPhone().isEmpty() || customer.getGender().isEmpty()) {
            System.out.println("Customer Name,Phone number and Gender cannot be empty.");
            createBasicCustomer();
        }
        return customer;
    }

    public void updateCustomer() {
        Scanner scanner = new Scanner(System.in);
        char selection;
        Customer customer = Customer.getCustomer();
        do {
            if (customer != null) {
                Customer.printCustomerHeader();
                System.out.println("\t" + customer.getId() + "\t|\t" + customer.getName() + "\t\t|\t\t" + customer.getPhone() +
                        "\t\t|\t\t" + customer.getEmail() + "\t\t|\t\t" + customer.getGender() + "\t\t|\t" +
                        customer.getDateOfBirth() + "\t|\t" + customer.getAddress() + "\t\t\t|\t\t" + customer.getCity() +
                        "\t\t\t|\t\t" + customer.getProvince() + "\t\t\t|\t\t" + customer.getZip());
                System.out.println();

                System.out.print("Do you want edit this customer (Y/N) : ");
                char choice = scanner.next().charAt(0);
                if (choice == 'Y' || choice == 'y') {
                    scanner = new Scanner(System.in);
                    System.out.println("Just enter if you dont want to change the value");
                    System.out.print("Enter customer name : ");
                    String string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        customer.setName(string);
                    }
                    System.out.print("Enter customer phone number : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        customer.setPhone(string);
                    }
                    System.out.print("Enter customer email : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        customer.setEmail(string);
                    }
                    System.out.print("Enter customer gender : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        customer.setGender(string);
                    }
                    System.out.print("Enter customer date of birth : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        customer.setDateOfBirth(Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(string))));
                    }
                    System.out.print("Enter customer address : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        customer.setAddress(string);
                    }
                    System.out.print("Enter customer city : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        customer.setCity(string);
                    }
                    System.out.print("Enter customer province : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        customer.setProvince(string);
                    }
                    System.out.print("Enter customer zip code : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        customer.setZip(string);
                    }
                }
                customer.updateCustomer();
                System.out.println("Item successfully updated");
            } else {
                System.out.println("Customer not found");
            }
            System.out.print("Do you want edit another item (Y/N) : ");
            selection = scanner.next().charAt(0);
            if (selection == 'Y' || selection == 'y') {
                System.out.print("Enter item id to edit : ");
                customer = Customer.getCustomer();
            }
        } while (selection == 'Y' || selection == 'y');
    }
    public void removeCustomer() {
        Customer customer = Customer.getCustomer();
        if (customer != null) {
            Customer.deleteCustomer(customer.getId());
        } else {
            System.out.println("Customer not found");
        }
    }

    public void ViewCustomersBDayInWeek() {
        System.out.print("Enter Date Considering : ");
        Scanner scanner = new Scanner(System.in);
        String dateString = scanner.next().trim();
        Date bDay;
        try {
            bDay = Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(dateString)));
        } catch (Exception e) {
            System.out.println("Invalid date format. Taking current date.");
            bDay = Date.valueOf(LocalDate.from(LocalDate.now()));
        }
        Customer customer = new Customer();
        customer.setDateOfBirth(bDay);
        List<Customer> customers = customer.getCustomerWithBDay();
        if (customers != null) {
            Customer.printCustomerHeader();
            for (Customer c : customers) {
                System.out.println("\t" + c.getId() + "\t\t|\t\t" + c.getName() + "\t\t|\t" +
                        c.getPhone() + "\t\t|\t" + c.getEmail() + "\t|\t\t" + c.getGender() +
                        "\t|\t" + c.getDateOfBirth() + "\t\t|\t\t" + c.getAddress() + "\t|\t" +
                        c.getCity() + "\t|\t\t" + c.getProvince() + "\t\t|\t\t" + c.getZip());
            }
            System.out.println();
        } else {
            System.out.println("Customer not found");
        }
    }
}
