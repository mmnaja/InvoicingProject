import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ManageItem {
    public void viewItem() {
        char choice;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter item id to view :");
            int id = scanner.reset().nextInt();
            Item item = Item.getItemById(id);
            if (item != null) {
                Item.printItemHeading();
                System.out.println("\t" + item.getId() + "\t|\t" + item.getName() + "\t\t|\t\t" + item.getPurchasedPrice() +
                        "\t\t|\t\t" + item.getSellingPrice() + "\t\t|\t\t" + item.getQuantity() + "\t\t|\t" +
                        item.getExpiryDate() + "\t|\t" + item.getManufactureDate() + "\t\t\t|\t\t" + item.getDescription());
                System.out.println();
            } else {
                System.out.println("Item with id " + id + " not found");
            }
            System.out.print("Do you want to view another item? (y/n)");
            choice = scanner.next().charAt(0);
        } while (choice == 'y' || choice == 'Y');
    }

    public void viewExpiredItemsOnDate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expiry date (yyyy-mm-dd) :");
        String date = scanner.nextLine();
        date = date.isEmpty() ? LocalDate.now().toString() : date;
        List<Item> items = Item.getExpiredItems(Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(date))));
        if (!items.isEmpty()) {
            Item.printItemHeading();
            for (Item item : items) {
                System.out.println("\t" + item.getId() + "\t|\t" + item.getName() + "\t\t|\t\t" + item.getPurchasedPrice() +
                        "\t\t|\t\t" + item.getSellingPrice() + "\t\t|\t\t" + item.getQuantity() + "\t\t|\t" +
                        item.getExpiryDate() + "\t|\t" + item.getManufactureDate() + "\t\t\t|\t\t" + item.getDescription());
            }
            System.out.println();
        } else {
            System.out.println("Expired items not found to date " + date + ".");
        }
    }

    public void addItem() {
        Item item = new Item();
        char choice;
        do {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Ented item name : ");
            item.setName(scanner.next());
            System.out.print("Enter item purchased price : ");
            float buyPrice = scanner.nextFloat();
            item.setPurchasedPrice(buyPrice);
            System.out.print("Enter item selling price : ");
            float sellPrice = scanner.nextFloat();
            item.setSellingPrice(sellPrice);
            System.out.print("Enter item quantity : ");
            item.setQuantity(scanner.nextInt());
            System.out.print("Enter item expiry date (YYYY-MM-DD): ");
            String dateString = scanner.next();
            item.setExpiryDate(Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(dateString))));
            System.out.print("Enter item manufacture date (YYYY-MM-DD): ");
            dateString = scanner.next();
            item.setManufactureDate(Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(dateString))));
            scanner = new Scanner(System.in);
            System.out.print("Enter item description : ");
            item.setDescription(scanner.nextLine());
            item.saveItem();

            System.out.print("Do you want to add more items (Y/N) : ");
            choice = scanner.next().charAt(0);
        } while (choice == 'y' || choice == 'Y');
    }

    public void updateItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item id to edit :");
        int id = scanner.nextInt();
        char selection;
        do {
            Item item = Item.getItemById(id);
            if (item != null) {
                Item.printItemHeading();
                System.out.println("\t" + item.getId() + "\t|\t" + item.getName() + "\t\t|\t\t" + item.getPurchasedPrice() +
                        "\t\t|\t\t" + item.getSellingPrice() + "\t\t|\t\t" + item.getQuantity() + "\t\t|\t" +
                        item.getExpiryDate() + "\t|\t" + item.getManufactureDate() + "\t\t\t|\t\t" + item.getDescription());
                System.out.println();

                System.out.print("Do you want edit this item (Y/N) : ");
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

    public void deleteItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item id to delete :");
        int id = scanner.nextInt();
        char choice;
        do {
            if (Item.validateItemById(id)) {
                if (Item.deleteItem(id)) {
                    System.out.println("Item successfully deleted");
                } else {
                    System.out.println("Failed to delete the item");
                }
            } else {
                System.out.println("Item with id " + id + " not found");
            }

            System.out.print("Do you want edit another item (Y/N) : ");
            choice = scanner.next().charAt(0);
            System.out.println("1");
            if (choice == 'Y' || choice == 'y') {
                System.out.print("Enter item id : ");
                id = scanner.nextInt();
            }
            System.out.println("2");
        } while (choice == 'Y' || choice == 'y');
    }

    public void viewAllItems() {
        List<Item> items = Item.getAllItems();
        if (!items.isEmpty()) {
            Item.printItemHeading();
            for (Item item : items) {
                System.out.println("\t" + item.getId() + "\t|\t" + item.getName() + "\t\t|\t\t" + item.getPurchasedPrice() +
                        "\t\t|\t\t" + item.getSellingPrice() + "\t\t|\t\t" + item.getQuantity() + "\t\t|\t" +
                        item.getExpiryDate() + "\t|\t" + item.getManufactureDate() + "\t\t\t|\t\t" + item.getDescription());
            }
            System.out.println();
        } else {
            System.out.println("Items not found.");
        }
    }

    public void expiredItemsToDate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter date (YYYY-MM-DD) check expairy: ");
        String dateString = scanner.next();
        Date date = Date.valueOf(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(dateString)));

        List<Item> items = Item.getExpiredItems(date);
        if (!items.isEmpty()) {
            Item.printItemHeading();
            for (Item item : items) {
                System.out.println("\t" + item.getId() + "\t|\t" + item.getName() + "\t\t|\t\t" + item.getPurchasedPrice() +
                        "\t\t|\t\t" + item.getSellingPrice() + "\t\t|\t\t" + item.getQuantity() + "\t\t|\t" +
                        item.getExpiryDate() + "\t|\t" + item.getManufactureDate() + "\t\t\t|\t\t" + item.getDescription());
            }
            System.out.println();
        } else {
            System.out.println("Items not found.");
        }
    }
}
