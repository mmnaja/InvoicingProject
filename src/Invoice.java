import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private Timestamp timeStamp;
    private int customerId;
    private String description;
    private static BasicIConnectionPool connectionPool;

    public Invoice() {}
    public Invoice(int id, Timestamp timestamp, int customerId, String description) {
        this.id = id;
        this.timeStamp = timestamp;
        this.customerId = customerId;
        this.description = description;
    }

    public Invoice(int id, Timestamp timestamp, int customerId) {
        this.id = id;
        this.timeStamp = timestamp;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public static int getLatestInvoiceId() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM `invoice` ORDER BY id DESC LIMIT 1");
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

    public static Invoice getLatestInvoice() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `invoice` ORDER BY id DESC LIMIT 1");
            if (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                statement.close();
                connectionPool.releaseConnection(connection);
                return invoice;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static List<Invoice> getInvoiceByCustomer(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM invoice WHERE customer_id=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Invoice> invoices = new ArrayList<>();
            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                invoices.add(invoice);
            }
            statement.close();
            connectionPool.releaseConnection(connection);
            return invoices;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Invoice getInvoiceById(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        Invoice invoice = null;
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM invoice WHERE id=?");
            statement.setInt(1,  id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                statement.close();
                connectionPool.releaseConnection(connection);
            }
        } catch (SQLException ex) {
            System.out.println("Item not found with id " + id);
            ex.printStackTrace();
        }

        return invoice;
    }

    public void save() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO invoice(id, timestamp, customer_id, description) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.id);
            statement.setTimestamp(2, this.timeStamp);
            statement.setInt(3, this.customerId);
            statement.setString(4, this.description);
            statement.executeUpdate();
            System.out.println("Invoice " + Invoice.getLatestInvoiceId() + " successfully saved");
            ActivityLog.log("Invoice " + Invoice.getLatestInvoiceId() + " saved", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateInvoice() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "UPDATE invoice SET timestamp=?, customer_id=?, description=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, this.timeStamp);
            statement.setInt(2, this.customerId);
            statement.setString(3, this.description);
            statement.setInt(4, this.id);
            statement.executeUpdate();
            System.out.println("Invoice " + this.id + " successfully updated");
            ActivityLog.log("Invoice " + this.id + " deleted", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteInvoice(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "DELETE FROM invoice WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Invoice " + id + " successfully deleted");
            ActivityLog.log("Invoice " + id + " deleted", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Invoice> getInvoicesForPeriod(Date startDate, Date endDate) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        List<Invoice> invoices = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM invoice WHERE DATE(timestamp) BETWEEN '" +
                     " ? ' AND ' ? '");
            statement.setDate(1, (startDate));
            statement.setDate(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                invoices.add(invoice);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

    public static List<Invoice> getInvoicesToDay() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        List<Invoice> invoices = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM invoice WHERE DATE(timestamp) = CURDATE()");
            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                invoices.add(invoice);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

    public static List<Invoice> getInvoiceByPeriod(Date strStartDate, Date strEndDate) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        List<Invoice> invoices = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM invoice WHERE timestamp BETWEEN ? AND ?");
            statement.setDate(1, strStartDate);
            statement.setDate(2, strEndDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                invoices.add(invoice);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

    public static List<Invoice> getInvoicesOfMonth() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        List<Invoice> invoices = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `invoice` WHERE " +
                    "YEAR(timestamp) = YEAR(CURDATE()) AND MONTH(timestamp) = MONTH(CURDATE())");
            while (resultSet.next()) {
                Invoice invoice = new Invoice(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("description")
                );
                invoices.add(invoice);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }
}