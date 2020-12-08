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

    public void insertEntry(Entry entry) {
        try {
            // Create template query
            String query = "INSERT INTO entries " +
                    "(entry_id, original_author_id, content, modification_date, creation_date) " +
                    "VALUES (?, ?, ?, ?, ?)";

            // Setup and populate statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, entry.getId());
            preparedStatement.setInt(2, entry.getOriginalAuthor().getId());
            preparedStatement.setString(3, entry.getContent());
            preparedStatement.setTimestamp(4, entry.getModificationDate());
            preparedStatement.setTimestamp(5, entry.getCreationDate());
            preparedStatement.execute();

            preparedStatement.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertAuthor(User author) {
        try {
            // Create template query
            String query = "INSERT INTO authors " +
                    "(author_id, author_name) " +
                    "VALUES (?, ?)";

            // Setup and populate statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, author.getId());
            preparedStatement.setString(2, author.getName());
            preparedStatement.execute();

            preparedStatement.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
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
