import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManageInvoice {
    Invoice invoice;
    InvoiceItem invoiceItem;

    public void generateInvoice() {
        invoice = new Invoice();
        Scanner scanner = new Scanner(System.in);

        Customer customer = Customer.getCustomer();
        invoice.setCustomerId(customer.getId());
        invoice.setTimeStamp(Timestamp.from(Instant.now()));
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        int invoiceId = Invoice.getLatestInvoiceId() + 1;

        char choice = 'Y';
        while (choice == 'y' || choice == 'Y') {
            //Create a new invoice item object
            InvoiceItem newItem = addInvoiceItem(invoiceId);
            //If invoiceItems list is empty no need to check for duplicated items.
            if (!invoiceItems.isEmpty()) {
                ListIterator<InvoiceItem> iterator = invoiceItems.listIterator();
                while (iterator.hasNext()) {
                    InvoiceItem invoiceItem = iterator.next();
                    if (invoiceItem.getItemId() != newItem.getItemId()) {
                        iterator.add(newItem);
                    } else {
                        System.out.println("Duplicated Item " + newItem.getItemId() + " found. " + "Adding quantities.");
                        invoiceItems.remove(invoiceItem);
                        invoiceItem.setQuantity(invoiceItem.getQuantity() + newItem.getQuantity());
                        if(validateItemToSave(invoiceItem)) {
                            iterator.add(invoiceItem);
                        }
                    }
                }
            } else {
                invoiceItems.add(newItem);
            }

            System.out.print("Do you want to add more items (Y) or N : ");
            choice = scanner.next().charAt(0);
        }

        for (InvoiceItem invoiceItem : invoiceItems) {
            invoiceItem.save();
            Item.reduceItemCount(invoiceItem.getItemId(), invoiceItem.getQuantity());
        }
        invoice.save();
    }

    private InvoiceItem addInvoiceItem(int invoiceId) {
        invoiceItem = new InvoiceItem();
        Scanner scanner = new Scanner(System.in);

        invoiceItem.setInvoiceId(invoiceId);
        System.out.print("Enter Item Id : ");
        invoiceItem.setItemId(scanner.nextInt());
        System.out.print("Enter item quantity : ");
        invoiceItem.setQuantity(scanner.nextFloat());
        System.out.print("Enter item discount : ");
        invoiceItem.setDiscount(scanner.nextFloat());
        if(!validateItemToSave(invoiceItem)) {
            addInvoiceItem(invoiceId);
        }

        return invoiceItem;
    }

    private boolean validateItemToSave(InvoiceItem invoiceItem) {
        boolean valid = true;
        float quantity = Item.getItemQuantityById(invoiceItem.getItemId(), invoiceItem.getQuantity());
        if(quantity <= 0) {
            System.out.println("Invalid item Id");
            valid = false;
        } else if (quantity - invoiceItem.getQuantity() < 0) {
            System.out.println("Insufficient item quantity in stock. Available only " + quantity);
            valid = false;
        }
        if(invoiceItem.getQuantity() == 0) {
            System.out.println("Invalid quantity");
            valid = false;
        }
        if(invoiceItem.getDiscount() < 0) {
            System.out.println("Invalid discount");
            valid = false;
        }

        return valid;
    }

    public void viewInvoiceById() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Invoice Id : ");
        int id = scanner.nextInt();
        Invoice invoice = Invoice.getInvoiceById(id);
        if (invoice == null) {
            System.out.println("Invoice not found.");
            viewInvoiceById();
        } else {
            List<InvoiceItem> invoiceItems = InvoiceItem.getInvoiceItemsByInvoiceId(id);
            Customer customer = Customer.getCustomerById(invoice.getCustomerId());
            System.out.println();
            System.out.println("Invoice Id : " + invoice.getId() + "\t\tCustomer Name : " + customer.getName() +
                    "\t\tInvoice Date - Time : " + invoice.getTimeStamp());
            System.out.println("-----------------------------------------------------------------------------------------" +
                    "-------------------------------------------");
            System.out.println("Item Id\t|\tItem Name\t|\tQuantity\t|\tUnit Price\t|\tDiscount(%)\t|\t" +
                    "Sub Total\t|\tDescription");
            if (invoiceItems != null && !invoiceItems.isEmpty()) {
                for(InvoiceItem invoiceItem :invoiceItems) {
                    Item item = Item.getItemById(invoiceItem.getItemId());
                    System.out.println("\t" + item.getId() + "\t|\t" + item.getName() + "\t\t|\t\t" + item.getQuantity() +
                            "\t\t|\t\t" + item.getSellingPrice() + "\t|\t\t" + invoiceItem.getDiscount() + "\t\t|\t" +
                            item.getSellingPrice() * item.getQuantity() * (1 - invoiceItem.getDiscount()/100)  +
                            "\t\t|\t\t" + item.getDescription());
                }
            } else {
                System.out.println("Item not found");
            }
            System.out.println();
        }
    }

    public void updateInvoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Invoice id to edit :");
        int id = scanner.nextInt();
        char selection;
        do {
            Item item = Item.getItemById(id);
            Invoice invoice = Invoice.getInvoiceById(id);
            if (invoice != null) {
                viewInvoice(invoice);
                System.out.print("Do you want edit this Invoice (Y) or N : ");
                char choice = scanner.next().charAt(0);
                if (choice == 'Y' || choice == 'y') {
                    scanner = new Scanner(System.in);
                    System.out.println("Just enter if you dont want to change the value");
                    System.out.print("Enter item name : ");
                    String string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        item.setName(string);
                    }
                    System.out.print("Enter item purchased price : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        item.setPurchasedPrice(Float.parseFloat(string));
                    }
                    System.out.print("Enter item selling price : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        item.setSellingPrice(Float.parseFloat(string));
                    }
                    System.out.print("Enter item quantity : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty()) {
                        item.setQuantity(Integer.parseInt(string));
                    }
                    System.out.print("Enter item expiry date : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        item.setExpiryDate(Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(string))));
                    }
                    System.out.print("Enter item manufacture date : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        item.setManufactureDate(Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(string))));
                    }
                    System.out.print("Enter item description : ");
                    string = scanner.nextLine();
                    if(!string.isEmpty() || !string.isBlank()) {
                        item.setDescription(string);
                    }
                }
                item.updateItem();
                System.out.println("Item successfully updated");
            } else {
                System.out.println("Item with id " + id + " not found");
            }
            System.out.print("Do you want edit another item (Y/N) : ");
            selection = scanner.next().charAt(0);
            if (selection == 'Y' || selection == 'y') {
                System.out.print("Enter item id to edit : ");
                id = scanner.nextInt();
            }
        } while (selection == 'Y' || selection == 'y');
    }

    public void viewInvoiceByCustomer() {
        Customer customer = Customer.getCustomer();
        List<Invoice> invoices = Invoice.getInvoiceByCustomer(customer.getId());
        if (invoices != null && !invoices.isEmpty()) {
            viewInvoice(invoices);
        }
    }

    public void daySales() {
        List<Invoice> invoices = Invoice.getInvoicesToDay();
        if (!invoices.isEmpty()) {
            viewSales(invoices);
        } else {
            System.out.println("No invoices found on " + LocalDate.now());
        }
    }

    private void viewSales(List<Invoice> invoices) {
        float totalEarning = 0;
        float totalSellingPrice = 0;
        printViewSalesHeader();
        for (Invoice invoice : invoices) {
            List<InvoiceItem> invoiceItems = new InvoiceItem().getItemsByInvoiceId(invoice.getId());
            for (InvoiceItem invoiceItem : invoiceItems) {
                Item item = Item.getItemById(invoiceItem.getItemId());
                if (item == null) {
                    System.out.println("Item not found with id " + invoice.getId());
                    continue;
                }
                float totalSales = item.getSellingPrice() * invoiceItem.getQuantity();
                float totalPurchasedPrice = item.getPurchasedPrice() * invoiceItem.getQuantity();
                float earnings = (totalSales - totalPurchasedPrice - invoiceItem.getDiscount());
                totalEarning += earnings;
                totalSellingPrice += totalSales;
                System.out.println("\t" + invoice.getId() + "\t\t|\t" + invoiceItem.getItemId() + "\t\t|\t\t" +
                        item.getName() + "\t\t|\t\t" + invoiceItem.getQuantity() + "\t\t|\t\t" +
                        invoiceItem.getDiscount() + "\t\t|\t\t" + item.getPurchasedPrice() + "\t\t|\t\t" +
                        item.getSellingPrice() + "\t\t|\t\t\t" + totalPurchasedPrice + "\t\t\t|\t\t\t" +
                        totalSales +  "\t\t|\t" + earnings);
            }
        }
        System.out.println();
        System.out.println("______________________________________");
        System.out.println("Total sales : " + totalSellingPrice);
        System.out.println("Total Revenue : " + totalEarning);
        System.out.println("______________________________________");
        System.out.println();
        System.out.println();
    }

    private void printViewSalesHeader() {
        System.out.println();
        System.out.println("Invoice Id\t|\tItem Id\t|\tItem Name\t|\tQuantity\t|\tDiscount\t|\tPurchased Price\t|\tSelling Price\t|" +
                "\tTotal Purchased Price\t|\tTotal Selling Price\t|\tEarning\t");
        System.out.println("__________________________________________________________________________________________" +
                "______________________________________________________________________________________");
    }

    public void viewInvoicesGivenPeriod() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter starting date : ");
        String strStartDate = scanner.nextLine();
        System.out.print("Enter ending date : ");
        String strEndDate = scanner.nextLine();
        Date startDate = Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(strStartDate)));
        Date endDate = Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(strEndDate)));
        viewInvoicesGivenPeriod(startDate, endDate);
    }

    public void viewInvoicesGivenPeriod(Date startDate, Date endDate) {
        List<Invoice> invoices = Invoice.getInvoiceByPeriod(startDate, endDate);
        if (!invoices.isEmpty()) {
            viewInvoice(invoices);
        } else {
            if (startDate == endDate) {
                System.out.println("No invoices found on " + startDate);
            }else{
                System.out.println("No invoices found between " + startDate + " and " + endDate);
            }
        }
    }

    public void deleteInvoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Invoice id to edit :");
        int id = scanner.nextInt();
        Invoice invoice = Invoice.getInvoiceById(id);
        if (invoice != null) {
            Invoice.deleteInvoice(invoice.getId());
            System.out.println("Invoice successfully deleted");
        }
    }

    public void monthlySales() {
        List<Invoice> invoices = Invoice.getInvoicesOfMonth();
        if (!invoices.isEmpty()) {
            viewSales(invoices);
        } else {
            System.out.println("No invoices found.");
        }
    }

    private void viewInvoice(Invoice invoice) {
        viewInvoiceHeader();
        System.out.println("\t" + invoice.getId() + "\t\t|\t\t" + invoice.getCustomerId() + "\t\t|\t\t" +
                invoice.getTimeStamp() + "\t|\t\t" + invoice.getDescription());
        System.out.println();
    }

    private void viewInvoice(List<Invoice> invoices) {
        viewInvoiceHeader();
        for (Invoice invoice : invoices) {
            System.out.println("\t" + invoice.getId() + "\t\t|\t\t" + invoice.getCustomerId() + "\t\t|\t\t" +
                    invoice.getTimeStamp() + "\t|\t\t" + invoice.getDescription());
        }
        System.out.println();
    }

    public void viewInvoiceHeader() {
        System.out.println();
        System.out.println("Invoice Id\t|\tCustomer Id\t|\t\tInvoice Date/Time\t\t|\tDescription");
    }

    public void salesOfGivenPeriod() {
    }
}
