import java.util.Scanner;

public class AppInterface {
    public void mainMenu() {
        int choice;
        while (true) {
            System.out.println("==========================");
            System.out.println("Main Menu");
            System.out.println("==========================");
            System.out.println("""
                    1) Generate Invoice
                    2) Manage Items
                    3) Manage Customers
                    4) Manage invoices
                    5) Admin Tasks
                    9) Exit""");
            System.out.println("==========================\n");
            System.out.print("Enter Your Choice : ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Generate Invoice");
                    ManageInvoice manageInvoice = new ManageInvoice();
                    manageInvoice.generateInvoice();
                    break;
                case 2:
                    System.out.println("Manage Items");
                    manageItems();
                    break;
                case 3:
                    System.out.println("Manage Customers");
                    manageCustomers();
                    break;
                case 4:
                    System.out.println("Manage invoices");
                    manageInvoices();
                    break;
                case 5:
                    System.out.println("Admin Tasks");
                    adminTasks();
                    break;
                case 9:
                    System.out.println("Exiting...\nThank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void manageItems() {
        ManageItem manageItem = new ManageItem();
        int choice = 0;
        while (choice != 5) {
            System.out.println("""
                ==========================
                Manage Items
                ==========================""");
            System.out.println("""
                    1) View Item
                    2) Add Item
                    3) Edit Item
                    4) Delete Item
                    5) Previous Menu
                    9) Exit""");
            System.out.print("Enter Your Choice : ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            System.out.println(choice);
            switch (choice) {
                case 1:
                    System.out.println("View Item");
                    viewItem();
                    break;
                case 2:
                    System.out.println("Add Item");
                    manageItem.addItem();
                    break;
                case 3:
                    System.out.println("Update Item");
                    manageItem.updateItem();
                    break;
                case 4:
                    System.out.println("Delete Item");
                    manageItem.deleteItem();
                    break;
                case 5:
                    System.out.println("Previous Menu");
                    break;
                case 9:
                    System.out.println("Exiting...\nThank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void manageCustomers() {
        ManageCustomer manageCustomer = new ManageCustomer();
        int choice = 0;
        while (choice != 5) {
            System.out.println("""
                ==========================
                Manage Customers
                ==========================""");
            System.out.println("""
                    1) View Customer
                    2) Add Customer
                    3) Edit Customer
                    4) Delete Customer
                    5) Previous Menu
                    9) Exit""");
            System.out.print("Enter Your Choice : ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            System.out.println(choice);
            switch (choice) {
                case 1:
                    System.out.println("View Customer");
                    manageCustomer.viewCustomer();
                    break;
                case 2:
                    System.out.println("Add Customer");
                    manageCustomer.addCustomer();
                    break;
                case 3:
                    System.out.println("Update Customer");
                    manageCustomer.updateCustomer();
                    break;
                case 4:
                    System.out.println("Delete Customer");
                    manageCustomer.removeCustomer();
                    break;
                case 5:
                    System.out.println("Previous Menu");
                    break;
                case 9:
                    System.out.println("Exiting...\nThank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void manageInvoices() {
        int choice = 0;
        ManageInvoice manageInvoice = new ManageInvoice();
        while (choice != 8) {
            System.out.println("""
                ==========================
                Manage Invoice
                ==========================""");
            System.out.println("""
                    1) View Invoice
                    2) Add Invoice
                    3) Edit Invoice
                    4) Delete Invoice
                    5) Previous Menu
                    9) Exit""");
            System.out.print("Enter Your Choice : ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            System.out.println(choice);
            switch (choice) {
                case 1:
                    System.out.println("Add Invoice");
                    manageInvoice.generateInvoice();
                    break;
                case 2:
                    System.out.println("View Invoice by Id");
                    manageInvoice.viewInvoiceById();
                    break;
                case 3:
                    System.out.println("View Invoices of a Customer");
                    manageInvoice.viewInvoiceByCustomer();
                    break;
                case 4:
                    System.out.println("View Invoices of Given Period");
                    manageInvoice.viewInvoicesGivenPeriod();
                    break;
                case 5:
                    System.out.println("Update Invoice");
                    manageInvoice.updateInvoice();
                    break;
                case 6:
                    System.out.println("Delete Invoice");
                    manageInvoice.deleteInvoice();
                    break;
                case 8:
                    System.out.println("Previous Menu");
                    break;
                case 9:
                    System.out.println("Exiting...\nThank you!");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void adminTasks() {
        ManageInvoice manageInvoice = new ManageInvoice();
        int choice = 0;
        while (choice != 8) {
            System.out.println("""
                ==========================
                Admin Tasks
                ==========================""");
            System.out.println("""
                    1) View Customers with Birthday with in the Week
                    2) View Stock
                    3) Vied Items Below Minimum Quantity
                    4) View Expired Items from Date
                    5) Day Sales
                    6) Monthly Sales
                    8) Previous Menu
                    9) Exit""");
            System.out.print("Enter Your Choice : ");
            Scanner scanner = new Scanner(System.in);
            ManageCustomer manageCustomer = new ManageCustomer();
            ManageItem manageItem = new ManageItem();
            choice = scanner.nextInt();
            System.out.println(choice);
            switch (choice) {
                case 1:
                    System.out.println("View Customers with Birthday with in the Week");
                    manageCustomer.ViewCustomersBDayInWeek();
                    break;
                case 2:
                    System.out.println("View Stock");
                    manageItem.viewAllItems();
                    break;
                case 3:
                    System.out.println("Vied Items Below Minimum Quantity");
                    break;
                case 4:
                    System.out.println("View Expired Items from Date");
                    manageItem.expiredItemsToDate();
                    break;
                case 5:
                    System.out.println("Day Sales");
                    manageInvoice.daySales();
                    break;
                case 6:
                    System.out.println("Monthly Sale");
                    manageInvoice.monthlySales();
                    break;
                case 7:
                    System.out.println("Sale of Given Period");
                    manageInvoice.salesOfGivenPeriod();
                    break;
                case 8:
                    System.out.println("Previous Menu");
                    break;
                case 9:
                    System.out.println("Exiting...\nThank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void viewItem() {
        ManageItem manageItem = new ManageItem();
        int choice = 0;
        while (choice != 5) {
            System.out.println("""
                ==========================
                View Items
                ==========================""");
            System.out.println("""
                    1) View Item by Id
                    2) View Expired Items on Date
                    3) View All Items
                    4) View Items With Less Than Min Quantity
                    5) Previous Menu
                    9) Exit""");
            System.out.print("Enter Your Choice : ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            System.out.println(choice);
            switch (choice) {
                case 1:
                    System.out.println("View Item by Id");
                    manageItem.viewItem();
                    break;
                case 2:
                    System.out.println("View Expired Items on Date");
                    manageItem.viewExpiredItemsOnDate();
                    break;
                case 3:
                    System.out.println("View All Items");
                    manageItem.viewAllItems();
                    break;
                case 4:
                    System.out.println("View Items With Less Than Min Quantity");
                    System.out.println("Not implemented yet.");
                    //manageItems.itemsWithLessThanMinQuantity();
                    break;
                case 5:
                    System.out.println("Previous Menu");
                    break;
                case 9:
                    System.out.println("Exiting...\nThank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
