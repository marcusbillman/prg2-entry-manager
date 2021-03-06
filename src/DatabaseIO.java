import java.sql.*;

/**
 * Handles integration with a MySQL database using the Java MySQL connector.
 */
public class DatabaseIO {
    private Connection connection;

    /**
     * Constructor for DatabaseIO. Sets up the connection to the database using the credentials in the DatabaseLoginData
     * class.
     */
    public DatabaseIO() {
        try {
            // Set up connection to database
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DatabaseLoginData.address + ":" + DatabaseLoginData.port + "/" + DatabaseLoginData.database +
                            "? allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    DatabaseLoginData.username, DatabaseLoginData.password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    /**
     * Inserts an entry into the database.
     * @param entry the entry to insert
     */
    public void insertEntry(Entry entry) {
        try {
            // Create template query
            String query = "INSERT INTO entries " +
                    "(entry_id, author_id, content, modification_date, creation_date) " +
                    "VALUES (?, ?, ?, ?, ?)";

            // Setup and populate statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, entry.getId());
            preparedStatement.setInt(2, entry.getAuthor().getId());
            preparedStatement.setString(3, entry.getContent());
            preparedStatement.setTimestamp(4, entry.getModificationDate());
            preparedStatement.setTimestamp(5, entry.getCreationDate());
            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inserts an author (User) into the database
     * @param author the author (User) to insert
     */
    public void insertAuthor(User author) {
        try {
            // Create template query
            String query = "INSERT INTO authors " +
                    "(author_id, name) " +
                    "VALUES (?, ?)";

            // Setup and populate statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, author.getId());
            preparedStatement.setString(2, author.getName());
            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Updates the entry in the database that has the same id as the given entry.
     * @param entry pre-modified entry containing updated data to write to the database
     */
    public void updateEntry(Entry entry) {
        try {
            // Create template query
            String query = "UPDATE entries " +
                    "SET content=?, modification_date=? " +
                    "WHERE entry_id=?";

            // Setup and populate statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, entry.getContent());
            preparedStatement.setTimestamp(2, entry.getModificationDate());
            preparedStatement.setInt(3, entry.getId());
            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Restores the state of EntryManager from the data in the database.
     * @return EntryManager created from data in the database
     */
    public EntryManager load() {
        EntryManager entryManager = new EntryManager();

        try {
            // Setup statement
            Statement statement = connection.createStatement();

            // Create and execute query
            String query = "SELECT * FROM authors";
            ResultSet resultSet = statement.executeQuery(query);

            // Loop through the result set and create users in entryManager
            while (resultSet.next()) {
                int id = resultSet.getInt("author_id");
                String name = resultSet.getString("name");

                entryManager.createUser(name, id);
            }

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            // Setup statement
            Statement statement = connection.createStatement();

            // Create and execute query
            String query = "SELECT * FROM entries";
            ResultSet resultSet = statement.executeQuery(query);

            // Loop through the result set and create entries in entryManager
            while (resultSet.next()) {
                int id = resultSet.getInt("entry_id");
                int authorId = resultSet.getInt("author_id");
                String content = resultSet.getString("content");
                Timestamp modificationDate = resultSet.getTimestamp("modification_date");
                Timestamp creationDate = resultSet.getTimestamp("creation_date");

                User authorUser = entryManager.getUserById(authorId);

                entryManager.createEntry(content, authorUser, id, modificationDate, creationDate);
            }

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return entryManager;
    }

    /**
     * Closes the database connection safely.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
