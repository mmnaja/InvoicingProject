import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItem {
    private int itemId;
    private int invoiceId;
    private float quantity;
    private float discount;
    private static BasicIConnectionPool connectionPool;

    public InvoiceItem() {
        connectionPool = getConnectionPool();
    }
    public InvoiceItem(int itemId, int invoiceId, float quantity, float discount) {
        this.itemId = itemId;
        this.invoiceId = invoiceId;
        this.quantity = quantity;
        this.discount = discount;
        connectionPool = getConnectionPool();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public static BasicIConnectionPool getConnectionPool() {
        return new BasicIConnectionPool("jdbc:mysql://localhost:3306/invoice", "root", null);
    }

    public void save() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try(Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO invoice_item (invoice_id, item_id, quantity, discount) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.invoiceId);
            statement.setInt(2, this.itemId);
            statement.setFloat(3, this.quantity);
            statement.setFloat(4, this.discount);
            statement.executeUpdate();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteInvoiceItem(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try(Connection connection = connectionPool.getConnection()) {
            String sql = "DELETE FROM invoice_item WHERE invoice_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static InvoiceItem getInvoiceItem(String id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try(Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM invoice_item WHERE invoice_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                InvoiceItem invoiceItem = new InvoiceItem(
                    resultSet.getInt("item_id"),
                    resultSet.getInt("invoice_id"),
                    resultSet.getFloat("quantity"),
                    resultSet.getFloat("discount")
                );
                statement.close();
                connectionPool.releaseConnection(connection);
                return invoiceItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  new InvoiceItem();
    }

    public void updateInvoiceItem() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "UPDATE invoice_item SET item_id=?, invoice_id=?, quantity=?, discount=? WHERE invoice_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.itemId);
            statement.setInt(2, this.invoiceId);
            statement.setFloat(3, this.quantity);
            statement.setFloat(4, this.discount);
            statement.setInt(5, invoiceId);
            statement.executeUpdate();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<InvoiceItem> getInvoiceItemsByInvoiceId(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try(Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM invoice_item WHERE invoice_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            if(resultSet != null) {
                while (resultSet.next()) {
                    InvoiceItem invoiceItem = new InvoiceItem (
                            resultSet.getInt("item_id"),
                            resultSet.getInt("invoice_id"),
                            resultSet.getFloat("quantity"),
                            resultSet.getFloat("discount")
                    );
                    invoiceItems.add(invoiceItem);
                }
                statement.close();
                connectionPool.releaseConnection(connection);
                return invoiceItems;
            } else {
                System.out.println("No invoice items found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InvoiceItem> getItemsByInvoiceId(int invoiceId) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try(Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM invoice_item WHERE invoice_id = " + invoiceId;
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            if(resultSet != null) {
                while (resultSet.next()) {
                    InvoiceItem invoiceItem = new InvoiceItem (
                            resultSet.getInt("item_id"),
                            resultSet.getInt("invoice_id"),
                            resultSet.getFloat("quantity"),
                            resultSet.getFloat("discount")
                    );
                    invoiceItems.add(invoiceItem);
                }
                statement.close();
                connectionPool.releaseConnection(connection);
                return invoiceItems;
            } else {
                System.out.println("No invoice items found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
