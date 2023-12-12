import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicIConnectionPool implements IConnectionPool {
    private String url;
    private String user;
    private String password;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();// List of used Connection
    private static int INITIAL_POOL_SIZE = 10;
    private static int MAX_TIMEOUT = 500;

    public BasicIConnectionPool(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
    }

    public  static BasicIConnectionPool create(String url, String user, String password)
    throws SQLException{
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for(int i = 0; i < INITIAL_POOL_SIZE; i++){
            pool.add(createConnection(url, user, password));
        }

        return new BasicIConnectionPool(url, user, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(connectionPool.isEmpty()) {
            if(usedConnections.size() < INITIAL_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password));
            } else {
                throw new RuntimeException("Maximum number of connections reached");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        if(!connection.isValid(MAX_TIMEOUT)) {
            connection = createConnection(url, user, password);
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public void shutDown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }

    private static Connection createConnection(String url, String user, String password)
            throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public static int getInitialPoolSize() {
        return INITIAL_POOL_SIZE;
    }

    public static void setInitialPoolSize(int initialPoolSize) {
        INITIAL_POOL_SIZE = initialPoolSize;
    }

    public static int getMaxTimeout() {
        return MAX_TIMEOUT;
    }

    public static void setMaxTimeout(int maxTimeout) {
        MAX_TIMEOUT = maxTimeout;
    }
}
