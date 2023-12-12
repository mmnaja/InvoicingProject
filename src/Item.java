import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private int id;
    private String name;
    private float purchasedPrice;
    private float sellingPrice;
    private int quantity;
    private Date expiryDate;
    private Date manufactureDate;
    private String description;
    private static BasicIConnectionPool connectionPool;

    public Item() {}
    public Item(int id, String name, float purchasedPrice, float sellingPrice, int quantity, Date expiryDate, Date manufactureDate) {
        this.id = id;
        this.name = name;
        this.purchasedPrice = purchasedPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.manufactureDate = manufactureDate;
    }

    public Item(int id, String name, float purchasedPrice, float sellingPrice, int quantity, Date expiryDate, Date manufactureDate, String description) {
        this.id = id;
        this.name = name;
        this.purchasedPrice = purchasedPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.manufactureDate = manufactureDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(float purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public  void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private static BasicIConnectionPool getConnectionPool() {
        return new BasicIConnectionPool("jdbc:mysql://localhost:3306/invoice", "root", "");
    }

    public static float getItemQuantityById(int id, float quantity) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, quantity FROM item WHERE id=" + id);
            if (resultSet.next()) {
                float remainingQuantity = resultSet.getFloat("quantity");
                statement.close();
                connectionPool.releaseConnection(connection);
                return remainingQuantity;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static boolean validateItemById(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM item WHERE id=" + id);
            if (resultSet.next()) {
                statement.close();
                connectionPool.releaseConnection(connection);
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void saveItem() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO item(name, purchased_price, selling_Price, quantity, " +
                    "expiry_date, manufacture_date, description) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setFloat(2, this.purchasedPrice);
            statement.setFloat(3, this.sellingPrice);
            statement.setInt(4, this.quantity);
            statement.setDate(5, this.expiryDate);
            statement.setDate(6, this.manufactureDate);
            statement.setString(7, this.description);
            statement.executeUpdate();
            System.out.println("Item saved successfully!");
            ActivityLog.log("Item " + Item.getLatestItemId() + " saved", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.out.println("Error saving item!!!");
            e.printStackTrace();
        }
    }

    public void updateItem() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "UPDATE item SET name=?, purchased_price=?, selling_Price=?, quantity=?, expiry_date=?, manufacture_date=?, description=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setFloat(2, this.purchasedPrice);
            statement.setFloat(3, this.sellingPrice);
            statement.setInt(4, this.quantity);
            statement.setDate(5, this.expiryDate);
            statement.setDate(6, this.manufactureDate);
            statement.setString(7, this.description);
            statement.setInt(8, this.id);
            statement.executeUpdate();
            System.out.println("Item updated successfully!");
            ActivityLog.log("Item " + this.id + " updated", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Item getItemById(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM item WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Item item = new Item(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("purchased_price"),
                        resultSet.getFloat("selling_Price"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getDate("manufacture_date"),
                        resultSet.getString("description")
                );
                statement.close();
                connectionPool.releaseConnection(connection);
                return item;
            } else {
                System.out.println("Item not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteItem(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        boolean deleted = true;
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "DELETE FROM item WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Item deleted successfully!");
            ActivityLog.log("Item " + id + " deleted", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            deleted = false;
            e.printStackTrace();
        }

        return deleted;
    }

    public static List<Item> getExpiredItems(Date expiryDate) {
        List<Item> items = new ArrayList<>();
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }

        try (Connection connection = connectionPool.getConnection()) {
            Item item;
            String sql = "SELECT * FROM item WHERE expiry_date <= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, expiryDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                item = new Item(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("purchased_price"),
                        resultSet.getFloat("selling_Price"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getDate("manufacture_date"),
                        resultSet.getString("description")
                );
                items.add(item);
            }
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static List<Item> getAllItems() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }

        List<Item> items = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            Item item;
            String sql = "SELECT * FROM item";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                item = new Item(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("purchased_price"),
                        resultSet.getFloat("selling_Price"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getDate("manufacture_date"),
                        resultSet.getString("description")
                );
                items.add(item);
            }
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static int getLatestItemId() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM `item` ORDER BY id DESC LIMIT 1");
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                statement.close();
                connectionPool.releaseConnection(connection);
                return id;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    public static void reduceItemCount(int itemId, float quantity) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "UPDATE item SET quantity = quantity - ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setFloat(1, quantity);
            statement.setInt(2, itemId);
            statement.executeUpdate();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printItemHeading() {
        System.out.println();
        System.out.println("Item Id\t|\tItem Name\t|\tPurchased Price\t|\tSelling Price\t|\tQuantity\t|\t" +
                "Expiry Date\t|\tManufacture Date\t|\tDescription");
    }
}
