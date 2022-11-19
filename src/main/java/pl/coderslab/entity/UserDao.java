package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "Insert into users(username, email, password) values (?, ?, ?)";
    private static final String SELECT_USER_QUERY =
            "Select * from users where ";
    private static final String FIND_ALL_USERS_QUERY =
            "Select * from users";
    private static final String UPDATE_USER_QUERY =
            "Update users set username = ?, email = ?, password = ? where id = ?";
    private static final String DELETE_USER_QUERY =
            "Delete from users where id = ?";

    private static User[] users = new User[0];

    private void addToArray(User u) {
        users = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        users[users.length-1] = u; // Dodajemy obiekt na ostatniej pozycji.
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void create(User user) {

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User read(String condition, int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            User user = null;
            PreparedStatement statement = null;
            if (condition.equals("id")) {
                statement = conn.prepareStatement(SELECT_USER_QUERY + "id = ?");
            }

            statement.setInt(1, userId);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            User user = null;
            PreparedStatement statement =
                    conn.prepareStatement(FIND_ALL_USERS_QUERY);

            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if(!resultSet.isBeforeFirst()) return users;

            while (resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                addToArray(user);
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(UPDATE_USER_QUERY);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(DELETE_USER_QUERY);

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
