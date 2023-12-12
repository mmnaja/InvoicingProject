import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String zip;
    private String province;
    private static BasicIConnectionPool connectionPool;

    public Customer() {}

    public Customer(int id, String name, String email, String phone, Date dateOfBirth, String gender, String city, String zip, String province, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.zip = zip;
        this.city = city;
        this.province = province;
        this.connectionPool = getConnectionPool();
    }

    public Customer(int id, String name, String phone, String gender) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.connectionPool = getConnectionPool();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String toString() {
        return "Id = " + this.id + "\nName = " + this.name + "\nContact No = " + this.phone;
    }

    private static BasicIConnectionPool getConnectionPool() {
        return new BasicIConnectionPool("jdbc:mysql://localhost:3306/invoice", "root", "");
    }

    public static boolean validateCustomerById(int customerId) {
        try (Connection connection = getConnectionPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM customer WHERE id=?");
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Customer getCustomerById(int customerId) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = getConnectionPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE id=? LIMIT 1");
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("city"),
                        resultSet.getString("zip"),
                        resultSet.getString("province"),
                        resultSet.getString("address")
                );
            } else {
                System.out.println("Customer not found with id: " + customerId);
                statement.close();
                connectionPool.releaseConnection(connection);
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Customer getCustomerByEmail(String customerEmail) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = getConnectionPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE email=? LIMIT 1");
            statement.setString(1, customerEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("city"),
                        resultSet.getString("zip"),
                        resultSet.getString("province"),
                        resultSet.getString("address")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Customer getCustomerByPhone(String customerPhone) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = getConnectionPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE phone=? LIMIT 1");
            statement.setString(1, customerPhone);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("city"),
                        resultSet.getString("zip"),
                        resultSet.getString("province"),
                        resultSet.getString("address")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void saveCustomer() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO customer (name, email, phone, date_of_birth, gender, address, city, zip, province) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setString(2, this.email);
            statement.setString(3, this.phone);
            statement.setDate(4, this.dateOfBirth);
            statement.setString(5, this.gender);
            statement.setString(6, this.address);
            statement.setString(7, this.city);
            statement.setString(8, this.zip);
            statement.setString(9, this.province);
            statement.executeUpdate();
            System.out.print("Customer saved successfully");
            ActivityLog.log("Customer " + Customer.getLatestCustomerId() + " saved", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException ex) {
            System.out.println("Unable to save customer");
            ex.printStackTrace();
        }
    }

    public static Customer getCustomers(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = getConnectionPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE id=? LIMIT 1");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("city"),
                        resultSet.getString("zip"),
                        resultSet.getString("province"),
                        resultSet.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCustomer(int id) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = getConnectionPool().getConnection()) {
            String sql = "DELETE FROM customer WHERE id=? LiMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Customer deleted successfully");
            ActivityLog.log("Customer " + id + " deleted", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomer() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql ="UPDATE customer SET name=?, email=?, phone=?, date_of_birth=?, gender=?, city=?," +
                    " zip=?, province=?, address=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.name);
            statement.setString(2, this.email);
            statement.setString(3, this.phone);
            statement.setDate(4, (java.sql.Date) this.dateOfBirth);
            statement.setString(5, this.gender);
            statement.setString(6, this.address);
            statement.setString(7, this.city);
            statement.setString(8, this.zip);
            statement.setString(9, this.province);
            statement.setInt(10, this.id);
            statement.executeUpdate();
            System.out.print("Customer updated successfully");
            ActivityLog.log("Customer " + this.id + " updated", statement.toString());
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int getLatestCustomerId() {
        if (connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM `customer` ORDER BY id DESC LIMIT 1");
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    public static Customer getCustomer() {
        Customer customer = null;
        System.out.print("Enter Customer Id, email or phone number : ");
        Scanner scanner = new Scanner(System.in);
        String customerIdentity = scanner.nextLine();
        Pattern patternEmail = Pattern.compile("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$");
        Pattern patternIntPhone = Pattern.compile("^(?:0|94|\\+94|0094)?\\d{9}$");
        Pattern patternLocalPhone = Pattern.compile("^\\d0?\\d{9}$");
        Pattern patternCustomerId = Pattern.compile("^\\d{1,5}$");
        if(patternCustomerId.matcher(customerIdentity).matches()) {
            customer = Customer.getCustomerById(Integer.parseInt(customerIdentity));
        } else if (patternLocalPhone.matcher(customerIdentity).matches() ||
                patternIntPhone.matcher(customerIdentity).matches()) {
            customer = Customer.getCustomerByPhone(customerIdentity);
        } else if (patternEmail.matcher(customerIdentity).matches()) {
            customer = Customer.getCustomerByEmail(customerIdentity);
        } else {
            System.out.println("Invalid customer Id");
            getCustomer();
        }
        if(customer == null) {
            System.out.println("Customer not found.");
            getCustomer();
        }

        return customer;
    }

    public static void printCustomerHeader() {
        System.out.println();
        System.out.println("Customer Id\t|\tCustomer Name\t|\t\tPhone\t\t|\t\tEmail\t\t\t|\tGender\t\t|\tDate of Birth" +
                "\t|\tAddress\t|\tCity\t|\tProvince\t|\tZip\t");
    }

    public List<Customer> getCustomerWithBDay() {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        List<Customer> customers = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(this.dateOfBirth.toString());
        LocalDate endDate = startDate.plusDays(7);
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM customer WHERE WEEK(date_of_birth) = WEEK(?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, this.dateOfBirth);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("city"),
                        resultSet.getString("zip"),
                        resultSet.getString("province"),
                        resultSet.getString("address")
                ));
            }
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (customers.isEmpty()) {
            System.out.println("No customers with birthday in the next 7 days");
        }
        return customers;
    }
}
