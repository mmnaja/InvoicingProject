import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ActivityLog {
    private int id;
    private Timestamp dateTime;
    private String action;
    private String query;
    private static BasicIConnectionPool connectionPool;

    public ActivityLog(int id, Timestamp dateTime, String action, String query) {
        this.id = id;
        this.dateTime = dateTime;
        this.action = action;
        this.query = query;
    }

    public ActivityLog(int id, Timestamp dateTime, String action) {
        this.id = id;
        this.dateTime = dateTime;
        this.action = action;
    }

    public ActivityLog(){}



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public static BasicIConnectionPool getConnectionPool() {
        return new BasicIConnectionPool("jdbc:mysql://localhost:3306/invoice", "root", null);
    }

    public static void log(String action, String query) {
        if(connectionPool == null) {
            connectionPool = getConnectionPool();
        }
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO activity_log(date_time, action, query) VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setString(2, action);
            statement.setString(3, query.substring(43));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}