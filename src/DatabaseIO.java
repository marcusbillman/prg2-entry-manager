import java.sql.*;

public class DatabaseIO {
    private Connection connection;

    public DatabaseIO() {
        try {
            // Set up connection to database
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DatabaseLoginData.address + ":" + DatabaseLoginData.port + "/" + DatabaseLoginData.database +
                            "? allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    DatabaseLoginData.username, DatabaseLoginData.password);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    public EntryManager load() {
        EntryManager entryManager = null;

        try {
            // Setup statement
            Statement statement = connection.createStatement();

            // Create and execute query
            String query = "SELECT * FROM entries";
            ResultSet resultSet = statement.executeQuery(query);

            // Loop through the result set and print
            while (resultSet.next()) {
                String body = resultSet.getString("content");
                System.out.println(body);
            }

            statement.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return entryManager;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
